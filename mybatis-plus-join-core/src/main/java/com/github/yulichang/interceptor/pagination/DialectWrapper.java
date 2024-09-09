package com.github.yulichang.interceptor.pagination;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserGlobal;
import com.baomidou.mybatisplus.extension.plugins.pagination.DialectModel;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import lombok.Getter;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author yulichang
 * @since 1.5.0
 */
public class DialectWrapper {

    private final IDialect dialect;

    @Getter
    private final List<ParameterMapping> fullMappings = new ArrayList<>();
    @Getter
    private final List<ParameterMapping> pageMappings = new ArrayList<>();

    @Getter
    private String finallySql;

    public DialectWrapper(IDialect dialect) {
        this.dialect = dialect;
    }

    public void buildPaginationSql(String originalSql, List<ParameterMapping> mappings, long offset, long limit,
                                   Consumer<DialectModel> consumers, MappedStatement ms, Object parameter) {
        try {
            // 解析sql
            Select select = (Select) JsqlParserGlobal.parse(originalSql);
            if (select instanceof SetOperationList) {
                throw ExceptionUtils.mpe("不支持 union 对多分页");
            }
            //获取 ? 出现的次数
            String sourceSql = select.toString();
            int count = ParseHelper.countChar(sourceSql);
            String formatSql;
            if (count == mappings.size()) {
                formatSql = ParseHelper.decode(sourceSql);
            } else {
                SqlSource sqlSource = ms.getSqlSource();
                if (sqlSource instanceof DynamicSqlSource) {
                    formatSql = ParseHelper.getOriginalSql(parameter, (DynamicSqlSource) sqlSource);
                } else {
                    throw ExceptionUtils.mpe("unknown type: " + sqlSource.getClass().getName());
                }
            }
            //分页的sql
            PlainSelect pageSql = (PlainSelect) JsqlParserGlobal.parse(formatSql);

            List<Join> joins = pageSql.getJoins();
            if (CollectionUtils.isNotEmpty(joins)) {
                pageSql.setJoins(null);
            }

            pageSql.setSelectItems(Collections.singletonList(new SelectItem<>(new Column().withColumnName("*"))));

            // 替换回 ? 同步mappings顺序
            String repSql = pageSql.toString();
            Map<Integer, ParameterMapping> sortMap = new HashMap<>();
            repSql = ParseHelper.encode(mappings, count, repSql, sortMap);
            this.pageMappings.addAll(sortMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue).collect(Collectors.toList()));

            DialectModel dialectModel = dialect.buildPaginationSql(repSql, offset, limit);
            consumers.accept(dialectModel);

            // 带分页方言的sql
            String formatDialectSql = ParseHelper.decode(dialectModel.getDialectSql(), i -> "s" + i);

            //完整的sql
            PlainSelect fullSql = (PlainSelect) JsqlParserGlobal.parse(formatSql);

            PlainSelect finalPlainSelect = new PlainSelect();
            finalPlainSelect.setSelectItems(fullSql.getSelectItems());
            finalPlainSelect.setDistinct(fullSql.getDistinct());
            finalPlainSelect.setFromItem(
                    new Table()
                            .withName("(" + formatDialectSql + ")")
                            .withAlias(new Alias(fullSql.getFromItem().getAlias().getName(), false)));
            finalPlainSelect.setJoins(fullSql.getJoins());

            String finalSql = finalPlainSelect.toString();
            Map<Integer, ParameterMapping> finalSortMap = new HashMap<>();
            //page部分
            for (int i = 0; i < count; i++) {
                String repStr = ParseHelper.format.apply("s" + i);
                int i1 = finalSql.indexOf(repStr);
                if (i1 != -1) {
                    finalSql = finalSql.replace(repStr, "?");
                    finalSortMap.put(i1, this.pageMappings.get(i));
                }
            }
            //其他部分
            finalSql = ParseHelper.encode(mappings, count, finalSql, finalSortMap);
            this.fullMappings.addAll(finalSortMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue).collect(Collectors.toList()));
            this.finallySql = finalSql;
        } catch (Exception e) {
            throw ExceptionUtils.mpe("not support this sql, please use xml. error sql: " + originalSql);
        }
    }


}
