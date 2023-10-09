package com.katzenyasax.mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableCaching  //开启缓存
@MapperScan(value = "com.katzenyasax.mall.product.dao")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.katzenyasax.mall.product.feign")
@SpringBootApplication
public class MallProduct {

    public static void main(String[] args) {
        SpringApplication.run(MallProduct.class, args);
    }

}
