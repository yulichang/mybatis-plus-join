package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.wrapper.interfaces.SelectWrapper;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import java.util.function.Consumer;

/**
 * @author yulichang
 * @since 1.4.10
 */
public final class JSqlParserHelper {

    /**
     * 解析sql select字段 刚接触JSqlParser有更好的用法欢迎PR
     *
     * @param selectSql     要解析的select sql片段
     * @param columConsumer 解析的字段处理
     */
    public static void paresColum(SelectWrapper<?, ?> wrapper, String selectSql, Consumer<String> columConsumer) throws Exception {
        Statement statement = CCJSqlParserUtil.parse(String.format("SELECT %s FROM table %s %s", selectSql, wrapper.getAlias(), wrapper.getFrom()));
        if (statement instanceof Select) {
            Select select = (Select) statement;
            SelectBody selectBody = select.getSelectBody();
            if (selectBody instanceof PlainSelect) {
                PlainSelect plainSelect = (PlainSelect) selectBody;
                if (CollectionUtils.isNotEmpty(plainSelect.getSelectItems())) {
                    for (SelectItem item : plainSelect.getSelectItems()) {
                        if (item instanceof SelectExpressionItem) {
                            String col;
                            SelectExpressionItem selectExpressionItem = (SelectExpressionItem) item;
                            if (null == selectExpressionItem.getAlias()) {
                                if (selectExpressionItem.getExpression() instanceof Column) {
                                    col = StringUtils.getTargetColumn(((Column) selectExpressionItem.getExpression()).getColumnName());
                                } else {
                                    col = StringUtils.getTargetColumn(selectExpressionItem.getExpression().toString());
                                }
                            } else {
                                col = StringUtils.getTargetColumn(selectExpressionItem.getAlias().getName());
                            }
                            if (StringUtils.isNotBlank(col)) {
                                columConsumer.accept(col);
                            }
                        }
                    }
                }
            }
        }
    }
}
