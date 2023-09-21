package com.katzenyasax.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.katzenyasax.common.to.SkuFullReductionTO;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.mall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 08:49:54
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveFullReduction(SkuFullReductionTO to);
}

