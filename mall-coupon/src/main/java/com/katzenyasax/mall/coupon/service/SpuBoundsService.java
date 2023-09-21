package com.katzenyasax.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.katzenyasax.common.to.SpuBoundsTO;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.mall.coupon.entity.SpuBoundsEntity;

import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 08:49:54
 */
public interface SpuBoundsService extends IService<SpuBoundsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveBoundsTO(SpuBoundsTO to);
}

