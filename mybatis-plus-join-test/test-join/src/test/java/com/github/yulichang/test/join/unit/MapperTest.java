package com.github.yulichang.test.join.unit;

import com.github.yulichang.test.join.entity.AddressDO;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.toolkit.JoinWrappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MapperTest {


    @BeforeEach
    void setUp() {
        Reset.reset();
    }


    @Test
    void mapper() {
        JoinWrappers.lambda(AddressDO.class).list();
    }

}
