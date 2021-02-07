package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yulichang
 */
public class StreamUtils {

    public static <T, R> List<R> toList(List<T> list, Function<T, R> func) {
        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream().map(func).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static <T, R> Set<R> toSet(List<T> list, Function<T, R> func) {
        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream().map(func).collect(Collectors.toSet());
        }
        return new HashSet<>();
    }
}
