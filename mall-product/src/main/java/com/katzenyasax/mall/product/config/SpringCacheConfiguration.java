package com.katzenyasax.mall.product.config;


import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;


@Configuration
@EnableCaching
public class SpringCacheConfiguration {
    @Bean
    RedisCacheConfiguration redisCacheConfiguration(){
        RedisCacheConfiguration conf=RedisCacheConfiguration.defaultCacheConfig();
        //先弄一个默认配置，在其基础上修改
        conf = conf
                .entryTtl(Duration.ofMinutes(60))   //ttl
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))     //
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()))    //json格式
                //.prefixCacheNameWith("CACHE_")         //缓存名设置前缀
                //.disableKeyPrefix()                 //禁用前缀
                //.disableCachingNullValues()       //禁用空值缓存，可能导致缓存穿透
        ;
        //把格式固定成为了fastJson包下的Json格式，因为Json是标准格式，用哪个包下面的Json肯定都是标准的。
        return conf;
    }
}
