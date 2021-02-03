package com.github.yulichang.wrapper;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import com.github.yulichang.toolkit.MyLambdaUtils;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

/**
 * copy {@link com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper}
 */
@SuppressWarnings("serial")
public abstract class MyAbstractLambdaWrapper<T, Children extends MyAbstractLambdaWrapper<T, Children>>
        extends MyAbstractWrapper<T, Children> {

    private Map<String, ColumnCache> columnMap = null;
    private boolean initColumnMap = false;

    /**
     * 参与连接的表<class,别名>
     */
    protected Map<Class<?>, SubClass<?, ?>> subTable = new HashMap<>();

    @SuppressWarnings("unchecked")
    protected <X> String columnsToString(SFunction<X, ?>... columns) {
        return columnsToString(true, columns);
    }

    @Override
    protected <X> String columnToString(X column) {
        return columnToString((SFunction<?, ?>) column, true);
    }

    @SuppressWarnings("unchecked")
    protected <X> String columnsToString(boolean onlyColumn, SFunction<X, ?>... columns) {
        return Arrays.stream(columns).map(i -> columnToString(i, onlyColumn)).collect(joining(StringPool.COMMA));
    }

    @Override
    protected <X> String columnsToString(X... columns) {
        return Arrays.stream(columns).map(i -> columnToString((SFunction<?, ?>) i, true)).collect(joining(StringPool.COMMA));
    }

    protected String columnToString(SFunction<?, ?> column, boolean onlyColumn) {
        TableInfo info = TableInfoHelper.getTableInfo(MyLambdaUtils.getEntityClass(column));
        Assert.notNull(info, "can not find table for lambda");
        return info.getTableName() + StringPool.DOT + getColumn(LambdaUtils.resolve(column), onlyColumn);
    }


    /**
     * 获取 SerializedLambda 对应的列信息，从 lambda 表达式中推测实体类
     * <p>
     * 如果获取不到列信息，那么本次条件组装将会失败
     *
     * @param lambda     lambda 表达式
     * @param onlyColumn 如果是，结果: "name", 如果否： "name" as "name"
     * @return 列
     * @throws com.baomidou.mybatisplus.core.exceptions.MybatisPlusException 获取不到列信息时抛出异常
     * @see SerializedLambda#getImplClass()
     * @see SerializedLambda#getImplMethodName()
     */
    private String getColumn(SerializedLambda lambda, boolean onlyColumn) {
        Class<?> aClass = lambda.getInstantiatedType();
        tryInitCache(aClass);
        String fieldName = PropertyNamer.methodToProperty(lambda.getImplMethodName());
        ColumnCache columnCache = getColumnCache(fieldName, aClass);
        return onlyColumn ? columnCache.getColumn() : columnCache.getColumnSelect();
    }

    private void tryInitCache(Class<?> lambdaClass) {
        if (!initColumnMap) {
            final Class<T> entityClass = getEntityClass();
            if (entityClass != null) {
                lambdaClass = entityClass;
            }
            columnMap = LambdaUtils.getColumnMap(lambdaClass);
            initColumnMap = true;
        }
        Assert.notNull(columnMap, "can not find lambda cache for this entity [%s]", lambdaClass.getName());
    }

    private ColumnCache getColumnCache(String fieldName, Class<?> lambdaClass) {
        ColumnCache columnCache = columnMap.get(LambdaUtils.formatKey(fieldName));
        if (Objects.isNull(columnCache)) {
            columnCache = new ColumnCache(fieldName, null);
        }
        return columnCache;
    }

    public static class SubClass<L, R> {

        private String tableAlias;

        private SFunction<L, ?> left;

        private SFunction<R, ?> right;


        public SubClass(String tableAlias, SFunction<L, ?> left, SFunction<R, ?> right) {
            this.tableAlias = tableAlias;
            this.left = left;
            this.right = right;
        }

        public String getTableAlias() {
            return tableAlias;
        }

        public void setTableAlias(String tableAlias) {
            this.tableAlias = tableAlias;
        }

        public SFunction<L, ?> getLeft() {
            return left;
        }

        public void setLeft(SFunction<L, ?> left) {
            this.left = left;
        }

        public SFunction<R, ?> getRight() {
            return right;
        }

        public void setRight(SFunction<R, ?> right) {
            this.right = right;
        }
    }
}
