package com.github.yulichang.wrapper.segments;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yulichang
 * @since 1.5.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo implements Serializable {

    private IPage<?> innerPage;

    private String countSelectSql;

    public static PageInfoBuilder builder() {
        return new PageInfoBuilder();
    }


    @SuppressWarnings("unused")
    public static class PageInfoBuilder {
        private IPage<?> innerPage;
        private String countSelectSql;

        PageInfoBuilder() {
        }

        public PageInfoBuilder innerPage(IPage<?> innerPage) {
            this.innerPage = innerPage;
            return this;
        }

        public PageInfoBuilder countSelectSql(String countSelectSql) {
            this.countSelectSql = countSelectSql;
            return this;
        }

        public PageInfo build() {
            return new PageInfo(this.innerPage, this.countSelectSql);
        }
    }
}
