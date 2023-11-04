package com.katzenyasax.mall.ware.feign;


import com.katzenyasax.common.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("mall-member")
public interface MemberFeign {
    /**
     *
     * @param id
     * @return
     *
     * 获取收货地址
     */
    @RequestMapping("member/memberreceiveaddress/info/{id}")
    R info(@PathVariable("id") Long id);
}
