package com.github.yulichang.wrapper.segments;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author yulichang
 * @since 1.5.0
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo implements Serializable {

    private IPage<?> innerPage;

    private String countSelectSql;
}
