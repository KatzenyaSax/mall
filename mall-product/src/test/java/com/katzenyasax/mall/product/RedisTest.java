package com.katzenyasax.mall.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.UUID;

@SpringBootTest
public class RedisTest {


    /**
     * redis连接器
     */
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Test
    public void RedisTest01(){
        //让连接器创建一个操作杠杆，用于直接操作redis
        ValueOperations<String,String> ops=stringRedisTemplate.opsForValue();
        //存入一个数据
        ops.set("Hello","Redis!"+ UUID.randomUUID());
        //查询，并输出
        System.out.println(ops.get("Hello"));
    }











}
