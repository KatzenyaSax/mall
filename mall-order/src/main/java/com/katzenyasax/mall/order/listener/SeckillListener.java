package com.katzenyasax.mall.order.listener;


import com.katzenyasax.common.to.SeckillSubmitOrderTO;
import com.katzenyasax.mall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//@RabbitListener(queues = "order.queue.seckill")
public class SeckillListener {

    @Autowired
    OrderService orderService;

    /**
     * 监听order.queue.seckill队列，拿取SeckillSubmitOrderTO类型的对象实例
     */
    @RabbitHandler
    public void listenSeckill(Message message, SeckillSubmitOrderTO to, Channel channel){
        System.out.println("Order::SeckillListener: 从队列 order.queue.seckill 收到消息: "+to);
        try{
            orderService.seckillConfirmOrder(to);
            channel.basicAck(
                    message.getMessageProperties().getDeliveryTag()     //消息的tag
                    , false                                         //是否批量确认
            );
        }catch (Exception e){
            System.out.println("Order::SeckillListener: 处理秒杀订单失败");
        }
    }

}
