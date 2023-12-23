package com.katzenyasax.mall.product.feign;


import com.katzenyasax.common.to.SeckillSkuRelationTO;
import com.katzenyasax.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("mall-seckill")
public interface SeckillFeign {


    /**
     * 商城主页查询所有的seckill信息，包括seckillSkuRelation和skuInfo
     */
    @GetMapping("/getCurrentSeckillSkus")
    List<SeckillSkuRelationTO> getCurrentSeckillSku();


}
