package com.katzenyasax.mallseckill.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class SessionConfiguration {


    /**
     *
     * @return
     *
     * 自定义session
     *
     */
    @Bean
    public CookieSerializer cookieSerializer(){
        DefaultCookieSerializer cookieSerializer=new DefaultCookieSerializer();
        cookieSerializer.setDomainName("katzenyasax-mall.com");     //设置作用域为父域
        //cookieSerializer.setCookieName("katzenyasax-mall::session");        //session的名字

        return cookieSerializer;
    }

    /**
     *
     * @return
     *
     * 将redis的序列化机制改成json（用到Jackson的序列化器）
     *
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer(){
        return new GenericJackson2JsonRedisSerializer();
    }

}
