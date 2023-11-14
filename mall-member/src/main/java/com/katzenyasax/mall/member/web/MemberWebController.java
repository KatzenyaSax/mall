package com.katzenyasax.mall.member.web;

import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.member.feign.OrderFeign;
import com.katzenyasax.mall.member.interceptor.MemberInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberWebController {

    @Autowired
    OrderFeign orderFeign;

    /**
     * 返回订单数据
     */
    @RequestMapping("/memberOrder.html")
    public String getOrderLists(Model model,@RequestParam(value = "pageNum",defaultValue = "1") Long pageNum){
        Long memberId = MemberInterceptor.orderThreadLocal.get().getId();
        R r = orderFeign.getMemberOrder(pageNum,memberId);
        model.addAttribute("orders",r);
        System.out.println("     MemberWebController::getOrderLists : \"r\":"+r+", \"memberId\":"+memberId);
        return "orderList";
    }

}
