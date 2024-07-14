package com.github.yulichang.apt;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.function.Supplier;

@Data
@AllArgsConstructor
public class Column {

    private BaseColumn<?> root;

    private Class<?> clazz;

    private String property;

    private Supplier<String> alias;
}