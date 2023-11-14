package com.katzenyasax.mall.member.config;

import com.katzenyasax.mall.member.interceptor.MemberInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class MemberWebConfiguration implements WebMvcConfigurer {
    @Autowired
    MemberInterceptor memberInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(memberInterceptor)      //添加cart的拦截器
                .addPathPatterns("/**")                     //拦截url为/**，即所有url
        ;
    }
}
