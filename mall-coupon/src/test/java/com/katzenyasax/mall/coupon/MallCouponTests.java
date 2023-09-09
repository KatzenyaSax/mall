package com.katzenyasax.mall.coupon;

import com.katzenyasax.mall.coupon.entity.CouponEntity;
import com.katzenyasax.mall.coupon.service.CouponService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MallCouponTests {

    @Test
    void contextLoads() {
    }


    @Autowired
    CouponService couponService;
    @Test
    void test01(){
        CouponEntity coupon=new CouponEntity();
        coupon.setCouponName("110001");
        couponService.save(coupon);

    }




}
