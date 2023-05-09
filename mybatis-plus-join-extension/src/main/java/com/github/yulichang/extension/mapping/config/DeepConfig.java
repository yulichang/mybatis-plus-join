package com.github.yulichang.extension.mapping.config;

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
@Accessors(chain = true)
public class DeepConfig<T> {

    private static final DeepConfig<?> defaultConfig = new DeepConfig<>();

    @Getter
    @Setter
    private List<SFunction<T, ?>> prop;

    @Getter
    @Setter
    private boolean loop = false;

    @Getter
    @Setter
    private int maxCount = ConfigProperties.mappingMaxCount;

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    @SuppressWarnings("unchecked")
    public static <T> DeepConfig<T> defaultConfig() {
        return (DeepConfig<T>) defaultConfig;
    }

    public static class Builder<T> {

        private final DeepConfig<T> conf = new DeepConfig<>();

        @SafeVarargs
        public final Builder<T> prop(SFunction<T, ?>... prop) {
            if (Objects.isNull(conf.prop)) {
                conf.prop = new ArrayList<>();
            }
            conf.prop.addAll(Arrays.asList(prop));
            return this;
        }

        public Builder<T> loop(boolean loop) {
            conf.loop = loop;
            return this;
        }

        public Builder<T> maxCount(int maxCount) {
            conf.maxCount = maxCount;
            return this;
        }

        public DeepConfig<T> build() {
            return conf;
        }
    }
}
