package com.github.yulichang.toolkit;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.ibatis.session.SqlSession;

import java.util.Objects;
import java.util.Optional;

/**
 * spring容器工具类
 *
 * @author yulichang
 * @since 1.2.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpringContentUtils {

    private static SpringContext springContext;

    public static void setSpringContext(SpringContext springContext) {
        if (Objects.isNull(SpringContentUtils.springContext)) {
            SpringContentUtils.springContext = springContext;
        }
    }

    public static <T> T getBean(Class<T> clazz) {
        return Optional.ofNullable(springContext).map(c -> c.getBean(clazz)).orElse(null);
    }

    public static <T> void getBeansOfType(Class<T> clazz) {
        Optional.ofNullable(springContext).ifPresent(c -> c.getBeansOfType(clazz));
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    public static <T> T getMapper(Class<?> clazz) {
        if (Objects.isNull(springContext)) {
            SqlSession session = SqlHelper.sqlSession(clazz);
            Assert.notNull(session, "mapper not find by class <%s>", clazz.getSimpleName());
            BaseMapper<?> mapper = SqlHelper.getMapper(clazz, session);
            Assert.notNull(mapper, "mapper not find by class <%s>", clazz.getSimpleName());
            return (T) mapper;
        }
        Class<?> mapper = MPJTableMapperHelper.getMapper(clazz);
        Assert.notNull(mapper, "mapper not find by class <%s>", clazz.getSimpleName());
        Object bean = getBean(mapper);
        Assert.notNull(bean, "mapper not find by class <%s>", clazz.getSimpleName());
        return (T) bean;
    }

    public interface SpringContext {

        <T> T getBean(Class<T> clazz);

        <T> void getBeansOfType(Class<T> clazz);
    }
}
