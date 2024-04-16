package com.lgblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.swing.*;

@SpringBootApplication
@MapperScan("com.lgblog.dao")
@EnableScheduling
@EnableSwagger2
public class LGBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(LGBlogApplication.class,args);
    }
}
