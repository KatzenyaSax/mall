package com.katzenyasax.mall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.mall.product.entity.AttrEntity;
import com.katzenyasax.mall.product.entity.AttrGroupEntity;
import com.katzenyasax.mall.product.vo.AttrAttrGroupVO_AttrIdWithAttrGroupId;
import com.katzenyasax.mall.product.vo.AttrAttrGroupVO_JustReceiveData;
import com.katzenyasax.mall.product.vo.AttrGroupVO_WithAttrs;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:16:41
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);


    PageUtils getGroupWithId(Map<String, Object> params,Integer groupId);

    Long[] getPath(Long categoryId);

    List<AttrEntity> getAttrRelatedWithGroup(Integer attrGroupId);

    PageUtils getAttrRelatedNOTWithGroup(@RequestParam Map<String, Object> params,Integer attrGroupId);

    void addRelation(List<AttrAttrGroupVO_JustReceiveData> vos);

    List<AttrGroupVO_WithAttrs> getAttrGroupWithsAttrs(Long catelogId);

    void deleteRelation(AttrAttrGroupVO_AttrIdWithAttrGroupId[] vos);

    void removeGroupAndRelation(Long[] attrGroupIds);
}

