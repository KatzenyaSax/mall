package com.katzenyasax.mall.order.config;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfirmConfiguration {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void setRabbitTemplate(){

        /**
         * 1.publisher到broker的确认
         */
        rabbitTemplate.setConfirmCallback(
                (correlationData, b, s) -> System.out.println("由publisher发往broker，Data: "+correlationData+" , 是否接收到: "+b+" 原因: "+s)
        );


        /**
         * 2.broker到queue的确认
         */
        rabbitTemplate.setReturnsCallback(returnedMessage ->
            System.out.println(
                    "由broker发往queue，发送失败的消息内容: "+returnedMessage.getMessage()
                    +" 回复的状态码: "+ returnedMessage.getReplyCode()
                    +" 回复的文本内容: "+ returnedMessage.getReplyText()
                    +" 发送该消息的交换机: "+returnedMessage.getExchange()
                    +" 路由键: "+returnedMessage.getRoutingKey()
            )
        );

    }
}
