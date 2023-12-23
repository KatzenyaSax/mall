package com.katzenyasax.mallseckill.feign;

import com.katzenyasax.common.to.SeckillSessionTO;
import com.katzenyasax.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("mall-coupon")
public interface CouponFeign {
    /**
     * 由seckill模块定时任务模块调用，上线秒杀场次
     */
    @RequestMapping("coupon/seckillsession/uploadSession")
    R uploadSession(@RequestParam List<Long> sessionIds);
    /**
     * 由seckill模块定时任务调用，查询三日后将开始的秒杀的场次的sessionId
     */
    @RequestMapping("coupon/seckillsession/selectInThreeDays")
    List<Long> selectInThreeDays();

    /**
     * 由seckill模块调用，返回现在的session
     */
    @GetMapping("coupon/seckillsession/selectCurrentSession")
    List<SeckillSessionTO> selectCurrentSession();

}
