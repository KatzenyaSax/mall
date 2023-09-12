package com.katzenyasax.mall.gateway.conf;


//这是解决跨域的配置类

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class Mall_CorsConfiguration {
    @Bean
    public CorsWebFilter corsWebFilter(){
        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration=new CorsConfiguration();
        //1.配置跨域
        corsConfiguration.addAllowedHeader("*");            //允许跨域的请求头
        corsConfiguration.addAllowedMethod("*");            //允许跨域的请求方式
        corsConfiguration.addAllowedOriginPattern("*");            //允许跨域的
        corsConfiguration.setAllowCredentials(true);        //允许携带cookie跨域
        source.registerCorsConfiguration("/**",corsConfiguration);
        //表示在上述配置下，允许任意请求跨域
        return new CorsWebFilter(source);
    }
}
