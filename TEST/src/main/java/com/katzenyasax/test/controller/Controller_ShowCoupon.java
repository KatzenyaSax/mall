package com.katzenyasax.test.controller;


import com.katzenyasax.common.utils.R;
import com.katzenyasax.test.feign.Feign_ShowCoupon;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller_ShowCoupon {
    @Resource
    Feign_ShowCoupon showCoupon;
    @RequestMapping(value = "/showCoupon")
    public R GetAllCoupons(){
        return R.ok(showCoupon.show());
    }
}
