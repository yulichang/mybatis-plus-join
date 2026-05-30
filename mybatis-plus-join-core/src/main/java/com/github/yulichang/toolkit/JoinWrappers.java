package com.github.yulichang.toolkit;

import com.github.yulichang.wrapper.JoinQueryWrapper;

/**
 * @author yulichang
 * @since 1.4.4
 */
@SuppressWarnings("unused")
public class JoinWrappers {

    /**
     * JoinWrappers.&lt;UserDO&gt;lambda()
     */
    public static <T> JoinQueryWrapper<T> query() {
        return new JoinQueryWrapper<>();
    }

    /**
     * JoinWrappers.&lt;UserDO&gt;lambda("t")
     */
    public static <T> JoinQueryWrapper<T> query(String alias) {
        return new JoinQueryWrapper<>(alias);
    }

    /**
     * JoinWrappers.query(User.class)
     */
    public static <T> JoinQueryWrapper<T> query(Class<T> clazz) {
        return new JoinQueryWrapper<>(clazz);
    }

    /**
     * JoinWrappers.query("t", User.class)
     */
    public static <T> JoinQueryWrapper<T> query(String alias, Class<T> clazz) {
        return new JoinQueryWrapper<>(clazz, alias);
    }

    /**
     * JoinWrappers.query(user)
     */
    public static <T> JoinQueryWrapper<T> query(T entity) {
        return new JoinQueryWrapper<>(entity);
    }

    /**
     * JoinWrappers.query("t", user)
     */
    public static <T> JoinQueryWrapper<T> query(String alias, T entity) {
        return new JoinQueryWrapper<>(entity, alias);
    }
}
