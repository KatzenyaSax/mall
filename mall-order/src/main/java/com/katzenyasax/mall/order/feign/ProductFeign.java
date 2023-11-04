package com.katzenyasax.mall.order.feign;

import com.katzenyasax.common.to.SpuInfoTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.Map;

@FeignClient("mall-product")
public interface ProductFeign {
    /**
     *
     * @param skuId
     * @return
     *
     * 由order服务调用
     * 获取价格
     */
    @RequestMapping("product/skuinfo/price/{skuId}")
    BigDecimal getPrice(@PathVariable("skuId") Long skuId);

    /**
     *
     * @return
     *
     * 查重量
     */
    @GetMapping("product/spuinfo/allSpu")
    Map<String, BigDecimal> allSpuWeights();


    /**
     *
     * @param skuId
     * @return
     *
     * 由orderService调用，根据skuId获取完整的spuInfoEntity
     */
    @RequestMapping("product/spuinfo/getBySkuId/{skuId}")
    SpuInfoTO getSpuBySkuId(@PathVariable Long skuId);
}
