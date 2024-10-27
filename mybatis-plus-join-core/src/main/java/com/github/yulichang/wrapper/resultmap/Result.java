package com.github.yulichang.wrapper.resultmap;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.toolkit.StrUtils;
import com.github.yulichang.toolkit.support.ColumnCache;
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

    private String index;

    private SelectCache selectNormal;

    private String property;

    private Class<?> javaType;

    private JdbcType jdbcType;

    public Result() {
    }


    @SuppressWarnings({"UnusedReturnValue", "unused"})
    public static class Builder<T> {

        private final Result result;

        public Builder(boolean isId, String index) {
            this.result = new Result();
            result.isId = isId;
            result.index = index;
        }

        public Builder(boolean isId, String index, SelectCache selectCache) {
            this.result = new Result();
            result.isId = isId;
            result.index = index;
            result.selectNormal = selectCache;
            result.property = selectCache.getColumProperty();
            result.javaType = selectCache.getColumnType();
            result.jdbcType = selectCache.getJdbcType();
        }

        public Builder<T> property(SFunction<T, ?> property) {
            result.property = LambdaUtils.getName(property);
            return this;
        }

        public <E> Builder<T> column(SFunction<E, ?> column) {
            Class<E> entityClass = LambdaUtils.getEntityClass(column);
            Map<String, SelectCache> normalMap = ColumnCache.getMapField(entityClass);
            String name = LambdaUtils.getName(column);
            SelectCache normal = normalMap.get(name);
            result.selectNormal = normal;
            if (StrUtils.isBlank(result.property)) {
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
