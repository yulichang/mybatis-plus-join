package com.github.yulichang.wrapper.interfaces;

import com.github.yulichang.wrapper.JoinQueryWrapper;

/**
 * @param <Children> wrapper
 * @author yulichang
 * @since 1.5.2
 */
public interface IExt<Children extends JoinQueryWrapper<?>> {

    Children getChildren();
}

