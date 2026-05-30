package com.github.yulichang.autoconfigure.conditional;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.MethodMetadata;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * @author yulichang
 * @since 1.4.5
 */
public class OnSqlInjectorCondition implements Condition {

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        if (Objects.nonNull(conditionContext.getBeanFactory())) {
            String[] names = conditionContext.getBeanFactory().getBeanNamesForType(ISqlInjector.class);
            for (String name : names) {
                BeanDefinition definition = conditionContext.getBeanFactory().getBeanDefinition(name);
                if (definition instanceof AnnotatedBeanDefinition) {
                    AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) definition;
                    MethodMetadata metadata = annotatedBeanDefinition.getFactoryMethodMetadata();
                    if (Objects.nonNull(metadata)) {
                        MergedAnnotations annotations = metadata.getAnnotations();
                        for (MergedAnnotation<Annotation> it : annotations) {
                            if (Objects.equals(it.getType(), Primary.class)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
