package com.katzenyasax.mall.ware.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("mall-product")
public interface ProductFeign {
    /**
     *
     * @param skuId
     * @return
     *
     * 远程调用product的skuinfo模块，获取sku的name
     */
    @RequestMapping("/product/skuinfo/skuName")
    String getSkuName(@RequestParam Long skuId);


}
