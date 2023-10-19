package com.katzenyasax.mall.auth.feign;


import com.katzenyasax.common.to.UserLoginTo;
import com.katzenyasax.common.to.UserRegisterTo;
import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.auth.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
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
    R register(@RequestBody UserRegisterTo to);

    @RequestMapping("/member/member/login")
    R login(UserLoginTo to);
}
