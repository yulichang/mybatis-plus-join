package com.github.yulichang.toolkit;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author yulichang
 * @see HashMap
 * @since 1.4.8
 */
public class FieldStringMap<V> extends HashMap<String, V> {

    @Override
    public V get(Object key) {
        return super.get(key == null ? null : ((String) key).toUpperCase(Locale.ENGLISH));
    }

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(key == null ? null : ((String) key).toUpperCase(Locale.ENGLISH));
    }
}