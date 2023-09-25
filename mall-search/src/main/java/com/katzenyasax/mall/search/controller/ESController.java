package com.katzenyasax.mall.search.controller;

import com.katzenyasax.common.exception.BizCodeEnume;
import com.katzenyasax.common.to.SkuEsModel;
import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.search.service.ESService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/search/es")
public class ESController {

    @Autowired
    ESService eSService;


    /**
     *
     * @param skuEsModels
     * @return
     *
     * 上传skuEsModels到es
     *
     */
    @RequestMapping("/up")
    public R SkuUp(@RequestBody List<SkuEsModel> skuEsModels){
        //try {
            eSService.SkuUp(skuEsModels);
            return R.ok();
        //}catch (Exception e){
            //log.error("SpuUP(/search/es/up) 发生错误");
            //return R.error(BizCodeEnume.PRODUCT_ES_SAVE_EXCEPTION.getCode(),BizCodeEnume.PRODUCT_ES_SAVE_EXCEPTION.getMsg());
        //}

        //TODO 更完善的异常反馈机制
    }

}
