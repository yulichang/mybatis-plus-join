package com.github.yulichang.extension.apt.matedata;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yulichang
 * @since 1.5.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseColumn<T> implements Serializable {

    protected String alias;

    abstract public Class<T> getColumnClass();
}