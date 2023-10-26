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

    void check(Long skuId, String thisKey, boolean b);

    void count(Long skuId, String thisKey, Long num);

    void deleteCartItem(Long skuId,String thisKey);
}
