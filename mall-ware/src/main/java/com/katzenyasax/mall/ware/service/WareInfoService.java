package com.katzenyasax.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.mall.ware.entity.WareInfoEntity;
import com.katzenyasax.mall.ware.vo.FareVo;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:17:29
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils getWareInfo(Map<String, Object> params);

    FareVo getFareVo(Long addrId);
}

