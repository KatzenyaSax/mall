package com.katzenyasax.mallseckill.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.katzenyasax.common.constant.SeckillConstant;
import com.katzenyasax.common.to.MemberTO;
import com.katzenyasax.common.to.SeckillSessionTO;
import com.katzenyasax.common.to.SeckillSkuRelationTO;
import com.katzenyasax.common.to.SeckillSubmitOrderTO;
import com.katzenyasax.common.utils.R;
import com.katzenyasax.mallseckill.feign.CouponFeign;
import com.katzenyasax.mallseckill.interceptor.SeckillUserInterceptor;
import com.katzenyasax.mallseckill.service.WebService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("webService")
public class WebServiceImpl implements WebService {

    @Autowired
    CouponFeign couponFeign;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    SeckillUserInterceptor seckillUserInterceptor;

    @Autowired
    RabbitTemplate rabbitTemplate;



    /**
     * 查询进三日所有的有秒杀的商品
     */
    public List<SeckillSkuRelationTO> getCurrentSeckillSku() {
        List<SeckillSessionTO> tos = couponFeign.selectCurrentSession();
        //最近的一次，可能是正在进行的场次，也有可能是马上开始的场次
        if (tos.isEmpty()){
            return new ArrayList<>();
        }
        SeckillSessionTO session=tos.get(0);
        //key
        String key = SeckillConstant.SECKILL_SKU_PREFIX + session.getId();
        //遍历单个场次
        List<SeckillSkuRelationTO> finale = session.getRelationTOList().stream().map(relation ->{
                //遍历该场次对应的skuRelation
            SeckillSkuRelationTO skuRelationTO = JSON.parseObject(
                    redisTemplate.boundHashOps(key).get(relation.getSkuId().toString()).toString()
                    , SeckillSkuRelationTO.class
            );
            skuRelationTO.setStartTime(session.getStartTime().getTime());
            skuRelationTO.setEndTime(session.getEndTime().getTime());
            return skuRelationTO;
        }).collect(Collectors.toList());
        return finale;
    }







    /**
     * 处理秒杀请求
     */
    @Override
    public R kill(String killId, String key, Long num) {
        //信息
        Long thisSessionId = Long.parseLong(killId.split("-")[0]);
        Long thisSkuId = Long.parseLong(killId.split("-")[1]);
        MemberTO thisMember = SeckillUserInterceptor.seckillThreadLocal.get();

        //从redis获取场次信息
        SeckillSessionTO thisSession = JSON.parseObject(
                redisTemplate.opsForValue().get(SeckillConstant.SECKILL_SESSION + thisSessionId).toString()
                , SeckillSessionTO.class
        );

        //从redis获取sku信息
        SeckillSkuRelationTO thisSkuRelation = JSON.parseObject(
                redisTemplate.boundHashOps(SeckillConstant.SECKILL_SKU_PREFIX + thisSessionId).get(thisSkuId.toString()).toString()
                , SeckillSkuRelationTO.class
        );

        /**
         * 验证是否在秒杀时间内
         */

        System.out.println("Seckill::WebService::kill: now: "+new Date()+" startTime: "+thisSession.getStartTime()+" endTime: "+thisSession.getEndTime());
        if(!(new Date().before(thisSession.getEndTime()) && new Date().after(thisSession.getStartTime()))){
            return R.ok().put("code",SeckillConstant.SECKILL_ERROR_OUT_OF_TIME);
        }

        /**
         * 验证商品数量是否合理
         */
        if(thisSkuRelation.getSeckillCount().compareTo(BigDecimal.valueOf(num)) == -1 ){
            return R.ok().put("code",SeckillConstant.SECKILL_ERROR_TOO_MUCH_BOUGHT);
        }

        /**
         * 验证该用户是否已经购买过该商品
         * 可以用setIfAbsent，通过占位一个  SeckillConstant.SECKILL_IF_USER_BOUGHT:用户id-场次id-skuId
         * 如果未占到，说明之前已经占位过，表示该用户已经购买过
         */
        if(!redisTemplate.opsForValue().setIfAbsent(
                SeckillConstant.SECKILL_IF_USER_BOUGHT+thisMember.getId()+"-"+thisSessionId+"-"+thisSkuId
                ,true
        )){
            return R.ok().put("code",SeckillConstant.SECKILL_ERROR_THIS_USER_HAS_BOUGHT_THIS_SKU);
        }

        /**
         * 验证信号量
         */
        Long nowSemaphore = Long.parseLong(redisTemplate.boundHashOps(SeckillConstant.SECKILL_SKU_SEMAPHORE + thisSessionId).get(thisSkuId.toString()).toString());
        if (nowSemaphore==0){
            //秒杀商品充足已被抢完
            return R.ok().put("code",SeckillConstant.SECKILL_ERROR_SEMAPHORE_OVER);
        }


        /**
         * 扣除信号量
         */
        nowSemaphore-=1;
        //存redis
        redisTemplate.boundHashOps(SeckillConstant.SECKILL_SKU_SEMAPHORE + thisSessionId).put(thisSkuId.toString(),nowSemaphore.toString());

        /**
         * 现在已确认该用户购买了该商品，接下来要发消息
         */
        //订单号
        String orderSn = IdWorker.getTimeId();
        //封装数据
        SeckillSubmitOrderTO seckillSubmitOrderTO=new SeckillSubmitOrderTO();
        seckillSubmitOrderTO.setOrderSn(orderSn);
        seckillSubmitOrderTO.setMemberId(thisMember.getId());
        seckillSubmitOrderTO.setPromotionSessionId(thisSessionId);
        seckillSubmitOrderTO.setSkuId(thisSkuId);
        seckillSubmitOrderTO.setNum(num);
        //发消息(已废除)
        /*try{
            rabbitTemplate.convertAndSend(
                    "order.exchange.seckill"
                    ,"order.key.seckill"
                    ,seckillSubmitOrderTO
            );
            System.out.println(
                    "Seckill::WebService 向交换机 order.exchange.seckill 以路由键 order.key.seckill 发送了消息："
                            + seckillSubmitOrderTO
            );
        } catch (Exception e){
            System.out.println("Seckill::WebService 向交换机 order.exchange.seckill 以路由键 order.key.seckill 发送消息时发送错误");
        }*/

        System.out.println("WebService::kill: seckillSubmitOrderTO= "+seckillSubmitOrderTO);
        return R.ok().put("code",SeckillConstant.SECKILL_SUCCESS).put("data",JSON.toJSONString(seckillSubmitOrderTO));
    }
}
