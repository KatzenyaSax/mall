package com.katzenyasax.mall.product.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient("mall-ware")
public interface WareFeign {

    /**
     * 根据skuId，找ware模块要skuId对应的stock
     */
    @RequestMapping("ware/waresku/stock/{skuId}")
    Map<Long,Integer> getStockBySkuId(@PathVariable Long skuId);

}
