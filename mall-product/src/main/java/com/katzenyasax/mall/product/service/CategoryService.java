package com.katzenyasax.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.mall.product.entity.CategoryEntity;
import com.katzenyasax.mall.product.vo.catalogVO.Catalog2VO;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:16:41
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listAsTree();


    void hideByIds(List<Long> list);

    void Sort(CategoryEntity[] category);

    Long[] getCategoryPath(Long categoryId);


    List<CategoryEntity> listOne();

    Map<String, List<Catalog2VO>> getCatalogJson();
}

