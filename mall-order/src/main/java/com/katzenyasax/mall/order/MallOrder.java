package com.katzenyasax.mall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@MapperScan(value = "com.katzenyasax.mall.order.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class MallOrder {

    public static void main(String[] args) {
        SpringApplication.run(MallOrder.class, args);
    }

}
