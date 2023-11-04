package com.katzenyasax.mall.order.config;

import com.katzenyasax.mall.order.interceptor.OrderInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class OrderWebConfiguration implements WebMvcConfigurer {
    @Autowired
    OrderInterceptor orderInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(orderInterceptor)      //添加cart的拦截器
                .addPathPatterns("/**")                     //拦截url为/**，即所有url
        ;
    }
}
