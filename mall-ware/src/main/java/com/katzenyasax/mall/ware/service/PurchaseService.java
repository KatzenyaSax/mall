package com.katzenyasax.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.mall.ware.entity.PurchaseEntity;
import com.katzenyasax.mall.ware.vo.PurchaseVO_IdAndErrorPurchaseDetail;
import com.katzenyasax.mall.ware.vo.PurchaseVO_Merge;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:17:29
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils getUnreceive(Map<String, Object> params);

    void mergePurchse(PurchaseVO_Merge vo);

    void receivePurchase(List<Long> ids);

    void donePurchase(PurchaseVO_IdAndErrorPurchaseDetail vo);
}

