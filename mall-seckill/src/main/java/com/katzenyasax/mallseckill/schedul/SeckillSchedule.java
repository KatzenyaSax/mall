package com.katzenyasax.mallseckill.schedul;


import com.katzenyasax.mallseckill.feign.CouponFeign;
import com.katzenyasax.mallseckill.service.impl.WebServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableAsync
@EnableScheduling
@Component
/**
 * 定时任务
 */
public class SeckillSchedule {

    @Autowired
    WebServiceImpl webServiceImpl;

    @Autowired
    CouponFeign couponFeign;

    /**
     * 动态查询最近三天要上架的商品，将其上线，随后等待其自动生效
     */
    @Scheduled(cron = "0 0 3 * * ? ")
    @Test
    public void uploadSeckill(){
        couponFeign.uploadSession(
                couponFeign.selectInThreeDays()
        );
    }
}
