package com.github.yulichang.kt.resultmap;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.toolkit.KtUtils;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.wrapper.resultmap.IResult;
import com.github.yulichang.wrapper.segments.SelectCache;
import kotlin.reflect.KProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * result 标签
 *
 * @author yulichang
 * @since 1.4.6
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
            result.jdbcType = Optional.ofNullable(selectCache.getTableFieldInfo()).map(TableFieldInfo::getJdbcType).orElse(null);
        }

        public Builder<T> property(KProperty<?> property) {
            result.property = property.getName();
            return this;
        }

        public <E> Builder<T> column(KProperty<?> column) {
            Map<String, SelectCache> normalMap = ColumnCache.getMapField(KtUtils.ref(column));
            String name = column.getName();
            SelectCache normal = normalMap.get(name);
            result.selectNormal = normal;
            if (StringUtils.isBlank(result.property)) {
                result.property = normal.getColumProperty();
            }
            if (Objects.isNull(result.javaType)) {
                result.javaType = normal.getColumnType();
            }
            if (Objects.isNull(result.jdbcType)) {
                result.jdbcType = Objects.isNull(normal.getTableFieldInfo()) ? null : normal.getTableFieldInfo().getJdbcType();
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
