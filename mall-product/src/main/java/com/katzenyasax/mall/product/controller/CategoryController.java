package com.katzenyasax.mall.product.controller;

import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.product.entity.CategoryEntity;
import com.katzenyasax.mall.product.service.CategoryService;
import com.katzenyasax.mall.product.service.impl.CategoryBrandRelationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * 商品三级分类
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:16:41
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryBrandRelationServiceImpl categoryBrandRelationService;




    //商品三级分类
    @RequestMapping("/list/tree")
    public R listTree(){
        Long l=System.currentTimeMillis();
        List<CategoryEntity> categoryEntities=categoryService.listAsTree();
        System.out.println("list/tree：token："+(System.currentTimeMillis()-l));
        return R.ok().put("success", categoryEntities);
    }


    //逻辑删除
    @CacheEvict(value = {"product-category"},allEntries = true)
    @RequestMapping("/delete")
    public R deleteSafe(@RequestBody Long[] catIds){
        categoryService.hideByIds(Arrays.asList(catIds));
        for(Long id:catIds){
            categoryBrandRelationService.deleteCategory(id);
        }

        return R.ok();
    }



    //拖拽功能排序
    @CacheEvict(value = {"product-category"},allEntries = true)
    @RequestMapping("/update/sort")
    public R updateSort(@RequestBody CategoryEntity[] category){
        categoryService.updateBatchById(Arrays.stream(category).toList());
        return R.ok();
    }







    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */

    @CacheEvict(value = {"product-category"},allEntries = true)
    @RequestMapping("/save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     *
     * 修改时，也修改关系表
     *
     *
     */
    @CacheEvict(value = {"product-category"},allEntries = true)
    @RequestMapping("/update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());

        return R.ok();
    }






    /**
     * 删除
     */
    //@RequestMapping("/delete")
    public R delete(@RequestBody Long[] catIds){
		categoryService.removeByIds(Arrays.asList(catIds));
        return R.ok();
    }

}
