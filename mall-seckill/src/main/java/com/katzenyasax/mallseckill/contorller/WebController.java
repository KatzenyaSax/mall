package com.katzenyasax.mallseckill.contorller;


import com.alibaba.fastjson.JSON;
import com.katzenyasax.common.constant.SeckillConstant;
import com.katzenyasax.common.to.SeckillSkuRelationTO;
import com.katzenyasax.common.to.SeckillSubmitOrderTO;
import com.katzenyasax.common.utils.R;
import com.katzenyasax.mallseckill.service.WebService;
import com.qiniu.util.StringUtils;
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
    //@GetMapping("/kill")
    public R kill(@RequestParam String killId,@RequestParam String key,@RequestParam Long num){
        R res = webService.kill(killId, key, num);
        String code = res.get("code").toString();
        if(code.equals(SeckillConstant.SECKILL_ERROR_OUT_OF_TIME)){
            return R.error(code);
        } else if(code.equals(SeckillConstant.SECKILL_ERROR_TOO_MUCH_BOUGHT)){
            return R.error(code);
        } else if(code.equals(SeckillConstant.SECKILL_ERROR_SEMAPHORE_OVER)){
            return R.error(code);
        }

        SeckillSubmitOrderTO to = JSON.parseObject(res.get("data").toString(), SeckillSubmitOrderTO.class);

        return R.ok().put("data",to);
    }




}
