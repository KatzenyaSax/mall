package com.katzenyasax.mall.product.vo.item;

import lombok.Data;

import java.util.List;

@Data
public class SkuItemSaleAttrVo {
    private Long attrId;

    private String attrName;

    private List<AttrValueWithSkuIdVo> attrValues;
}
