package com.katzenyasax.mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@MapperScan(value = "com.katzenyasax.mall.product.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class MallProduct {

    public static void main(String[] args) {
        SpringApplication.run(MallProduct.class, args);
    }

}
