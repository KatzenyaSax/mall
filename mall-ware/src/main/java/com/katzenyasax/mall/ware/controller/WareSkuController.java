package com.katzenyasax.mall.ware.controller;

import java.util.Arrays;
import java.util.Map;

import io.swagger.models.auth.In;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.katzenyasax.mall.ware.entity.WareSkuEntity;
import com.katzenyasax.mall.ware.service.WareSkuService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.R;



/**
 * 商品库存
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:17:29
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;



    /**
     * 列表
     * params中包含wareId和skuId
     * 要求根据这俩查询sku
     */
    @RequestMapping("/list")
    @RequiresPermissions("ware:waresku:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.getSkuInfo(params);

        return R.ok().put("page", page);
    }







    /**
     * 响应product模块的方法
     * skuId，获取skuId对应的stock
     * 返回值为一个map，包含该skuId对应的商品所处的不同仓库的id和各仓库内的库存
     */
    @RequestMapping("/stock/{skuId}")
    Map<Long,Integer> getStockBySkuId(@PathVariable Long skuId){
        Map<Long, Integer> finale=wareSkuService.getStockBySkuxId(skuId);
        return finale;
    }








    //==============================================
   /* *//**
     * 列表
     *//*
    @RequestMapping("/list")
    @RequiresPermissions("ware:waresku:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }*/


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ware:waresku:info")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ware:waresku:save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ware:waresku:update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ware:waresku:delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
