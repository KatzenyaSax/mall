package com.katzenyasax.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description: 订单确认页需要用的数据
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-07-02 18:59
 **/

@Data
public class OrderConfirmVO {


    /** 会员收获地址列表 **/
    List<MemberAddressVo> memberAddressVos;

    /** 所有选中的购物项 **/
    List<OrderItemVo> items;

    /** 发票记录 **/
    /** 优惠券（会员积分） **/
    private Integer integration;

    /** 防止重复提交的令牌 **/
    private String orderToken;

    Map<Long,Boolean> stocks;

    private Integer count;

    private BigDecimal total;

    private BigDecimal payPrice;

    /**
     * 是否为秒杀订单
     */
    private Boolean isSeckillTrade = false;

 }
