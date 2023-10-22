package com.katzenyasax.mall.cart.feign;


import com.katzenyasax.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("mall-product")
public interface ProductFeign {
    @RequestMapping("product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);

    @GetMapping("product/skusaleattrvalue/skuAttrs/{skuId}")
    List<String> getSkuAttrs(@PathVariable("skuId") Long skuId);

}
