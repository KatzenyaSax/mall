package com.katzenyasax.mall.search.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.katzenyasax.common.to.SkuEsModel;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ESService {
    void SkuUp(List<SkuEsModel> skuEsModels);
}
