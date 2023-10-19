package com.katzenyasax.mall.auth.controller;


import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.auth.config.SendCodeConfigurationProperties;
import com.katzenyasax.mall.auth.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {


    @Autowired
    SendCodeConfigurationProperties sendCodeConfigurationProperties;




   /**
    * 已被viewController替代
    */
   /*
    @GetMapping("/login.html")
    public String logIn(){
        return "login";
    }

    @GetMapping("/reg.html")
    public String Register(){
        return "reg";
    }*/


    /**
     * @param phone
     *
     * 发送短信验证码
     * 使用的是阿里云的服务
     *
     *
     *
     */
    @GetMapping("/sms/sendCode")
    public R sendCode(@RequestParam String phone){
        R data=sendCodeConfigurationProperties.send(phone);
        return data;
    }

}
