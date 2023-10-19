package com.katzenyasax.mall.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@EnableDiscoveryClient
@ConfigurationPropertiesScan
@EnableFeignClients(value = "com.katzenyasax.mall.auth.feign")
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class MallAuth {

	public static void main(String[] args) {
		SpringApplication.run(MallAuth.class, args);
	}

}
