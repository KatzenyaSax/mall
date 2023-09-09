package com.katzenyasax.mall.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@MapperScan(value = "com.katzenyasax.mall.member.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class MallMember {

    public static void main(String[] args) {
        SpringApplication.run(MallMember.class, args);
    }

}
