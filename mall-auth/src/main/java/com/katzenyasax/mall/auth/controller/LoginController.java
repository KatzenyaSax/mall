package com.katzenyasax.mall.auth.controller;


import com.katzenyasax.common.to.UserLoginTo;
import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.auth.service.LoginService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    @Autowired
    LoginService loginService;

    /**
     *
     * 用户登录
     */
    @RequestMapping("/login")
    public String login(@Valid UserLoginTo to, BindingResult result, RedirectAttributes redirectAttributes, HttpSession session){
        if(result.hasErrors()){
            //并且要返回错误字段
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(
                    error -> "msg"
                    ,error -> error.getDefaultMessage()
            ));

            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.katzenyasax-mall.com/login.html";
        }
        System.out.println(to.toString());
        R r=loginService.login(to);
        System.out.println("LoginController Login: "+r);
        if(r.get("code").equals(0)){
            //登陆成功
            //返回session

            session.setAttribute("loginUser",r.get("LoginUser"));

            return "redirect:http://katzenyasax-mall.com";
        }
        else {
            Map<String,String> errors=new HashMap<>();
            errors.put("msg",r.get("msg").toString());
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.katzenyasax-mall.com/login.html";
        }
    }


    /**
     *
     * @return
     *
     * 用户qq扫码登录接口
     *
     */
    @RequestMapping("/")
    public R QQLogin(){
        //TODO
        // qq扫码登陆


        return R.ok();
    }






}
