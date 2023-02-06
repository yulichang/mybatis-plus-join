package com.github.yulichang.test.kotlin;

import com.github.yulichang.test.kotlin.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KotlinTest {

    @Autowired
    UserService userService;

    @Test
    void testGet(){
        userService.get("user1");
    }

    @Test
    void testGetFromExFunCallsJavaMapper(){
        userService.getFromFuncVar("user1");
    }
}
