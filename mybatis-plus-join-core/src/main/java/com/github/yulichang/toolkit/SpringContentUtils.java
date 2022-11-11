package com.github.yulichang.toolkit;


/**
 * spring容器工具类
 *
 * @author yulichang
 * @since 1.2.0
 */
public class SpringContentUtils {

    private static SpringContext springContext;

    public SpringContentUtils(SpringContext springContext) {
        SpringContentUtils.springContext = springContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return SpringContentUtils.springContext.getBean(clazz);
    }

    public interface SpringContext {

        <T> T getBean(Class<T> clazz);
    }
}
