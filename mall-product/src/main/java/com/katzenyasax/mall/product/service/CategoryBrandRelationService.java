package com.katzenyasax.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.mall.product.entity.CategoryBrandRelationEntity;
import com.katzenyasax.mall.product.vo.BrandVO_OnlyIdAndName;
import com.katzenyasax.mall.product.vo.CategoryVO_OnlyIdAndName;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:16:41
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveName(CategoryBrandRelationEntity categoryBrandRelation);

    List<BrandVO_OnlyIdAndName> selectBrandsThatRelatedWithCatelogId(Long catelogId);

    List<CategoryVO_OnlyIdAndName> selectCategoriesThatRelatedWithBrand(Long brandId);
}

