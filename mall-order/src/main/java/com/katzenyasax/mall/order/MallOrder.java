package com.katzenyasax.mall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients(basePackages = "com.katzenyasax.mall.order.feign")
@EnableRabbit
@MapperScan(value = "com.katzenyasax.mall.order.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class MallOrder {

    public static void main(String[] args) {
        SpringApplication.run(MallOrder.class, args);
    }

}
