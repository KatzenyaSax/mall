package com.katzenyasax.mallseckill.service;

import com.katzenyasax.common.to.SeckillSkuRelationTO;
import com.katzenyasax.common.utils.R;

import java.util.List;

public interface WebService {
    List<SeckillSkuRelationTO> getCurrentSeckillSku();

    R kill(String killId, String key, Long num);
}
