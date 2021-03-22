package com.github.yulichang.common.support.alias;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 字段添加别名
 * 使用方法:
 * <p>
 * select ${ew.aliasColumn("table","t")} from table t ${ew.customSqlSegment("t")}
 * </p>
 * 对应sql
 * <p>
 * select t.id,t.sex from table t where t.id = ? and t.sex = ?
 * </p>
 * 注意:
 * 官方的自定义sql是ew.customSqlSegment,不带括号,会调用getCustomSqlSegment()方法
 * 带别名的是 ew.customSqlSegment("t") 带括号
 * 括号中的别名必须带双引号
 *
 * @author yulichang
 */
public class AliasLambdaQueryWrapper<T> extends LambdaQueryWrapper<T> {

    /**
     * 别名查询字段缓存
     */
    private static final Map<Class<?>, Map<String, String>> ALIAS_COLUMN_CACHE = new ConcurrentHashMap<>();

    /**
     * 字段别名
     */
    private String alias;

    /**
     * 重写字段序列化方法
     */
    @Override
    protected String columnToString(SFunction<T, ?> column, boolean onlyColumn) {
        String as = super.columnToString(column, onlyColumn);
        return StringUtils.isBlank(alias) ? as : (alias + StringPool.DOT + as);
    }

    /**
     * ew.customSqlSegment("t")
     */
    public String customSqlSegment(String alias) {
        this.alias = alias;
        return super.getCustomSqlSegment();
    }

    /**
     * ew.customSqlSegment("table_name", "t")
     *
     * @param tableName 数据库表名
     * @param alias     别名
     */
    public String aliasColumn(String tableName, String alias) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(tableName);
        Assert.notNull(tableInfo, "未找到对应的表 -> %s", tableName);
        Assert.isTrue(StringUtils.isNotBlank(alias), "别名不能为空");

        this.alias = alias;
        Map<String, String> aliasMap = ALIAS_COLUMN_CACHE.get(tableInfo.getEntityType());
        if (CollectionUtils.isEmpty(aliasMap)) {
            aliasMap = new ConcurrentHashMap<>();
            String s = concat(tableInfo, alias);
            aliasMap.put(alias, s);
            ALIAS_COLUMN_CACHE.put(tableInfo.getEntityType(), aliasMap);
            return s;
        } else {
            if (!aliasMap.containsKey(alias)) {
                String s = concat(tableInfo, alias);
                aliasMap.put(alias, s);
                return s;
            } else {
                return aliasMap.get(alias);
            }
        }
    }

    private String concat(TableInfo tableInfo, String alias) {
        String s = tableInfo.getFieldList().stream().map(
                i -> alias + StringPool.DOT + i.getColumn()).collect(Collectors.joining(StringPool.COMMA));
        if (tableInfo.havePK()) {
            s = alias + StringPool.DOT + tableInfo.getKeyColumn() + StringPool.COMMA + s;
        }
        return s;
    }
}
