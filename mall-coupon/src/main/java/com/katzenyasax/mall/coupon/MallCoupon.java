package com.katzenyasax.mall.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@MapperScan(value = "com.katzenyasax.mall.coupon.dao")
@EnableDiscoveryClient

@SpringBootApplication
public class MallCoupon {

    public static void main(String[] args) {
        SpringApplication.run(MallCoupon.class, args);
    }

}
