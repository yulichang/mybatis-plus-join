package com.github.yulichang.adapter.jsqlparser.v46;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import java.util.function.Consumer;

/**
 * 字段解析
 *
 * @author yulichang
 * @since 1.4.12
 */
public class JSqlParserHelperV46 {

    public static void parserColum(String alias, String from, String selectSql, Consumer<String> columConsumer) {
        try {
            boolean parser = false;
            Statement statement = CCJSqlParserUtil.parse(String.format("SELECT %s FROM table %s %s", selectSql, alias, from));
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
                                        col = ((Column) selectExpressionItem.getExpression()).getColumnName();
                                    } else {
                                        col = selectExpressionItem.getExpression().toString();
                                    }
                                } else {
                                    col = selectExpressionItem.getAlias().getName();
                                }
                                if (isNotBlank(col)) {
                                    columConsumer.accept(col);
                                }
                            }
                        }
                        parser = true;
                    }
                }
                if (!parser)
                    throw ExceptionUtils.mpe("JSqlParser parser error <%s>", selectSql);
            }
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }


    public static boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
