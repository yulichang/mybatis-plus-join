package com.github.yulichang.toolkit;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author yulichang
 * @see HashMap
 * @since 1.4.7.3
 */
public class FieldStringMap<V> extends HashMap<String, V> {

    @Override
    public V get(Object key) {
        return super.get(((String) key).toUpperCase(Locale.ENGLISH));
    }

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(((String) key).toUpperCase(Locale.ENGLISH));
    }
}