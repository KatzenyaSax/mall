package com.katzenyasax.test.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("mall-coupon")
public interface Feign_ShowCoupon {
    @RequestMapping("/coupon/coupon")
    public String show();
}
