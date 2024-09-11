package com.github.yulichang.extension.apt.matedata;

import lombok.Getter;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * @author yulichang
 * @since 1.5.0
 */
@Getter
public class Column implements Serializable {

    private final BaseColumn<?> root;

    private final String property;

    public Column(BaseColumn<?> root, String property) {
        this.root = root;
        this.property = property;
    }

    public Class<?> getClazz() {
        return root.getColumnClass();
    }

    public Supplier<String> getAlias() {
        return root::getAlias;
    }
}