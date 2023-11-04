package com.katzenyasax.mall.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebViewController implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/detail.html").setViewName("detail");
        registry.addViewController("/pay.html").setViewName("pay");
        registry.addViewController("/confirm.html").setViewName("confirm");
        registry.addViewController("/createForWxNative.html").setViewName("createForWxNative");
    }
}
