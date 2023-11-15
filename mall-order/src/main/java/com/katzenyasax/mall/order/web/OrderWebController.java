package com.katzenyasax.mall.order.web;


import com.alibaba.fastjson.JSON;
import com.katzenyasax.common.to.MemberTO;
import com.katzenyasax.mall.order.interceptor.OrderInterceptor;
import com.katzenyasax.mall.order.service.OrderService;
import com.katzenyasax.mall.order.vo.OrderConfirmVo;
import com.katzenyasax.mall.order.vo.OrderSubmitVo;
import com.katzenyasax.mall.order.vo.SubmitOrderResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrderWebController {

    @Autowired
    OrderService orderService;

    /**
     *
     * @param model
     * @return
     *
     * 根据session获取的用户信息，获得用户的订单确认页面
     */
    @RequestMapping("/toTrade")
    public String toTrade(Model model){
        System.out.println("Entered OrderWebController::toTrade !");
        MemberTO memberTO = OrderInterceptor.orderThreadLocal.get();
        System.out.println(JSON.toJSONString("OrderWebController::toTrade::Get member: "+memberTO));
        //获取orderConfirmVo
        OrderConfirmVo vo=orderService.orderConfirm(memberTO.getId());
        model.addAttribute("confirmOrderData",vo);
        return "confirm";
    }


    /**
     * 提交订单
     */
    @RequestMapping("/submitOrder")
    public String submitOrder(Model model, OrderSubmitVo vo, RedirectAttributes redirectAttributes){
        MemberTO memberTO = OrderInterceptor.orderThreadLocal.get();
        System.out.println(vo);
        //获取下单状态
        SubmitOrderResponseVo resp=orderService.submitOrder(vo);
        //System.out.println(resp.getCode());
        if(resp.getCode().equals(0)){
            //下单成功
            model.addAttribute("submitOrderResp",resp);
            return "pay";
        } else{
            switch (resp.getCode()){
                case 1:
                    redirectAttributes.addFlashAttribute("msg","无货");
                    break;
                case 2:
                    redirectAttributes.addFlashAttribute("msg","价格有变动");
                    break;
                case 3:
                    redirectAttributes.addFlashAttribute("msg","请勿重复提交订单");
                    break;
            }
            //下单失败，跳转回toTrade页面
            return "redirect:http://order.katzenyasax-mall.com/toTrade";
        }
    }


    /**
     * 支付
     */
    @RequestMapping("/aliPayOrder")
    public String aliPayOrder(@RequestParam String orderSn){
        orderService.aliPayOrder(orderSn);
        return "redirect:http://member.katzenyasax-mall.com/memberOrder.html";
    }



}
