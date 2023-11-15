package com.katzenyasax.mall.ware.config;

import com.katzenyasax.common.to.WareOrderDetailTO;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * mq的配置类
 */
@Configuration
public class MqConfiguration {
    /**
     * 把rabbitTemplate中的默认序列化转换器替换为json的序列化
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }


    //===================================================






    //===================================================

    /**
     * 创建交换机stock.exchange.top
     */
    @Bean
    public Exchange stockExchangeTop(){
        //public TopicExchange(String name, boolean durable, boolean autoDelete, Map<String, Object> arguments)
        return new TopicExchange(
                "stock.exchange.top"           //交换机名
                ,true                           //持久化
                ,false                          //自动删除
        );
    }

    /**
     * 创建死信队列stock.queue.delay
     */
    @Bean
    public Queue stockQueueDelay(){
        //自定义参数，有关死信的全部参数
        Map<String,Object> arguments=new HashMap<>();
        arguments.put("x-dead-letter-exchange","stock.exchange.top");              //死信队列从哪个交换机拿死信
        arguments.put("x-dead-letter-routing-key","stock.key.unlock.unpay");               //死信队列拿到死信后，定时发送到交换机时的路由键
        arguments.put("x-message-ttl", 1000*15);                                        //死信定时时间，50min

        // public Queue(String name, boolean durable, boolean exclusive, boolean autoDelete, @Nullable Map<String, Object> arguments)
        return new Queue(
                "stock.queue.delay"          //交换机名
                ,true                        //持久化
                ,false                       //排他
                ,false                       //自动删除
                ,arguments                   //自定义参数
        );
    }

    /**
     * 创建ware订阅的队列stock.queue.unlock
     */
    @Bean
    public Queue stockQueueUnlock(){
        // public Queue(String name, boolean durable, boolean exclusive, boolean autoDelete, @Nullable Map<String, Object> arguments)
        return new Queue(
                "stock.queue.unlock"
                ,true
                ,false
                ,false
        );
    }


    /**
     * 创建死信delay队列和交换机的绑定
     */
    @Bean
    public Binding delayExchangeBinding(){
        return new Binding(
                "stock.queue.delay"                             //目的地是死信队列
                , Binding.DestinationType.QUEUE                 //死信队列是队列（QUEUE）
                ,"stock.exchange.top"                  //中转exchange
                ,"stock.key.locked"                //路由键，该键表明消息死亡时以该键送往死信队列
                ,null                       //自定义参数（可以不填）
        );
    }

    /**
     * 创建交换机和unlock队列的绑定
     */
    @Bean
    public Binding unlockExchangeBinding(){
        return new Binding(
                "stock.queue.unlock"                             //目的地是库存队列
                , Binding.DestinationType.QUEUE                 //库存队列是队列（QUEUE）
                ,"stock.exchange.top"                  //中转exchange
                ,"stock.key.unlock.#"                //路由键
                ,null                       //自定义参数（可以不填）
        );
    }

}
