package com.github.yulichang.extension.mapping.config;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.config.ConfigProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 映射查询配置
 *
 * @author yulichang
 * @since 1.4.5
 */
@Getter
@Accessors(chain = true)
public class DeepConfig<T> {

    private static final DeepConfig<?> defaultConfig = new DeepConfig<>();

    @Setter
    private List<SFunction<T, ?>> property;

    @Setter
    private boolean loop = false;

    @Setter
    private int deep = ConfigProperties.mappingMaxCount;

    @Setter
    private int maxDeep = ConfigProperties.mappingMaxCount;

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    @SuppressWarnings("unchecked")
    public static <T> DeepConfig<T> defaultConfig() {
        return (DeepConfig<T>) defaultConfig;
    }

    @SuppressWarnings("unused")
    public static class Builder<T> {

        private final DeepConfig<T> conf = new DeepConfig<>();

        @SafeVarargs
        public final Builder<T> property(SFunction<T, ?>... prop) {
            if (Objects.isNull(conf.property)) {
                conf.property = new ArrayList<>();
            }
            conf.property.addAll(Arrays.asList(prop));
            return this;
        }

        public Builder<T> loop(boolean loop) {
            conf.loop = loop;
            return this;
        }

        public Builder<T> deep(int deep) {
            Assert.isTrue(deep > 0, "查询深度必须大于0");
            conf.deep = deep;
            if (deep > conf.maxDeep) {
                conf.maxDeep = deep;
            }
            return this;
        }

        public Builder<T> maxDeep(int maxDeep) {
            conf.maxDeep = maxDeep;
            return this;
        }

        public DeepConfig<T> build() {
            return conf;
        }
    }
}
