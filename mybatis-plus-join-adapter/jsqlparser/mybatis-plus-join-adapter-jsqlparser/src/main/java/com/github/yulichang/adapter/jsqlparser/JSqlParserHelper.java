package com.github.yulichang.adapter.jsqlparser;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;

import java.util.function.Consumer;

/**
 * 字段解析
 *
 * @author yulichang
 * @since 1.4.12
 */
public class JSqlParserHelper {

    public static void parserColum(String alias, String from, String selectSql, Consumer<String> columConsumer) {
        try {
            boolean parser = false;
            Statement statement = CCJSqlParserUtil.parse(String.format("SELECT %s FROM table %s %s", selectSql, alias, from));
            if (statement instanceof PlainSelect) {
                PlainSelect plainSelect = (PlainSelect) statement;
                if (CollectionUtils.isNotEmpty(plainSelect.getSelectItems())) {
                    for (SelectItem<?> item : plainSelect.getSelectItems()) {
                        String col;
                        if (item.getAlias() == null) {
                            if (item.getExpression() instanceof Column) {
                                Column column = (Column) item.getExpression();
                                col = column.getColumnName();
                            } else {
                                col = item.getExpression().toString();
                            }
                        } else {
                            col = item.getAlias().getName();
                        }
                        if (StringUtils.isNotBlank(col)) {
                            columConsumer.accept(StringUtils.getTargetColumn(col));
                        }
                    }
                    parser = true;
                }
            }
            if (!parser)
                throw ExceptionUtils.mpe("JSqlParser parser error <%s>", selectSql);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
