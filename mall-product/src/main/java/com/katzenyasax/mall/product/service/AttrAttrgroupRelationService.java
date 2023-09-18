package com.katzenyasax.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.mall.product.entity.AttrAttrgroupRelationEntity;
import com.katzenyasax.mall.product.entity.AttrEntity;

import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:16:41
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveByAttrEntity(AttrEntity attr);
}

