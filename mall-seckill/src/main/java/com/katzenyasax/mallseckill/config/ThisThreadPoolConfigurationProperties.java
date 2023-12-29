package com.katzenyasax.mallseckill.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "mall.thread")
@Component
@Data
public class ThisThreadPoolConfigurationProperties {
    private Integer coreSize;   //核心线程数
    private Integer maxSize;    //最大线程数
    private Integer keepAliveTime;      //存活时间
}
