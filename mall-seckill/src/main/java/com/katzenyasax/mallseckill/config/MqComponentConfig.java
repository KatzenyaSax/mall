package com.katzenyasax.mallseckill.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqComponentConfig {

    /**
     * 创建交换机order.exchange.seckill
     */
    @Bean
    public Exchange seckillExchangeOrder(){
        //public TopicExchange(String name, boolean durable, boolean autoDelete, Map<String, Object> arguments)
        return new TopicExchange(
                "order.exchange.seckill"           //交换机名
                ,true                           //持久化
                ,false                          //自动删除
        );
    }


    /**
     * 创建队列order.queue.seckill
     */
    /**
     * 创建订单队列order.queue.post，用于order消息的发送
     */
    @Bean
    public Queue seckillQueueOrder(){
        // public Queue(String name, boolean durable, boolean exclusive, boolean autoDelete, @Nullable Map<String, Object> arguments)
        return new Queue(
                "order.queue.seckill"
                ,true
                ,false
                ,false
        );
    }



    /**
     * 创建交换机和队列的绑定关系
     */
    /**
     * 创建exchange和消息queue的绑定
     */
    @Bean
    public Binding seckillBinding(){
        return new Binding(
                "order.queue.seckill"                             //目的地是消息队列
                , Binding.DestinationType.QUEUE                 //目的地的类型是队列（QUEUE）
                ,"order.exchange.seckill"                  //中转交换机
                ,"order.key.seckill"                //路由键
                ,null                       //自定义参数（可以不填）
        );
    }

}
