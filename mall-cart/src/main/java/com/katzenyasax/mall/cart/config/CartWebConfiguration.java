package com.katzenyasax.mall.cart.config;


import com.katzenyasax.mall.cart.interceptor.CartInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CartWebConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(new CartInterceptor())      //添加cart的拦截器
                .addPathPatterns("/**")                     //拦截url为/**，即所有url
                ;
    }
}
