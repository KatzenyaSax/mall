package com.katzenyasax.mall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.katzenyasax.mall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.katzenyasax.mall.product.entity.AttrGroupEntity;
import com.katzenyasax.mall.product.service.AttrGroupService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.R;


@Slf4j
/**
 * 属性分组
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:16:41
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryService categoryService;




    //列出属性分组
    @RequestMapping("list/{attrgroup}")
    public R listAttrGroup(@RequestParam Map<String, Object> params,@PathVariable Integer attrgroup){
        PageUtils page=attrGroupService.queryPage(params,attrgroup);
        return R.ok().put("page",page);
    }













    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrGroupService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     *
     *
     * 修改数据回显信息
     * 包括该分组的完整路径
     *
     */
    @RequestMapping("/info/{attrGroupId}")
    @RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        Long attrGroupCatelogId=attrGroup.getCatelogId();
        //先获取分组id
        Long[] path=categoryService.getCategoryPath(attrGroupCatelogId);
        //再获取完整路径
        attrGroup.setPath(path);
        //设置回显路径


        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
