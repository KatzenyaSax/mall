package com.katzenyasax.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.katzenyasax.mall.product.dao.AttrAttrgroupRelationDao;
import com.katzenyasax.mall.product.entity.AttrAttrgroupRelationEntity;
import com.katzenyasax.mall.product.entity.AttrEntity;
import com.katzenyasax.mall.product.service.AttrAttrgroupRelationService;
import com.katzenyasax.mall.product.service.CategoryService;
import com.katzenyasax.mall.product.service.impl.AttrAttrgroupRelationServiceImpl;
import com.katzenyasax.mall.product.vo.AttrAttrGroupVO_JustReceiveData;
import com.katzenyasax.mall.product.vo.AttrGroupVO_WithAttrs;
import io.swagger.models.auth.In;
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
    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;


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
     *
     * @param params
     * @param attrgroup
     * @return
     *
     * 查找属性
     * 如果attrgroup为0则默认查找全部
     * 如果不为0则查找对应attrGroupId的属性
     *
     *
     *
     */
    @RequestMapping("list/{attrgroup}")
    public R listAttrGroup(@RequestParam Map<String, Object> params,@PathVariable Integer attrgroup){
        PageUtils page=attrGroupService.getGroupWithId(params,attrgroup);
        return R.ok().put("page",page);
    }











    /**查找和属性发生关联的所有参数
     * 根据属性id，从关系表内获取所有与之关联的参数
     *
     * 专供属性关联界面
     *
     * @param attrGroupId
     * @return R
     *
     */
    @RequestMapping("/{attrGroupId}/attr/relation")
    public R listAttrRelation(@PathVariable Integer attrGroupId){
        List<AttrEntity> page=attrGroupService.getAttrRelatedWithGroup(attrGroupId);

        return R.ok().put("data",page);
    }










    /**查找和属性未发生关联的所有参数
     * 根据属性id，从关系表内获取所有未与之关联的参数
     *
     * 专供属性关联界面
     *
     * @param attrGroupId
     * @return R
     *
     */
    @RequestMapping("{attrGroupId}/noattr/relation")
    public R listAttrNOTRelation(@RequestParam Map<String, Object> params,@PathVariable Integer attrGroupId){
        PageUtils page=attrGroupService.getAttrRelatedNOTWithGroup(params,attrGroupId);

        return R.ok().put("page",page);
    }


    /**
     *
     * @param vos
     * @return
     *
     *
     * 接收AttrAttrGroupVO_JustReceiveData
     * 根据其中的数据增加属性和参数的关系
     *
     *
     */
    @RequestMapping("attr/relation")
    public R addRelation(@RequestBody List<AttrAttrGroupVO_JustReceiveData> vos){
        attrGroupService.addRelation(vos);
        return R.ok();
    }


    /**
     *
     * @param catelogId
     * @return
     *
     * 根据catelogId，获取对应的属性，和属性对应的参数
     *
     * 返回值为一个vo
     *
     */

    @RequestMapping("{catelogId}/withattr")
    public R listAttrGroupsAndAttrs(@PathVariable Long catelogId){
        List<AttrGroupVO_WithAttrs> finale=attrGroupService.getAttrGroupWithsAttrs(catelogId);
        return R.ok().put("data",finale);
    }












    //====================================================================================================


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
