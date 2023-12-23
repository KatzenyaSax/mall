package com.katzenyasax.mallseckill.config;

import com.katzenyasax.mallseckill.interceptor.SeckillUserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class SeckillWebConfiguration implements WebMvcConfigurer {
    @Autowired
    SeckillUserInterceptor seckillUserInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(seckillUserInterceptor)      //添加cart的拦截器
                .addPathPatterns("/**")                     //拦截url为/**，即所有url
        ;
    }
}
