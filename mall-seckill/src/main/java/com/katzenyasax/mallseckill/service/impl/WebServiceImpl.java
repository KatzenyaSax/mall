package com.katzenyasax.mallseckill.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.katzenyasax.common.constant.SeckillConstant;
import com.katzenyasax.common.to.SeckillSessionTO;
import com.katzenyasax.common.to.SeckillSkuRelationTO;
import com.katzenyasax.mallseckill.feign.CouponFeign;
import com.katzenyasax.mallseckill.interceptor.SeckillUserInterceptor;
import com.katzenyasax.mallseckill.service.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
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



    /**
     * 查询进三日所有的有秒杀的商品
     */
    public List<SeckillSkuRelationTO> getCurrentSeckillSku() {
        List<SeckillSessionTO> tos = couponFeign.selectCurrentSession();
        //最近的一次，可能是正在进行的场次，也有可能是马上开始的场次
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
        System.out.println(finale);
        return finale;
    }







    /**
     * 处理秒杀请求
     */
    @Override
    public String kill(String killId, String key, Long num) {
        //信息
        Long thisSessionId = Long.parseLong(killId.split("-")[0]);
        Long thisSkuId = Long.parseLong(killId.split("-")[1]);

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
        if(new Date().after(thisSession.getEndTime()) || new Date().before(thisSession.getStartTime())){
            return null;
        }

        /**
         * 验证商品数量是否合理
         */
        if(thisSkuRelation.getSeckillCount().compareTo(BigDecimal.valueOf(num)) == -1 ){
            return null;
        }

        /**
         * 验证该用户是否已经购买过该商品
         * 可以用setIfAbsent，通过占位一个  SeckillConstant.SECKILL_IF_USER_BOUGHT:用户id-场次id-skuId
         * 如果未占到，说明之前已经占位过，表示该用户已经购买过
         */
        if(!redisTemplate.opsForValue().setIfAbsent(
                SeckillConstant.SECKILL_IF_USER_BOUGHT+SeckillUserInterceptor.seckillThreadLocal.get().getId()+"-"+thisSessionId+"-"+thisSkuId
                ,true
        )){
            return null;
        }

        /**
         * 验证信号量，并扣除
         */
        Long nowSemaphore = Long.parseLong(redisTemplate.boundHashOps(SeckillConstant.SECKILL_SKU_SEMAPHORE + thisSessionId).get(thisSkuId.toString()).toString());

        if (nowSemaphore>0){
            //信号量大于0，表示秒杀商品充足
        }

        String orderSn = IdWorker.getTimeId();




        return null;
    }
}
