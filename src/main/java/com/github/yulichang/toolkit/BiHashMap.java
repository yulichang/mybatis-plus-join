package com.github.yulichang.toolkit;

import java.util.HashMap;
import java.util.Map;

public class BiHashMap<K, V, E> {

    private final Map<K, Map<V, E>> map;

    public BiHashMap() {
        this.map = new HashMap<>();
    }


    public E get(K k, V v) {
        return map.containsKey(k) ? map.get(k).get(v) : null;
    }

    public void put(K k, V v, E e) {
        if (map.containsKey(k)) {
            map.get(k).put(v, e);
        } else {
            Map<V, E> veMap = new HashMap<>();
            veMap.put(v, e);
            map.put(k, veMap);
        }
    }
}
