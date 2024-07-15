package com.github.yulichang.apt;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.function.Supplier;

@Data
@AllArgsConstructor
public class Column implements Serializable {

    private BaseColumn<?> root;

    private Class<?> clazz;

    private String property;

    private Supplier<String> alias;
}