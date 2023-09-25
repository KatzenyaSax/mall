package com.katzenyasax.mall.search.config;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ESClientConfiguration {
    @Bean
    public RestHighLevelClient EsClient(){
        RestHighLevelClient highLevelClient=new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.74.130",9200,"http")
                )
        );
        return  highLevelClient;
    }


    /**
     *  es官方建议的es通用设置
     */
    public static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        COMMON_OPTIONS=builder.build();
    }



}
