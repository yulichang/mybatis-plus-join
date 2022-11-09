package com.github.yulichang.test;

import com.github.yulichang.test.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 测试
 */
@SpringBootTest
@SuppressWarnings("unused")
class MPJ131Test {
    @Resource
    private UserMapper userMapper;

    /**
     * 先明确 你要干啥？
     * 在mapper中定义lambda形式的resultMap？
     * 要解决问题
     * 1、只初始化了部分数据库实体类？ NPE
     * <p>
     * stater
     * core
     * anntion
     * test
     * <p>
     * 超高自由度的sql拼接器？
     * 解决了自连接和子查询和各种函数
     * 如果底层调用MPJLambdaWrapper那样还有什么意义
     */
    @Test
    void testJoin() {
        System.out.println(1);
    }
}
