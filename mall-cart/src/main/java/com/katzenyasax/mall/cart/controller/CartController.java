package com.katzenyasax.mall.cart.controller;


import com.alibaba.fastjson.JSON;
import com.katzenyasax.common.constant.AuthConstant;
import com.katzenyasax.common.constant.CartConstant;
import com.katzenyasax.common.to.MemberTO;
import com.katzenyasax.common.to.UserInfoTO;
import com.katzenyasax.mall.cart.interceptor.CartInterceptor;
import com.katzenyasax.mall.cart.service.CartService;
import com.katzenyasax.mall.cart.vo.Cart;
import com.katzenyasax.mall.cart.vo.CartAddVO;
import com.katzenyasax.mall.cart.vo.CartItemVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
public class CartController {

    @Autowired
    CartService cartService;





    /**
     * @return
     *
     * 根据session判断是否登录，
     * 未登录则显示临时cart
     * 登录则显示用户cart
     * 若登陆时临时cart非空还要将临时cart合并到用户cart
     */
    @GetMapping("/cart.html")
    public String getCart(Model model){
        UserInfoTO userInfoTo= CartInterceptor.cartThreadLocal.get();
        System.out.println("controller中："+userInfoTo);
        //返回的cartList页面
        //return "success";
        Cart thisCart=cartService.getCart(userInfoTo);

        model.addAttribute("cart",thisCart);
        return "cartList";
    }


    /**
     *
     * @return
     *
     * 根据vo提供的信息添加商品
     */
    @GetMapping("/addCartItem")
    public String addCart(@RequestParam Long skuId, @RequestParam Long num, HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes){
        Object thisSession = session.getAttribute(AuthConstant.USER_LOGIN);
        System.out.println(thisSession);
        String thisKey;
        if (thisSession!=null){
            thisKey=JSON.parseObject(JSON.toJSONString(thisSession), MemberTO.class).getId().toString();
        }else {
            thisKey = Arrays.stream(request.getCookies()).filter(
                            thisCookie -> thisCookie.getName().equals(CartConstant.TEMPLE_USER)
                    ).collect(Collectors.toList()).get(0)
                    .getValue();
        }
        CartItemVO thisItem=cartService.addCartItem(skuId,num,thisKey);
        //model.addAttribute("cartItem",thisItem);
        redirectAttributes.addAttribute("skuId",skuId);
        return "redirect:http://cart.katzenyasax-mall.com/addCartItemSuccess";
    }


    /**
     *
     *
     * @param skuId
     * @param model
     * @return
     *
     * 重定向的方法，重定向到专门的展示页面
     *
     */
    @RequestMapping("/addCartItemSuccess")
    public String addCartItemSuccess(@RequestParam("skuId") Long skuId,Model model){
        UserInfoTO userInfoTo= CartInterceptor.cartThreadLocal.get();
        CartItemVO item=new CartItemVO();
        if(userInfoTo.getUserId()!=null) {
            item=cartService.getCartItem(skuId,userInfoTo.getUserId().toString());
        } else {
            item=cartService.getCartItem(skuId,userInfoTo.getUserKey());
        }

        model.addAttribute("cartItem",item);
        return "success";
    }


    /**
     *
     * @param skuId
     * @param checked
     * @return
     *
     * 选中商品/不选商品
     */
    @RequestMapping("/checkItem")
    public String checkItem(@RequestParam("skuId") Long skuId,@RequestParam("checked") Long checked){
        UserInfoTO userInfoTo= CartInterceptor.cartThreadLocal.get();
        String thisKey;
        if(userInfoTo.getUserId()!=null){
            thisKey=userInfoTo.getUserId().toString();
        } else{
            thisKey=userInfoTo.getUserKey();
        }
        cartService.check(skuId,thisKey,checked>0);
        //重定向到cart界面
        return "redirect:http://cart.katzenyasax-mall.com/cart.html";
    }

    /**
     *
     * @param skuId
     * @param num
     * @return
     *
     * 选中商品/不选商品
     */
    @RequestMapping("/countItem")
    public String countItem(@RequestParam("skuId") Long skuId,@RequestParam("num") Long num){
        UserInfoTO userInfoTo= CartInterceptor.cartThreadLocal.get();
        String thisKey;
        if(userInfoTo.getUserId()!=null){
            thisKey=userInfoTo.getUserId().toString();
        } else{
            thisKey=userInfoTo.getUserKey();
        }
        cartService.count(skuId,thisKey,num);
        //重定向到cart界面
        return "redirect:http://cart.katzenyasax-mall.com/cart.html";
    }



    /**
     *
     * @param skuId
     * @return
     *
     * 选中商品/不选商品
     */
    @RequestMapping("/deleteItem")
    public String deleteItem(@RequestParam("skuId") Long skuId){
        UserInfoTO userInfoTo= CartInterceptor.cartThreadLocal.get();
        String thisKey;
        if(userInfoTo.getUserId()!=null){
            thisKey=userInfoTo.getUserId().toString();
        } else{
            thisKey=userInfoTo.getUserKey();
        }
        cartService.deleteCartItem(skuId,thisKey);
        //重定向到cart界面
        return "redirect:http://cart.katzenyasax-mall.com/cart.html";
    }


}
