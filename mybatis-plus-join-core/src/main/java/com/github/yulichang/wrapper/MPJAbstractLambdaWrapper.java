package com.github.yulichang.wrapper;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.config.ConfigProperties;
import com.github.yulichang.config.enums.LogicDelTypeEnum;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.toolkit.TableHelper;
import com.github.yulichang.toolkit.TableList;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.wrapper.enums.PrefixEnum;
import com.github.yulichang.wrapper.segments.SelectCache;
import lombok.Getter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static java.util.stream.Collectors.joining;

/**
 * 参考 {@link com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper}
 *
 * @author yulichang
 */
@SuppressWarnings({"DuplicatedCode", "unused"})
public abstract class MPJAbstractLambdaWrapper<T, Children extends MPJAbstractLambdaWrapper<T, Children>>
        extends MPJAbstractWrapper<T, Children> {

    /**
     * 主表别名
     */
    protected String alias = ConfigProperties.tableAlias;
    /**
     * 是否构建是否存在一对多
     */
    @Getter
    protected boolean resultMap = false;
    /**
     * 表序号
     */
    protected int tableIndex = 1;

    /**
     * 主表 表名处理方法
     */
    protected boolean dynamicTableName = false;

    /**
     * 主表 表名处理方法
     */
    protected Function<String, String> tableFunc;

    /**
     * 逻辑删除位置
     */
    protected LogicDelTypeEnum logicDelType = ConfigProperties.logicDelType;

    /**
     * 设置表别名
     * 设置表别名注意sql注入问题
     *
     * @return 自定义表别名
     */
    public Children setTableName(Function<String, String> tableFunc) {
        if (isMain) {
            if (tableFunc != null) {
                this.dynamicTableName = true;
                this.tableFunc = tableFunc;
            }
        } else {
            this.tableName = tableFunc.apply(this.tableName);
        }
        return typedThis;
    }

    public String getTableName(String tableName) {
        if (isMain) {
            if (dynamicTableName) {
                return tableFunc.apply(tableName);
            }
            return tableName;
        }
        return super.getTableName();
    }


    public String getTableNameEnc(String tableName) {
        Class<T> entityClass = getEntityClass();
        if (entityClass != null) {
            TableInfo tableInfo = TableHelper.get(entityClass);
            if (tableInfo != null) {
                if (dynamicTableName) {
                    return tableFunc.apply(tableInfo.getTableName());
                }
                return tableInfo.getTableName();
            }
        }
        String decode;
        try {
            decode = URLDecoder.decode(tableName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            decode = tableName;
        }
        if (dynamicTableName) {
            return tableFunc.apply(decode);
        }
        return decode;
    }

    @Override
    protected <X> String columnToString(Integer index, X column, boolean isJoin, PrefixEnum prefixEnum) {
        return columnToString(index, (SFunction<?, ?>) column, isJoin, prefixEnum);
    }

    @Override
    @SafeVarargs
    protected final <X> String columnsToString(Integer index, boolean isJoin, PrefixEnum prefixEnum, X... columns) {
        return Arrays.stream(columns).map(i -> columnToString(index, (SFunction<?, ?>) i, isJoin, prefixEnum)).collect(joining(StringPool.COMMA));
    }

    protected String columnToString(Integer index, SFunction<?, ?> column, boolean isJoin, PrefixEnum prefixEnum) {
        Class<?> entityClass = LambdaUtils.getEntityClass(column);
        return getDefault(index, entityClass, isJoin, prefixEnum) + StringPool.DOT + getCache(column).getColumn();
    }

    protected SelectCache getCache(SFunction<?, ?> fn) {
        Class<?> aClass = LambdaUtils.getEntityClass(fn);
        Map<String, SelectCache> cacheMap = ColumnCache.getMapField(aClass);
        return cacheMap.get(LambdaUtils.getName(fn));
    }

    /**
     * 返回前缀
     */
    protected String getDefault(Integer index, Class<?> clazz, boolean isJoin, PrefixEnum prefixEnum) {
        if (prefixEnum == PrefixEnum.ON_FIRST) {
            return tableList.getPrefix(index, clazz, false);
        } else if (prefixEnum == PrefixEnum.ON_SECOND) {
            return isJoin ? tableList.getPrefixOther(index, clazz) : tableList.getPrefix(index, clazz, false);
        } else if (prefixEnum == PrefixEnum.CD_FIRST) {
            return tableList.getPrefix(index, clazz, false);
        } else if (prefixEnum == PrefixEnum.CD_SECOND) {
            return isJoin ? tableList.getPrefixOther(index, clazz) :
                    tableList.getPrefix(index, clazz, false);
        } else if (prefixEnum == PrefixEnum.CD_ON_FIRST) {
            return tableList.getPrefix(index, clazz, false);
        } else if (prefixEnum == PrefixEnum.CD_ON_SECOND) {
            return isJoin ? tableList.getPrefixOther(index, clazz) :
                    tableList.getPrefix(index, clazz, false);
        } else {
            return tableList.getAlias();
        }
    }

    /**
     * 必要的初始化
     */
    protected void initNeed() {
        paramNameSeq = new AtomicInteger(0);
        paramNameValuePairs = new HashMap<>(16);
        expression = new MergeSegments();
        lastSql = SharedString.emptyString();
        sqlComment = SharedString.emptyString();
        sqlFirst = SharedString.emptyString();
        tableList = new TableList();
        tableList.setAlias(alias);
    }
}
