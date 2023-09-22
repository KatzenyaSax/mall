package com.katzenyasax.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.mall.product.entity.AttrEntity;
import com.katzenyasax.mall.product.entity.ProductAttrValueEntity;
import com.katzenyasax.mall.product.vo.AttrVO_WithAttrGroupId;
import com.katzenyasax.mall.product.vo.AttrVO_WithGroupIdAndPaths;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:16:41
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageBase(Map<String, Object> params,Integer attrId);

    PageUtils queryPageSale(Map<String, Object> params, Integer catelogId);

    void saveByAttrVO(AttrVO_WithAttrGroupId attr);

    AttrVO_WithGroupIdAndPaths getAttrWithGroupIdAndPath(Long attrId);

    void updateAttrWithGroupId(AttrVO_WithAttrGroupId vo);

    List<ProductAttrValueEntity> getSpuById(Long spuId);

    void updateSpuAttr(List<ProductAttrValueEntity> list, Long spuId);
}

