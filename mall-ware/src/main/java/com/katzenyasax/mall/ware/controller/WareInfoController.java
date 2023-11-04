package com.katzenyasax.mall.ware.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import com.katzenyasax.mall.ware.vo.FareVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.katzenyasax.mall.ware.entity.WareInfoEntity;
import com.katzenyasax.mall.ware.service.WareInfoService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.R;



/**
 * 仓库信息
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:17:29
 */
@RestController
@RequestMapping("ware/wareinfo")
public class WareInfoController {
    @Autowired
    private WareInfoService wareInfoService;


    /**
     * 前端请求，运费
     *
     * //todo 先直接返回8
     */
    @RequestMapping("/fare")
    public R getFare(@RequestParam Long addrId){
        FareVo data=wareInfoService.getFareVo(addrId);
        return R.ok().put("data",data);
    }















    //=====================================================


    /**
     * 列表
     * 要求可以根据key模糊查询
     *
     */
    @RequestMapping("/list")
    @RequiresPermissions("ware:wareinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareInfoService.getWareInfo(params);

        return R.ok().put("page", page);
    }



    /**
     * 列表
     *//*
    @RequestMapping("/list")
    @RequiresPermissions("ware:wareinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareInfoService.queryPage(params);

        return R.ok().put("page", page);
    }*/


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ware:wareinfo:info")
    public R info(@PathVariable("id") Long id){
		WareInfoEntity wareInfo = wareInfoService.getById(id);

        return R.ok().put("wareInfo", wareInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ware:wareinfo:save")
    public R save(@RequestBody WareInfoEntity wareInfo){
		wareInfoService.save(wareInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ware:wareinfo:update")
    public R update(@RequestBody WareInfoEntity wareInfo){
		wareInfoService.updateById(wareInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ware:wareinfo:delete")
    public R delete(@RequestBody Long[] ids){
		wareInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
