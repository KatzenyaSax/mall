package com.katzenyasax.mall.cart.vo;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartItemVO {

    private String skuId;

    private Boolean check;              //是否选中

    private String title;

    private String image;

    private List<String> skuAttrValues;

    private BigDecimal price;

    private Long count;

    private BigDecimal totalPrice;

    private BigDecimal reduce;

}
