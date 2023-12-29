package com.katzenyasax.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.katzenyasax.common.to.SeckillSubmitOrderTO;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.mall.order.entity.OrderEntity;
import com.katzenyasax.mall.order.vo.OrderConfirmVO;
import com.katzenyasax.mall.order.vo.OrderSubmitVO;
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

    OrderConfirmVO orderConfirm(Long id);

    SubmitOrderResponseVo submitOrder(OrderSubmitVO vo);

    void dealWithOrderStatus(Long order);

    PageUtils getMemberOrder(Long id, Long memberId);

    Boolean aliPayOrder(String orderSn);

    OrderConfirmVO seckillConfirmOrder(SeckillSubmitOrderTO to);

}

