package com.katzenyasax.mall.product.config;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement

/**
 *      开启事务
 *
 *
 * */

@MapperScan(value = "com.katzenyasax.mall.product.dao")
public class MybatisPlusConfiguration {


    /*@Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor paginationInterceptor=new PaginationInterceptor();

        paginationInterceptor.setOverflow(true);
        //翻页超过最后一页后，回到第一页
        paginationInterceptor.setLimit(2000);
        //设置每页数据最大量，当前为2000条
        return paginationInterceptor;
    }*/


    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
        return interceptor;
    }





}
