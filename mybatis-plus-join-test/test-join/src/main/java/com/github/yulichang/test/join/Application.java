package com.github.yulichang.test.join;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.github.yulichang.test")
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);
        ISqlInjector bean = run.getBean(ISqlInjector.class);
        System.out.println(bean);
    }

}
