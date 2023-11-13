package com.katzenyasax.mall.ware.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("mall-order")
public interface OrderFeign {
    /**
     * 订单状态
     */
    @RequestMapping("order/order/status/{id}")
    Integer getStatus(@PathVariable("id") Long id);
}
