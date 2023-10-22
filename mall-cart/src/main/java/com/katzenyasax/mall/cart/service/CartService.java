package com.katzenyasax.mall.cart.service;

import com.katzenyasax.common.to.UserInfoTO;
import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.cart.vo.Cart;
import com.katzenyasax.mall.cart.vo.CartAddVO;
import com.katzenyasax.mall.cart.vo.CartItemVO;

public interface CartService {
    Cart getCart(UserInfoTO userInfoTO);

    CartItemVO addCartItem(Long skuId, Long num, String thisKey);

    CartItemVO getCartItem(Long skuId, String thisKey);
}
