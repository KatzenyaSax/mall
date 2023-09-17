package com.katzenyasax.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.katzenyasax.mall.product.entity.CategoryBrandRelationEntity;
import com.katzenyasax.mall.product.service.CategoryBrandRelationService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.R;



/**
 * 品牌分类关联
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:16:41
 */
@Slf4j
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;



    //获取商品关联
    @GetMapping(value = "/catelog/list")
    public R listCatelog(@RequestParam("brandId")int brandId){
        log.info("brandId: "+String.valueOf(brandId));
        List<CategoryBrandRelationEntity> data=categoryBrandRelationService.list(
                new QueryWrapper<CategoryBrandRelationEntity>().eq(
                        "brand_id",brandId
                )
        );
        return R.ok().put("data",data);
    }



    //获取品牌关联
    @GetMapping(value = "/brand/list")
    public R lisBrand(@RequestParam("catelogId")int catelogId){
        List<CategoryBrandRelationEntity> data=categoryBrandRelationService.list(
                new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id",catelogId));
        return R.ok().put("data",data);
    }















    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     *
     *
     * 实现了属性分类和品牌名字的存储
     *
     *
     *
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.saveName(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
