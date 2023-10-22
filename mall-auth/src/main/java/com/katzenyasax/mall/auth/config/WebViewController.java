package com.katzenyasax.mall.auth.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebViewController implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry){

        /**
         * 淘汰了，目前login.html被作为接口了
         */
        //registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/reg.html").setViewName("reg");
    }
}
