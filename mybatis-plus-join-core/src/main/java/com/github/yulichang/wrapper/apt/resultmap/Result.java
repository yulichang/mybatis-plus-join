package com.github.yulichang.wrapper.apt.resultmap;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.apt.BaseColumn;
import com.github.yulichang.apt.Column;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.wrapper.resultmap.IResult;
import com.github.yulichang.wrapper.segments.SelectCache;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;

import java.util.Map;
import java.util.Objects;

/**
 * result 标签
 *
 * @author yulichang
 * @since 1.3.0
 */
@Getter
@Setter(AccessLevel.PACKAGE)
public class Result implements IResult {

    private boolean isId;

    private BaseColumn<?> baseColumn;

    private Column column;

    private SelectCache selectNormal;

    private String property;

    private Class<?> javaType;

    private JdbcType jdbcType;

    public Result() {
    }

    @Override
    public String getIndex() {
        return null;
    }


    @SuppressWarnings({"UnusedReturnValue", "unused"})
    public static class Builder<T> {

        private final Result result;

        public Builder(boolean isId, Column column) {
            this.result = new Result();
            result.isId = isId;
            result.column = column;
            result.baseColumn = column.getRoot();
        }

        public Builder(boolean isId, Column column, SelectCache selectCache) {
            this.result = new Result();
            result.isId = isId;
            result.column = column;
            result.baseColumn = column.getRoot();
            result.selectNormal = selectCache;
            result.property = selectCache.getColumProperty();
            result.javaType = selectCache.getColumnType();
            result.jdbcType = selectCache.getJdbcType();
        }

        public Builder(boolean isId, BaseColumn<?> baseColumn, SelectCache selectCache) {
            this.result = new Result();
            result.isId = isId;
            result.column = null;
            result.baseColumn = baseColumn;
            result.selectNormal = selectCache;
            result.property = selectCache.getColumProperty();
            result.javaType = selectCache.getColumnType();
            result.jdbcType = selectCache.getJdbcType();
        }

        public Builder<T> property(SFunction<T, ?> property) {
            result.property = LambdaUtils.getName(property);
            return this;
        }

        public Builder<T> column(Column column) {
            Map<String, SelectCache> normalMap = ColumnCache.getMapField(column.getClazz());
            SelectCache normal = normalMap.get(column.getProperty());
            result.selectNormal = normal;
            result.column = column;
            if (StringUtils.isBlank(result.property)) {
                result.property = normal.getColumProperty();
            }
            if (Objects.isNull(result.javaType)) {
                result.javaType = normal.getColumnType();
            }
            if (Objects.isNull(result.jdbcType)) {
                result.jdbcType = normal.getJdbcType();
            }
            return this;
        }

        public Builder<T> javaType(Class<?> javaType) {
            result.javaType = javaType;
            return this;
        }

        public Builder<T> jdbcType(JdbcType jdbcType) {
            result.jdbcType = jdbcType;
            return this;
        }

        public Result build() {
            return result;
        }

    }
}
