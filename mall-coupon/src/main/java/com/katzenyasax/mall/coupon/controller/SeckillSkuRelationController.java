package com.katzenyasax.mall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.katzenyasax.mall.coupon.entity.SeckillSkuRelationEntity;
import com.katzenyasax.mall.coupon.service.SeckillSkuRelationService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.R;



/**
 * 秒杀活动商品关联
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 08:49:54
 */
@RestController
@RequestMapping("coupon/seckillskurelation")
public class SeckillSkuRelationController {
    @Autowired
    private SeckillSkuRelationService seckillSkuRelationService;


    /**
     * 查秒杀场次的关联商品
     */
    @RequestMapping("/list")
    public R listSeckillSku(@RequestParam Map<String, Object> params){
        PageUtils page = seckillSkuRelationService.listSeckillSku(params);
        System.out.println("SeckillSkuRelationController::listSeckillSku: page"+page.getList());
        return R.ok().put("page", page);
    }

    /**
     * 保存关联商品
     */
    @RequestMapping("/save")
    public R saveSeckillSku(@RequestBody SeckillSkuRelationEntity seckillSkuRelation){
        seckillSkuRelationService.saveSeckillSku(seckillSkuRelation);
        return R.ok();
    }





    //=========================================


    /**
     * 列表
     */
    //@RequestMapping("/list")
    @RequiresPermissions("coupon:seckillskurelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = seckillSkuRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("coupon:seckillskurelation:info")
    public R info(@PathVariable("id") Long id){
		SeckillSkuRelationEntity seckillSkuRelation = seckillSkuRelationService.getById(id);

        return R.ok().put("seckillSkuRelation", seckillSkuRelation);
    }

    /**
     * 保存
     */
    //@RequestMapping("/save")
    @RequiresPermissions("coupon:seckillskurelation:save")
    public R save(@RequestBody SeckillSkuRelationEntity seckillSkuRelation){
		seckillSkuRelationService.save(seckillSkuRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("coupon:seckillskurelation:update")
    public R update(@RequestBody SeckillSkuRelationEntity seckillSkuRelation){
		seckillSkuRelationService.updateById(seckillSkuRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("coupon:seckillskurelation:delete")
    public R delete(@RequestBody Long[] ids){
		seckillSkuRelationService.removeSessionSkuRelation(Arrays.asList(ids));



        return R.ok();
    }

}
