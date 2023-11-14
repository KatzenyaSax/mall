package com.katzenyasax.mall.member.interceptor;

import com.alibaba.fastjson.JSON;
import com.katzenyasax.common.constant.AuthConstant;
import com.katzenyasax.common.to.MemberTO;
import com.katzenyasax.common.to.UserInfoTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class MemberInterceptor implements HandlerInterceptor {
    /**
     * 将该拦截器表示为orderThreadLocal
     */
    public static ThreadLocal<MemberTO> orderThreadLocal =new ThreadLocal<>();

    /**
     * member模块所有接口前判断是否已登录
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        /**
         * 一定要放行远程调用的url
         * 我们所有远程调用的url的路劲都是/member开头的，所以直接放行就行了
         */
        if(new AntPathMatcher().match("/member/**",request.getRequestURI())){
            return true;
        }


        //用户信息封装，之后要判断浏览器中是否有用户信息并封装
        System.out.println("Entered Interceptor !");
        UserInfoTO userInfoTo=new UserInfoTO();
        //先判断session，即是否已经登陆
        Object thisSession= request.getSession().getAttribute(AuthConstant.USER_LOGIN);
        if(thisSession!= null){
            //若session中有名为loginUser的cookie，表示用户已登录
            //要将用户信息通过threadLocal的方式交给下游服务其
            MemberTO to= JSON.parseObject(JSON.toJSONString(thisSession), MemberTO.class);
            orderThreadLocal.set(to);
            return true;
        }
        //未登录，则直接返回true，提交订单必须要先登录，重定向到登录页面
        else {
            System.out.println("OrderInterceptor: NO LOGIN USER! will redirect to login.html");
            response.sendRedirect("http://auth.katzenyasax-mall.com/login.html");
            return false;
        }
    }
    /**
     * url处理结束后调用
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //清空threadLocal
        orderThreadLocal.remove();
    }
}
