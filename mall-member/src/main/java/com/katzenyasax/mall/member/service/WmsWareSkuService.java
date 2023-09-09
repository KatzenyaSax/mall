package com.katzenyasax.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.mall.member.entity.WmsWareSkuEntity;

import java.util.Map;

/**
 * 商品库存
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:14:07
 */
public interface WmsWareSkuService extends IService<WmsWareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

