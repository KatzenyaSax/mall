package com.katzenyasax.mall.product.feign;


import com.katzenyasax.common.to.SkuEsModel;
import com.katzenyasax.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("mall-search")
public interface SearchFeign {


    /**
     *
     * @param skuEsModels
     * @return
     *
     * 上传skuEsModels到es
     *
     */
    @RequestMapping("/search/es/up")
    R SkuUp(@RequestBody List<SkuEsModel> skuEsModels);

}
