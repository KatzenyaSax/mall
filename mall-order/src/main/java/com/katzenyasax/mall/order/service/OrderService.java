package com.katzenyasax.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.katzenyasax.common.to.MemberAddressTO;
import com.katzenyasax.common.to.MemberTO;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.mall.order.entity.OrderEntity;
import com.katzenyasax.mall.order.vo.OrderConfirmVo;
import com.katzenyasax.mall.order.vo.OrderSubmitVo;
import com.katzenyasax.mall.order.vo.SubmitOrderResponseVo;

import java.util.Map;

/**
 * 订单
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:15:27
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    OrderConfirmVo orderConfirm(Long id);

    SubmitOrderResponseVo submitOrder(OrderSubmitVo vo);
}

