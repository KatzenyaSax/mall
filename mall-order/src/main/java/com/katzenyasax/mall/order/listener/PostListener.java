package com.katzenyasax.mall.order.listener;

import com.katzenyasax.mall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RabbitListener(queues = "order.queue.post")
public class PostListener {

    @Autowired
    OrderService orderService;


    /**
     * 监听order.queue.post队列，拿取order信息
     */
    @RabbitHandler
    public void listenerOrder(Message message, Long order, Channel channel){
        System.out.println("收到订单"+message.getMessageProperties().getDeliveryTag()+":"+order);
        System.out.println("将查看是否已处理，若未处理则按照过期处理......");
        try {
            //处理订单当前状态
            orderService.dealWithOrderStatus(order);
            channel.basicAck(
                    message.getMessageProperties().getDeliveryTag()     //消息的tag
                    , false                                         //是否批量确认
            );
        } catch (Exception e){
            System.out.println("处理订单失败");
        }
    }
}
