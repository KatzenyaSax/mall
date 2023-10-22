package com.katzenyasax.mall.cart.vo;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Cart {

    private List<CartItemVO> items;

    private Long countNum;          //商品总数

    private Long countType;         //商品种类的数量

    private BigDecimal totalAmount;         //总价格

    private BigDecimal reduce;


    /**
     * 另外禁止在vo内写其他方法，vo应当只是一个纯粹的变量容器
     */

}
