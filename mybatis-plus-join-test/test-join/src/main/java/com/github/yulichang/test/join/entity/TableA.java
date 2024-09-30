package com.github.yulichang.test.join.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.github.yulichang.annotation.Table;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Table
@Data
@ToString
@Accessors(chain = true)
@TableName(value = "tablea", autoResultMap = true)
public class TableA {

    @TableId
    private Integer id;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Integer> mapCol;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Inner entryCol;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> listCol;

    @Data
    @ToString
    public static class Inner{
        private String name;
    }
}
