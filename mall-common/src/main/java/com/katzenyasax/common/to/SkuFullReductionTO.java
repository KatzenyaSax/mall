package com.katzenyasax.common.to;

import io.swagger.models.auth.In;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuFullReductionTO {
    private Long skuId;     //只有这个需要自己set
    //以下全是sku自带的
    private BigDecimal fullCount;
    private BigDecimal discount;
    private Long countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Long priceStatus;
    private List<MemberPrice> memberPrice;

}
