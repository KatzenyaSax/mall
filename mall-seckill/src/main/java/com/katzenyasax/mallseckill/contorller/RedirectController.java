package com.katzenyasax.mallseckill.contorller;


import cn.hutool.crypto.Mode;
import com.alibaba.fastjson.JSON;
import com.katzenyasax.common.constant.SeckillConstant;
import com.katzenyasax.common.to.SeckillSubmitOrderTO;
import com.katzenyasax.common.utils.R;
import com.katzenyasax.mallseckill.service.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RedirectController {

    @Autowired
    WebService webService;

    /**
     * 秒杀接口
     */
    @GetMapping("/kill")
    public String kill(@RequestParam String killId, @RequestParam String key, @RequestParam Long num, RedirectAttributes redirectAttributes){
        String skuId = killId.split("-")[1];
        R res = webService.kill(killId, key, num);
        String code = res.get("code").toString();
    //    System.out.println("code: "+code);
        //若失败，重定向回该商品的详情页面
        if(!code.equals(SeckillConstant.SECKILL_SUCCESS)){
            redirectAttributes.addFlashAttribute("code",code);
            return "redirect:http://item.katzenyasax-mall.com/"+skuId+".html";
        }
        //若成功，重定向到order模块进行处理
        SeckillSubmitOrderTO to = JSON.parseObject(res.get("data").toString(), SeckillSubmitOrderTO.class);
        System.out.println(to);
        redirectAttributes.addAttribute("to",JSON.toJSONString(to));
        return "redirect:http://order.katzenyasax-mall.com/seckillToTrade";
    }
}
