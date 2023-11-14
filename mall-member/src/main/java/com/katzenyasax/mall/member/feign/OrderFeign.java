package com.katzenyasax.mall.member.feign;


import com.katzenyasax.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("mall-order")
public interface OrderFeign {
    /**
     * 查询用户的订单，由member服务调用
     */
    @RequestMapping("order/order/orders/{pageNum}")
    R getMemberOrder(@PathVariable Long pageNum,@RequestParam Long memberId);
}
