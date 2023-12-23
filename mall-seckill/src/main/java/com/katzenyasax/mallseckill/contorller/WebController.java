package com.katzenyasax.mallseckill.contorller;


import com.katzenyasax.common.to.SeckillSkuRelationTO;
import com.katzenyasax.common.utils.R;
import com.katzenyasax.mallseckill.service.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WebController {

    @Autowired
    WebService webService;

    /**
     * 商城主页查询所有的seckill信息，包括seckillSkuRelation和skuInfo
     */
    @GetMapping("/getCurrentSeckillSkus")
    public List<SeckillSkuRelationTO> getCurrentSeckillSku(){
        return webService.getCurrentSeckillSku();
    }


    /**
     * 秒杀逻辑
     */
    @GetMapping("/kill")
    public R kill(@RequestParam String killId,@RequestParam String key,@RequestParam Long num){

        String orderSn = webService.kill(killId,key,num);

        return R.ok().put("data",orderSn);
    }




}
