package com.katzenyasax.mallseckill;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@EnableFeignClients
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableRabbit
@EnableDiscoveryClient
@EnableRedisHttpSession
public class MallSeckill {

    public static void main(String[] args) {
        SpringApplication.run(MallSeckill.class, args);
    }

}
