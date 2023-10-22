package com.katzenyasax.mall.auth.feign;


import com.katzenyasax.common.to.UserLoginTO;
import com.katzenyasax.common.to.UserRegisterTO;
import com.katzenyasax.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("mall-member")
public interface MemberFeign {

    /**
     *
     * @param to
     * @return
     *
     * 注册会员
     */
    @RequestMapping ("/member/member/register")
    R register(@RequestBody UserRegisterTO to);

    @RequestMapping("/member/member/login")
    R login(UserLoginTO to);
}
