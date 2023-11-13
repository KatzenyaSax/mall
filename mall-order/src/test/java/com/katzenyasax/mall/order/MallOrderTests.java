package com.katzenyasax.mall.order;

import com.katzenyasax.mall.order.entity.OrderEntity;
import com.rabbitmq.client.AMQP;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MallOrderTests {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
    }




    @Test
    void rabbitTest01(){
        DirectExchange directExchange=new DirectExchange(
                "spring.test01.directExchange"      //名称
                ,true                       //是否持久化
                ,false          //是否自动删除
        );
        //声明一个交换机
        amqpAdmin.declareExchange(directExchange);
    }


    @Test
    void createQueue(){
        Queue queue=new Queue(
                "spring.test01.queue01"
                ,true                       //持久化
                ,false              //排他性
                ,false          //自动删除
        );
        //声明一个队列
        amqpAdmin.declareQueue(queue);
    }


    @Test
    void createBinding(){
        Binding binding=new Binding(
                /**
                 * String destination,          目的地
                 * DestinationType destinationType,     目的地类型
                 * String exchange,         交换机
                 * String routingKey,       路由键
                 * @Nullable Map<String, Object> arguments          自定义参数
                 */

                "spring.test01.queue01"
                , Binding.DestinationType.QUEUE
                ,"spring.test01.directExchange"
                ,"testRK"
                ,null
        );
        //声明一个绑定
        amqpAdmin.declareBinding(binding);
    }





    @Test
    void rabbitMessageSending(){
        OrderEntity message=new OrderEntity();
        message.setMemberId(1l);
        message.setMemberUsername("hdow");

        //发送消息
        rabbitTemplate.convertAndSend(
                "spring.test01.directExchange"
                ,"testRK"
                ,message);
    }



    @Test
    void StringMessageSending(){
        //发送String消息
        rabbitTemplate.convertAndSend(
                "spring.test01.directExchange"
                ,"testRK"
                ,"ttttest");
    }


}
