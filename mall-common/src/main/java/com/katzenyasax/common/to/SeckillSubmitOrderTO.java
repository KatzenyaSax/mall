package com.katzenyasax.common.to;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class SeckillSubmitOrderTO {

    /**
     * 订单号
     */
    private String orderSn;
    /**
     * 用户id
     */
    private Long memberId;
    /**
     * 场次
     */
    private Long promotionSessionId;
    /**
     * skuId
     */
    private Long skuId;
    /**
     * 购买数量
     */
    private Long num;

}
