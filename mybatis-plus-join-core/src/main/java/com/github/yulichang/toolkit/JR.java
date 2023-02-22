package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Field;
import java.util.Objects;

@SuppressWarnings("Convert2MethodRef")
public class JR {

    @SuppressWarnings("JavaReflectionMemberAccess")
    public static boolean mpjHasLogic(TableInfo tableInfo) {
        return tryThrowable(() -> tableInfo.isWithLogicDelete(), () -> {
            try {
                Field field = TableInfo.class.getDeclaredField("logicDelete");
                field.setAccessible(true);
                return (boolean) field.get(tableInfo);
            } catch (Exception ee) {
                throw new RuntimeException(ee);
            }
        });
    }

    public static boolean mpjIsPrimitive(TableFieldInfo tableFieldInfo) {
        return tryThrowable(() -> tableFieldInfo.isPrimitive(), () -> tableFieldInfo.getPropertyType().isPrimitive());
    }

    public static TableFieldInfo mpjGetLogicField(TableInfo tableInfo) {
        return tryThrowable(() -> tableInfo.getLogicDeleteFieldInfo(), () -> tableInfo.getFieldList().stream().filter(f ->
                Objects.nonNull(f.getLogicNotDeleteValue()) ||
                        Objects.nonNull(f.getLogicDeleteValue())).findFirst().orElse(null));
    }

    public static boolean mpjHasPK(TableInfo tableInfo) {
        return tryThrowable(() -> tableInfo.havePK(), () -> Objects.nonNull(tableInfo.getKeyProperty()));
    }

    public static Configuration mpjGetConfiguration(TableInfo tableInfo) {
        return tryThrowable(() -> tableInfo.getConfiguration(), () -> {
            try {
                Field field = TableInfo.class.getDeclaredField("configuration");
                field.setAccessible(true);
                return (Configuration) field.get(tableInfo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static <T> T tryThrowable(F<T> fn, F<T> orElse) {
        try {
            return fn.apply();
        } catch (Throwable throwable) {
            return orElse.apply();
        }
    }

    @FunctionalInterface
    public interface F<T> {
        T apply();
    }
}
