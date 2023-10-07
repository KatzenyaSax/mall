package com.katzenyasax.mall.product.config;


import io.lettuce.core.RedisClient;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfiguration {

    /**
     *
     * @return
     *
     * redisson配置类
     *
     */
    @Bean(destroyMethod = "shutdown")//销毁方法为shutdown
    RedissonClient redissonClient(){
        //创建配置
        Config config=new Config();
        config.useSingleServer().setAddress("redis://192.168.74.130:6379");
        //根据配置实例化redisson的客户端
        RedissonClient redissonClient= Redisson.create(config);
        return redissonClient;
    }
}
