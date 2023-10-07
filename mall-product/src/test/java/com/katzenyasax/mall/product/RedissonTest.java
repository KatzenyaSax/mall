package com.katzenyasax.mall.product;


import com.katzenyasax.mall.product.config.RedissonConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedissonTest {
    @Autowired
    RedissonConfiguration redissonConfiguration;
    @Test
    void printRedisson(){
        System.out.println(redissonConfiguration);
    }
}
