package com.github.yulichang.method.mp;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.github.yulichang.adapter.AdapterHelper;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.method.MPJBaseMethod;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 兼容原生方法
 *
 * @author yulichang
 * @since 1.2.0
 */
public interface TableAlias extends Constants, MPJBaseMethod {

    default String getTableName(TableInfo tableInfo) {
        String from = SqlScriptUtils.convertIf(" ${ew.from}",
                String.format("%s != null and %s != ''", "ew.from", "ew.from"), true);
        String alias = SqlScriptUtils.convertChoose(
                String.format("%s != null and %s instanceof %s", Constants.WRAPPER, Constants.WRAPPER, MPJBaseJoin.class.getName()),
                mpjTableName(tableInfo) + " ${ew.alias} " + NEWLINE + from, tableInfo.getTableName());
        return SPACE + alias;
    }

    /**
     * 复制tableInfo对象
     * 由于各个版本的MP的TableInfo对象存在差异，为了兼容性采用反射，而不是getter setter
     */
    @SuppressWarnings("JavaReflectionMemberAccess")
    default TableInfo copyAndSetTableName(TableInfo tableInfo, String tableName) {
        try {
            TableInfo table;
            try {
                table = TableInfo.class.getDeclaredConstructor(Class.class).newInstance(tableInfo.getEntityType());
            } catch (Exception e) {
                table = TableInfo.class.getDeclaredConstructor(Configuration.class, Class.class).newInstance(
                        AdapterHelper.getAdapter().mpjGetConfiguration(tableInfo), tableInfo.getEntityType());
            }
            //反射拷贝对象
            Field[] fields = TableInfo.class.getDeclaredFields();
            for (Field f : fields) {
                if (Modifier.isStatic(f.getModifiers()) ||
                        Modifier.isFinal(f.getModifiers())) {
                    continue;
                }
                f.setAccessible(true);
                f.set(table, f.get(tableInfo));
            }
            Field field = TableInfo.class.getDeclaredField("tableName");
            field.setAccessible(true);
            field.set(table, tableName);
            return table;
        } catch (Exception e) {
            throw ExceptionUtils.mpe("TableInfo 对象拷贝失败 -> " + tableInfo.getEntityType().getName());
        }
    }
}
