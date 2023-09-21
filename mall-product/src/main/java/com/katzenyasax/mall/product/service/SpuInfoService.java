package com.katzenyasax.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.mall.product.entity.SpuInfoEntity;
import com.katzenyasax.mall.product.vo.spu.SpuSaveVO;

import java.util.Map;

/**
 * spu信息
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:16:41
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuVo(SpuSaveVO vo);
}

