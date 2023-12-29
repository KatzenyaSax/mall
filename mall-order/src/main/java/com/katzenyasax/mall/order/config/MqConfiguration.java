package com.katzenyasax.mall.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MqConfiguration {

//====================== 创建mq组件 =======================================================================================

    /**
     * 创建中转交换机order.exchange.top，用于order消息的中转
     */
    @Bean
    public Exchange userExchangeOrder(){
        //public TopicExchange(String name, boolean durable, boolean autoDelete, Map<String, Object> arguments)
        return new TopicExchange(
                "order.exchange.top"           //交换机名
                ,true                           //持久化
                ,false                          //自动删除
        );
    }
    /**
     * 创建死信队列order.queue.delay，用于死消息的中转
     */
    @Bean
    public Queue delayQueueOrder(){
        //自定义参数，有关死信的全部参数
        Map<String,Object> arguments=new HashMap<>();
        arguments.put("x-dead-letter-exchange","order.exchange.top");              //死信队列从哪个交换机拿死信
        arguments.put("x-dead-letter-routing-key","order.key.post");               //死信队列拿到死信后，定时发送到交换机时的路由键
        arguments.put("x-message-ttl",1000*10);                                        //死信定时时间，30min

        // public Queue(String name, boolean durable, boolean exclusive, boolean autoDelete, @Nullable Map<String, Object> arguments)
        return new Queue(
                "order.queue.delay"          //交换机名
                ,true                        //持久化
                ,false                       //排他
                ,false                       //自动删除
                ,arguments                   //自定义参数
        );
    }
    /**
     * 创建订单队列order.queue.post，用于order消息的发送
     */
    @Bean
    public Queue userQueueOrder(){
        // public Queue(String name, boolean durable, boolean exclusive, boolean autoDelete, @Nullable Map<String, Object> arguments)
        return new Queue(
                "order.queue.post"
                ,true
                ,false
                ,false
        );
    }
    /**
     * 创建exchange和死信queue绑定
     */
    @Bean
    public Binding delayBinding(){
        return new Binding(
                "order.queue.delay"                             //目的地是死信队列
                , Binding.DestinationType.QUEUE                 //死信队列是队列（QUEUE）
                ,"order.exchange.top"                  //中转exchange
                ,"order.key.created"                //路由键
                ,null                       //自定义参数（可以不填）
        );
    }
    /**
     * 创建exchange和消息queue的绑定
     */
    @Bean
    public Binding orderBinding(){
        return new Binding(
                "order.queue.post"                             //目的地是消息队列
                , Binding.DestinationType.QUEUE                 //消息队列是队列（QUEUE）
                ,"order.exchange.top"                  //中转exchange
                ,"order.key.post"                //路由键
                ,null                       //自定义参数（可以不填）
        );
    }
}
