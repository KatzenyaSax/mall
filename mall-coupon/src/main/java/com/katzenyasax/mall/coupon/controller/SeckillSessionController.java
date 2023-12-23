package com.katzenyasax.mall.coupon.controller;

import com.katzenyasax.common.to.SeckillSessionTO;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.coupon.entity.SeckillSessionEntity;
import com.katzenyasax.mall.coupon.service.SeckillSessionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 秒杀活动场次
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 08:49:54
 */
@RestController
@RequestMapping("coupon/seckillsession")
public class SeckillSessionController {
    @Autowired
    private SeckillSessionService seckillSessionService;

    @Autowired
    RedisTemplate redisTemplate;


    /**
     * 保存场次信息，保存至redis
     */
    @RequestMapping("/save")
    public R saveSession(@RequestBody SeckillSessionEntity seckillSession){
        return seckillSessionService.saveSession(seckillSession);
    }


    /**
     * 由seckill模块定时任务模块调用，上线秒杀场次
     */
    @RequestMapping("/uploadSession")
    public R uploadSession(@RequestParam List<Long> sessionIds){
        return seckillSessionService.uploadSession(sessionIds);
    }


    /**
     * 由seckill模块定时任务调用，查询三日后将开始的秒杀的场次的sessionId
     */
    @RequestMapping("/selectInThreeDays")
    public List<Long> selectInThreeDays(){
        return seckillSessionService.selectInThreeDays();
    }

    /**
     * 由seckill模块调用，返回现在的session
     *
     * 由product模块调用，返回现在或最近的一个session
     */
    @RequestMapping("/selectCurrentSession")
    public List<SeckillSessionTO> selectSessionEntityInThreeDays(){
        return seckillSessionService.selectCurrentSession();
    }

    //=============================================================


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("coupon:seckillsession:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = seckillSessionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("coupon:seckillsession:info")
    public R info(@PathVariable("id") Long id){
		SeckillSessionEntity seckillSession = seckillSessionService.getById(id);

        return R.ok().put("seckillSession", seckillSession);
    }

    /**
     * 保存
     */
    //@RequestMapping("/save")
    @RequiresPermissions("coupon:seckillsession:save")
    public R save(@RequestBody SeckillSessionEntity seckillSession){
		seckillSessionService.save(seckillSession);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("coupon:seckillsession:update")
    public R update(@RequestBody SeckillSessionEntity seckillSession){
		seckillSessionService.updateById(seckillSession);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("coupon:seckillsession:delete")
    public R delete(@RequestBody Long[] ids){
		seckillSessionService.removeSessions(Arrays.asList(ids));

        return R.ok();
    }

}
