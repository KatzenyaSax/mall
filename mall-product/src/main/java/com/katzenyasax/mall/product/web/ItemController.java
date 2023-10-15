package com.katzenyasax.mall.product.web;

import com.katzenyasax.mall.product.service.SpuInfoService;
import com.katzenyasax.mall.product.vo.item.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ItemController {

    @Autowired
    SpuInfoService spuInfoService;



    @GetMapping("/{skuId}.html")
    public String showItem(@PathVariable("skuId") String skuId, Model model){
        SkuItemVo item=spuInfoService.getSkuItem(skuId);
        model.addAttribute("item",item);
        return "item";
    }


    /*@GetMapping("/{skuId}.html")
    public String  showItem(@PathVariable String skuId){
        return "item.html";
    }*/


}
