package com.katzenyasax.mall.auth.controller;


import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.auth.service.RegisterService;
import com.katzenyasax.mall.auth.vo.UserRegisterVo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class RegisterController {

    @Autowired
    RegisterService registerService;

    /**
     *
     * @param
     * @return
     *
     * 注册用户
     * 在此之前要先判断验证码是否相同
     * 还要判断用户名和手机号未被占用（远程调用member服务）
     *
     */
    //todo 分布式session问题
    //
    @RequestMapping("/register")
    public String register(@Valid UserRegisterVo vo, BindingResult result, /*Model model*/ RedirectAttributes redirectAttributes){

        //1.若数据有异常，跳转回注册页面
        if(result.hasErrors()){
            //并且要返回错误字段
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(
                    error -> "msg"
                    ,error -> error.getDefaultMessage()
            ));
            //  model.addAttribute(errors);
            //不用model了，这里使用redirectAttributes，防止重复提交表单

            System.out.println("数据异常");

            redirectAttributes.addFlashAttribute("errors",errors);
            //转到reg.html页面，带上所有的error这个map的数据
            return "redirect:http://auth.katzenyasax-mall.com/reg.html";
        }

        //2.判断code是否正确
        boolean isRightCode = registerService.isRightCode(vo);
        if(isRightCode=true){
            System.out.println("RegisterController：验证码正确");
            //验证码正确，则注册
            //注册
            R res=registerService.Register(vo);
            System.out.println("RegisterController"+res);
            if (res.get("code").equals(0)){
                //如果code为0，说明注册成功了
                //并且还要删除code
                registerService.deleteCode(vo.getPhone());
                return "redirect:http://auth.katzenyasax-mall.com/login.html";
            }
            else{
                Map<String,String> errors=new HashMap<>();
                errors.put("msg",res.get("msg").toString());
                redirectAttributes.addFlashAttribute("errors",errors);
                return "redirect:http://auth.katzenyasax-mall.com/reg.html";
            }
        }
        else {
            //验证码错误
            System.out.println("验证码错误");
            return "redirect:http://auth.katzenyasax-mall.com/reg.html";
        }
    }
}
