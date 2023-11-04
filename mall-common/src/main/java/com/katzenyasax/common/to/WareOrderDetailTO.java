package com.katzenyasax.common.to;

import lombok.Data;

@Data
public class WareOrderDetailTO {

    private Long orderId;

    private String orderSn;

    private Long skuId;

    private Long skuNum;

    private Long wareId;

}
