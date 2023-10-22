package com.katzenyasax.mall.auth.controller;


import com.katzenyasax.common.constant.AuthConstant;
import com.katzenyasax.common.to.UserLoginTO;
import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.auth.service.LoginService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     *
     * 用户登录
     */
    @RequestMapping("/login")
    public String login(@Valid UserLoginTO to, BindingResult result, RedirectAttributes redirectAttributes, HttpSession session){
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

            session.setAttribute(AuthConstant.USER_LOGIN,r.get("LoginUser"));

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
     * @param session
     * @return
     *
     * 用户登出，将session置为空
     */
    @RequestMapping("/logout.html")
    public String logOut(HttpSession session){
        session.setAttribute(AuthConstant.USER_LOGIN,null);
        return "redirect:http://katzenyasax-mall.com";
    }



    /**
     * 利用redis中保存的数据自动登录
     */
    @GetMapping("/login.html")
    public String autoLogin(HttpSession session){
        Object cookie = session.getAttribute(AuthConstant.USER_LOGIN);
        if(cookie!=null){
            //如果浏览器存入cookie，则直接从cookie拿取
            System.out.println("cookie exists");
            //重定向
            return "redirect:http://katzenyasax-mall.com";
        }
        else {
            //浏览器无cookie，接下来将匹配redis中有无数据
                return "login";
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
