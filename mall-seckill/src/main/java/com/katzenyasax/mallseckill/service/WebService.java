package com.katzenyasax.mallseckill.service;

import com.katzenyasax.common.to.SeckillSkuRelationTO;

import java.util.List;

public interface WebService {
    List<SeckillSkuRelationTO> getCurrentSeckillSku();

    String kill(String killId, String key, Long num);
}
