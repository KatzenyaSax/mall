package com.katzenyasax.mall.order.service.impl;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.order.dao.OrderDao;
import com.katzenyasax.mall.order.entity.OrderEntity;
import com.katzenyasax.mall.order.service.OrderService;

@RabbitListener(queues = {"spring.test01.queue01"})
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 监听OrderEntity对象消息
     *
     * 加上了手动确认ack的
     */
    @RabbitHandler
    //@RabbitListener(queues = {"spring.test01.queue01"})
    public void listenOrderMessage(Message message, OrderEntity body, Channel channel) {
        System.out.println("接收到OrderEntity对象消息："+body);
        try {
            channel.basicAck(
                    message.getMessageProperties().getDeliveryTag()     //消息的tag
                    , false                                         //是否批量确认
            );

            /*
             * 拒绝确认消息
             * channel.basicNack(
             *             message.getMessageProperties().getDeliveryTag()     //消息的tag
             *             , false                                         //是否批量确认
             *             , true                                          //退回队列，或是删除消息
             *     );
             */

        }catch (Exception e){
            System.out.println(message.getMessageProperties().getDeliveryTag()+"号消息异常");
        }
    }

    /**
     * 接收String对象消息
     */
    @RabbitHandler
    public void listenOrderMessage(String message,Channel channel){
        System.out.println("接受到String对象消息："+message);
    }

}