package com.lgblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gyd
 * @date 2023/12/21 0002 21:02
 */
@SpringBootApplication //把这个类标识为引导类(也叫启动类)
@MapperScan("com.lgblog.dao") //mybatis的配置
public class BlogAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogAdminApplication.class, args);
    }
}