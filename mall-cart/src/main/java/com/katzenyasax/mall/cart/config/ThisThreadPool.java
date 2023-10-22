package com.katzenyasax.mall.cart.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThisThreadPool {

    @Autowired
    ThisThreadPoolConfigurationProperties threadConfiguration;

    @Bean
    public ThreadPoolExecutor TPE(){
        ThreadPoolExecutor tpe;
        tpe = new ThreadPoolExecutor(
                threadConfiguration.getCoreSize(),                                          //核心线程数
                threadConfiguration.getMaxSize(),                                          //最大线程数
                threadConfiguration.getKeepAliveTime(),                                         //空闲线程最大存活时间
                TimeUnit.SECONDS,                           //时间单位（秒）
                new ArrayBlockingQueue<>(10000),                //建立阻塞队列，允许同时运行线程的数量
                Executors.defaultThreadFactory(),           //默认的线程工厂
                new ThreadPoolExecutor.AbortPolicy()        //拒绝服务策略(Abort)，队列满时处理剩余线程的策略
        );
        return tpe;
    }

}
