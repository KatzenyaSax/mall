package com.katzenyasax.mall.auth.service.impl;

import com.katzenyasax.common.to.UserLoginTo;
import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.auth.feign.MemberFeign;
import com.katzenyasax.mall.auth.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Autowired
    MemberFeign memberFeign;




    /**
     *
     * @return
     *
     * 登录
     * 需要远程调用member模块
     *
     */
    @Override
    public R login(UserLoginTo to) {
        R r=memberFeign.login(to);
        System.out.println("LoginService Login: "+r);
        return r;
    }
}
