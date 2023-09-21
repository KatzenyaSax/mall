package com.katzenyasax.mall.product.feign;


import com.katzenyasax.common.to.SkuFullReductionTO;
import com.katzenyasax.common.to.SpuBoundsTO;
import com.katzenyasax.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("mall-coupon")
public interface CouponFeign {


    /**
     *
     * @param to
     * @return
     *
     * 远程调用coupon模块
     * 传入一个to，要求coupon模块对其进行处理并存入sms_spu_bounds表内
     *
     */
    @PostMapping("/coupon/spubounds/save")
    R saveBounds(@RequestBody SpuBoundsTO to);


    /**
     *
     * @param to
     * @return
     *
     * 远程调用coupon模块
     * 传入一个to，要求coupon模块对其进行处理并存入sms_sku_full_reduction表内
     *
     *
     */
    @RequestMapping("/coupon/skufullreduction/save")
    R saveFullReduction(@RequestBody SkuFullReductionTO to);





}
