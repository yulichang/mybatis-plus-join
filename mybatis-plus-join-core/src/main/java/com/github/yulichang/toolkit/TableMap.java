package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.github.yulichang.apt.BaseColumn;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class TableMap implements Serializable {

    @Setter
    @Getter
    private TableMap parent;

    @Getter
    private final BaseColumn<?> root;

    @Getter
    @Setter
    private String rootAlias;

    private final Map<BaseColumn<?>, String> tableMap = new LinkedHashMap<>();

    public TableMap(BaseColumn<?> root, String rootAlias) {
        this.root = root;
        this.rootAlias = rootAlias;
    }

    public String put(BaseColumn<?> key, String value) {
        return tableMap.put(key, value);
    }

    public String get(BaseColumn<?> key) {
        return get(this, key);
    }

    private String get(TableMap tableMap, BaseColumn<?> key) {
        if (null != key.getAlias()) {
            return key.getAlias();
        }
        if (key == tableMap.root) {
            return tableMap.rootAlias;
        }
        String pf = tableMap.tableMap.get(key);
        if (null == pf) {
            if (tableMap.parent == null) {
                throw ExceptionUtils.mpe("table not found %s", key.getColumnClass().getName());
            } else {
                return get(tableMap.parent, key);
            }
        }
        return pf;
    }

    public Collection<BaseColumn<?>> keyList() {
        return tableMap.keySet();
    }

    public boolean isEmpty() {
        return this.tableMap.isEmpty();
    }

    public void clear() {
        this.tableMap.clear();
    }
}
