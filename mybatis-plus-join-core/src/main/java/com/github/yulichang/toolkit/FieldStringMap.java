package com.github.yulichang.toolkit;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author yulichang
 * @see HashMap
 * @since 1.4.7.3
 */
public class FieldStringMap<V> extends HashMap<String, V> {

    @Override
    public V get(Object key) {
        V v = super.get(key);
        if (Objects.isNull(v)) {
            String k = (String) key;
            return entrySet().stream().filter(f -> k.equalsIgnoreCase(f.getKey())).findFirst()
                    .map(Entry::getValue).orElse(null);
        }
        return v;
    }
}