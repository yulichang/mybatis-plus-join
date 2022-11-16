package com.github.yulichang.wrapper.resultmap;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.LambdaUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * result 标签
 *
 * @author yulichang
 * @since 1.3.0
 */
@Getter
@Setter(AccessLevel.PACKAGE)
public class Result {

    private boolean isId;

    private String property;

    private String column;

    private TableFieldInfo tableFieldInfo;

    private Class<?> javaType;

    private JdbcType jdbcType;

    private Class<? extends TypeHandler<?>> typeHandle;

    public Result() {
    }


    @SuppressWarnings({"UnusedReturnValue", "unused"})
    public static class Builder<E, T> {

        private final Result result;

        public Builder(boolean isId) {
            this.result = new Result();
            result.isId = isId;
        }

        public Builder<E, T> property(SFunction<T, ?> property) {
            result.property = LambdaUtils.getName(property);
            return this;
        }

        public Builder<E, T> column(SFunction<E, ?> column) {
            Class<E> entityClass = LambdaUtils.getEntityClass(column);
            String name = LambdaUtils.getName(column);
            TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
            Assert.notNull(tableInfo, "table not find by class <%s>", tableInfo);
            if (tableInfo.havePK() && tableInfo.getKeyProperty().equals(name)) {
                result.column = tableInfo.getKeyColumn();
                if (StringUtils.isBlank(result.property)) {
                    result.property = tableInfo.getKeyProperty();
                }
                result.javaType = tableInfo.getKeyType();
            } else {
                TableFieldInfo fieldInfo = tableInfo.getFieldList().stream().filter(i -> i.getField().getName().equals(name)).findFirst().orElse(null);
                Assert.notNull(fieldInfo, "table <%s> not find column <%>", tableInfo.getTableName(), name);
                result.column = fieldInfo.getColumn();
                result.tableFieldInfo = fieldInfo;
                if (StringUtils.isBlank(result.property)) {
                    result.property = fieldInfo.getProperty();
                }
                result.jdbcType = fieldInfo.getJdbcType();
                result.javaType = fieldInfo.getField().getType();
                result.typeHandle = fieldInfo.getTypeHandler();
            }
            return this;
        }

        public Builder<E, T> javaType(Class<?> javaType) {
            result.javaType = javaType;
            return this;
        }

        public Builder<E, T> jdbcType(JdbcType jdbcType) {
            result.jdbcType = jdbcType;
            return this;
        }

        public Result build() {
            return result;
        }

    }
}
