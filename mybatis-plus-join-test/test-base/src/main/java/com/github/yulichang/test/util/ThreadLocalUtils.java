package com.github.yulichang.test.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;

public class ThreadLocalUtils {

    private final static ThreadLocal<String> userThreadLocal = new ThreadLocal<>();

    /**
     * 设置数据到当前线程
     */
    public static void set(String... sql) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            userThreadLocal.set(mapper.writeValueAsString(Arrays.asList(sql)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取线程中的数据
     */
    @SneakyThrows
    public static List<String> get() {
        String s = userThreadLocal.get();
        if (StringUtils.isBlank(s)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        List<String> sqlList = mapper.readValue(s, new TypeReference<List<String>>() {
        });
        sqlList.removeIf(StringUtils::isBlank);
        set("");
        return sqlList;
    }
}
