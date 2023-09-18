package com.katzenyasax.mall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.katzenyasax.mall.product.service.AttrAttrgroupRelationService;
import com.katzenyasax.mall.product.vo.AttrVO_WithAttrGroupId;
import com.katzenyasax.mall.product.vo.AttrVO_WithGroupIdAndPaths;
import com.katzenyasax.mall.product.vo.AttrVO_WithGroupNameAndCatelogName;
import io.swagger.models.auth.In;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.katzenyasax.mall.product.entity.AttrEntity;
import com.katzenyasax.mall.product.service.AttrService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.R;



/**
 *              所有有关参数的接口
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:16:41
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;




    /**
    *
    * 获取普通的规格参数，和共用者
    * 普通参数的主页面
    *
    * */
    @RequestMapping("/base/list/{catelogId}")
    public R baseList(@RequestParam Map<String, Object> params,@PathVariable Integer catelogId){
        PageUtils page = attrService.queryPageBase(params,catelogId);
        return R.ok().put("page", page);
    }






    /**
     *
     * 获取普通的销售属性，和公用者
     * 销售属性的主页面
     *
     * */
    @RequestMapping("/sale/list/{catelogId}")
    public R saleList(@RequestParam Map<String, Object> params,@PathVariable Integer catelogId){
        PageUtils page = attrService.queryPageSale(params,catelogId);
        return R.ok().put("page", page);
    }






    /**
     * 保存
     * 同时实现属性和参数表的级联更新
     */
    @RequestMapping("/save")
    @RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVO_WithAttrGroupId attr){
        attrService.saveByAttrVO(attr);
        return R.ok();
    }





    /**
     * 查询参数
     * （专供参数修改的页面）
     * 传出的对象比AttrEntity多了Long类型的attrGroupId，和一个Long类型数组
     * 分别表示所属属性、所属类型的完整路径
     *
     * 因此要使用AttrVO_WithGroupIdAndPaths
     *
     *
     */
    @RequestMapping("/info/{attrId}")
    @RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
        AttrVO_WithGroupIdAndPaths attr = attrService.getAttrWithGroupIdAndPath(attrId);
        return R.ok().put("attr", attr);
    }


    /**
     * @param attr
     * @return
     *
     * 更新参数
     * 同时要保存参数属性分组，即groupId
     * 但是AttrEntity内没有attrGroupId
     * 所以要接收AttrVO_WithAttrGroupId
     */
    @RequestMapping("/update")
    @RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVO_WithAttrGroupId vo){
        attrService.updateAttrWithGroupId(vo);

        return R.ok();
    }
















    //=================================================================

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    /*@RequestMapping("/info/{attrId}")
    @RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		AttrEntity attr = attrService.getById(attrId);

        return R.ok().put("attr", attr);
    }*/

    /**
     * 保存
     */
   /* @RequestMapping("/save")
    @RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrEntity attr){
		attrService.save(attr);

        return R.ok();
    }*/

    /**
     * 修改
     */
    /*@RequestMapping("/update")
    @RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrEntity attr){
		attrService.updateById(attr);

        return R.ok();
    }*/

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
