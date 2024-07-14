package com.github.yulichang.toolkit;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableMap<K, V> {

    @Getter
    private final K root;
    @Getter
    private final String rootAlias;

    private final Map<K, V> map = new HashMap<>();

    private final List<K> list = new ArrayList<>();

    public TableMap(K root, String rootAlias) {
        this.root = root;
        this.rootAlias = rootAlias;
    }

    public V put(K key, V value) {
        V v = this.map.put(key, value);
        if (null == v) {
            this.list.add(key);
        }
        return v;
    }

    public V get(K key) {
        return this.map.get(key);
    }

    public List<K> keyList() {
        return this.list;
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public void clear() {
        this.map.clear();
        this.list.clear();
    }

}
