package com.katzenyasax.mall.product.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import com.katzenyasax.common.to.SpuInfoTO;
import com.katzenyasax.mall.product.vo.spu.SpuSaveVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.katzenyasax.mall.product.entity.SpuInfoEntity;
import com.katzenyasax.mall.product.service.SpuInfoService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.R;



/**
 * spu信息
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:16:41
 */
@RestController
@RequestMapping("product/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;


    /**
     *
     * @param skuId
     * @return
     *
     * 由orderService调用，根据skuId获取完整的spuInfoEntity
     */
    @RequestMapping("/getBySkuId/{skuId}")
    public SpuInfoTO getSpuBySkuId(@PathVariable Long skuId){
        return spuInfoService.getBySkuId(skuId);
    }




    /**
     *
     * @param vo
     *
     *
     * 保存有关SpuSaveVO对象
     * 该对象为生成的，接收商品数据的对象
     *
     * 专供商品维护/发布商品
     *
     *
     */
    @RequestMapping("/save")
    public R save(@RequestBody SpuSaveVO vo){
        spuInfoService.saveSpuVo(vo);
        return R.ok();
    }


    /**
     * 被order远程调用的接口
     * 获取全部spu的weights
     */
    @RequestMapping("/allSpu")
    Map<String, BigDecimal> allSpuWeights(){
        Map<String, BigDecimal> res=spuInfoService.allSpuWeights();
        return res;
    }



    /**
     * 列表
     *
     * 根据param，返回spuInfoEntity的集合
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuInfoService.getSpuInfo(params);
        return R.ok().put("page", page);
    }

    /**
     *
     * @param spuId
     * @return
     *
     * 根据spuId上架
     * 就是把publish status改为1
     *
     *
     * 功能加强：
     * 要加入es
     *
     */
    @RequestMapping("/{spuId}/up")
    public R upSpu(@PathVariable Long spuId){
        spuInfoService.upSpu(spuId);
        return R.ok();
    }


    /**
     *
     * @param spuId
     * @return
     *
     * 根据spuId下架
     * 就是把publish status改为2
     *
     */
    @RequestMapping("/{spuId}/down")
    public R downSpu(@PathVariable Long spuId){
        spuInfoService.downSpu(spuId);
        return R.ok();
    }




//============================================================================



    /**
     * 列表
     *//*
    @RequestMapping("/list")
    @RequiresPermissions("product:spuinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuInfoService.queryPage(params);

        return R.ok().put("page", page);
    }*/


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("product:spuinfo:info")
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     *//*
    @RequestMapping("/save")
    @RequiresPermissions("product:spuinfo:save")
    public R save(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.save(spuInfo);

        return R.ok();
    }*/

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:spuinfo:update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:spuinfo:delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
