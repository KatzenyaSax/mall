package com.katzenyasax.mall.order.feign;


import com.katzenyasax.common.to.MemberAddressTO;
import com.katzenyasax.common.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("mall-member")
public interface MemberFeign {
    /**
     *
     * @param id
     * @return
     *
     * 获取该用户所有的地址
     */
    @GetMapping("member/memberreceiveaddress/getByMemberId/{id}")
    List<MemberAddressTO> getByMemberId(@PathVariable String id);


    /**
     *
     * @param id
     * @return
     *
     * 获取一个用户信息
     * 使用R返回，口令是"member"
     */
    @RequestMapping("member/member/info/{id}")
    @RequiresPermissions("member:member:info")
    R info(@PathVariable("id") Long id);

}
