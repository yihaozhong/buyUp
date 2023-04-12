package com.skillup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// 基本的分层：防腐层（SystemController负责接入接出）是干脏活累活的，domain（封装业务）、数据库、和前端分别都是纯粹的一层，infrastructure向上依赖domain向下依赖数据库
@SpringBootApplication
public class SkillUpApplication {
    public static void main(String[] args) {

        SpringApplication.run(SkillUpApplication.class, args);
        System.out.println("I am Yihao Zhong");
    }
}
