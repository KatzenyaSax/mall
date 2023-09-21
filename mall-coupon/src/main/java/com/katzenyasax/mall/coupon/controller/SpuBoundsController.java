package com.katzenyasax.mall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import com.katzenyasax.common.to.SpuBoundsTO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.katzenyasax.mall.coupon.entity.SpuBoundsEntity;
import com.katzenyasax.mall.coupon.service.SpuBoundsService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.R;



/**
 * 商品spu积分设置
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 08:49:54
 */
@RestController
@RequestMapping("coupon/spubounds")
public class SpuBoundsController {
    @Autowired
    private SpuBoundsService spuBoundsService;





    /**
     * 保存
     *
     * product远程调用coupon时调用的方法
     * 接收的是一个和SpuBoundsEntity一致的to
     * 需要将其数据进行处理，并保存到spu bounds
     *
     */
    @PostMapping("/save")
    public R saveBounds(@RequestBody SpuBoundsTO to){
        spuBoundsService.saveBoundsTO(to);
        return R.ok();
    }













    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("coupon:spubounds:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuBoundsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("coupon:spubounds:info")
    public R info(@PathVariable("id") Long id){
		SpuBoundsEntity spuBounds = spuBoundsService.getById(id);

        return R.ok().put("spuBounds", spuBounds);
    }

   /* *//**
     * 保存
     *//*
    @RequestMapping("/save")
    @RequiresPermissions("coupon:spubounds:save")
    public R save(@RequestBody SpuBoundsEntity spuBounds){
		spuBoundsService.save(spuBounds);

        return R.ok();
    }*/

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("coupon:spubounds:update")
    public R update(@RequestBody SpuBoundsEntity spuBounds){
		spuBoundsService.updateById(spuBounds);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("coupon:spubounds:delete")
    public R delete(@RequestBody Long[] ids){
		spuBoundsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
