package com.katzenyasax.mall.cart.interceptor;


import com.alibaba.fastjson.JSON;
import com.katzenyasax.common.constant.AuthConstant;
import com.katzenyasax.common.constant.CartConstant;
import com.katzenyasax.common.to.MemberTO;
import com.katzenyasax.common.to.UserInfoTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

/**
 * 拦截器，必须继承HandlerInterceptor才能生效
 */
@Component
public class CartInterceptor implements HandlerInterceptor {

    /**
     * 将该拦截器表示为cartThreadLocal
     */
    public static ThreadLocal<UserInfoTO> cartThreadLocal=new ThreadLocal<>();

    /**
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return
     * @throws Exception
     *
     * cart模块所有接口前判断是否已登录
     *
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //用户信息封装，之后要判断浏览器中是否有用户信息并封装
        UserInfoTO userInfoTo=new UserInfoTO();

        //先判断session，即是否已经登陆
        Object thisSession= request.getSession().getAttribute(AuthConstant.USER_LOGIN);
        if(thisSession!= null){
            //若session中有名为loginUser的cookie，表示用户已登录
            MemberTO to= JSON.parseObject(JSON.toJSONString(thisSession), MemberTO.class);
            userInfoTo.setUserId(to.getId());
            //System.out.println("session中："+userInfoTo);
        }
        //未登录，则判断cookie中是否有名为user-key的cookie，表示是否已存在临时用户，没有则应当创建
        if(request.getCookies()!=null && request.getCookies().length>0) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(CartConstant.TEMPLE_USER)) {
                    //有名为user-key的cookie
                    userInfoTo.setUserKey(cookie.getValue());
                    userInfoTo.setTempUser(true);
                    //System.out.println("cookie中"+userInfoTo);
                    break;
                }
            }
        }
        if(userInfoTo.getUserKey()==null){
            userInfoTo.setUserKey(UUID.randomUUID().toString());
        }

        cartThreadLocal.set(userInfoTo);
        return true;
    }


    /**
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler the handler (or {@link HandlerMethod}) that started asynchronous
     * execution, for type and/or instance examination
     * @param modelAndView the {@code ModelAndView} that the handler returned
     * (can also be {@code null})
     * @throws Exception
     *
     * cart模块所有接口，请求结束后执行：
     *          1.保存cookie
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoTO userInfoTo = cartThreadLocal.get();
        if(userInfoTo.getTempUser()!=true){
            //即此时无临时用户
            Cookie cookie = new Cookie(CartConstant.TEMPLE_USER, userInfoTo.getUserKey());
            cookie.setDomain("katzenyasax-mall.com");   //作用域
            cookie.setMaxAge(60 * 60 * 24 * 30);              //过期时间一个月
            response.addCookie(cookie);
        }
        //清空threadLocal，防止内存泄露
        cartThreadLocal.remove();
    }




}

