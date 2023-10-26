package com.katzenyasax.mall.cart.service.Impl;

import com.alibaba.fastjson.JSON;
import com.katzenyasax.common.constant.CartConstant;
import com.katzenyasax.common.to.SkuInfoTO;
import com.katzenyasax.common.to.UserInfoTO;
import com.katzenyasax.common.to.UserLoginTO;
import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.cart.config.ThisThreadPool;
import com.katzenyasax.mall.cart.feign.ProductFeign;
import com.katzenyasax.mall.cart.interceptor.CartInterceptor;
import com.katzenyasax.mall.cart.service.CartService;
import com.katzenyasax.mall.cart.vo.Cart;
import com.katzenyasax.mall.cart.vo.CartAddVO;
import com.katzenyasax.mall.cart.vo.CartItemVO;
import io.lettuce.core.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service("cartService")
public class CartServiceImpl implements CartService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ProductFeign productFeign;

    @Autowired
    ThisThreadPool threadPool;




    /**
     *
     * @return
     *
     * 获取temple cart
     * 同样从redis获取
     *
     */
    @Override
    public Cart getCart(UserInfoTO userInfoTO) {
        //1.初始化封装对象
        Cart cart=new Cart();
        cart.setItems(new ArrayList<>());
        cart.setCountType(0l);
        cart.setCountNum(0l);
        cart.setTotalAmount(BigDecimal.ZERO);
        cart.setReduce(BigDecimal.ZERO);

        String thisKey= userInfoTO.getUserKey();;
        if(userInfoTO.getUserId()!=null){
            //用户已登录
            //就行了在另一个方法中合并临时cart到用户cart
            this.writeInUserCart(userInfoTO);
            thisKey=userInfoTO.getUserId().toString();
        }


        //获取数据key的集合
        String redis_key=CartConstant.CART_USER_PREFIX+thisKey;
        BoundHashOperations<String,Object,Object> ops = redisTemplate.boundHashOps(redis_key);

        //遍历集合
        Set<Object> keys = ops.keys();
        keys.forEach(key->{
            CartItemVO item = JSON.parseObject(
                    ops.get(key).toString()
                    , CartItemVO.class
            );
            cart.getItems().add(item);
            cart.setCountType(cart.getCountType()+1);
            cart.setCountNum(cart.getCountNum()+ item.getCount());
            cart.setTotalAmount(cart.getTotalAmount()
                    .add(item.getTotalPrice())
            );
        });

        System.out.println("getTempleCart: "+cart);
        return cart;
    }

    /**
     *
     * @param userInfoTO
     *
     * 将临时cart的数据写入用户cart中
     */
    private void writeInUserCart(UserInfoTO userInfoTO) {
        //分别获取临时和用户cart的操作柄
        String keyUser=CartConstant.CART_USER_PREFIX+userInfoTO.getUserId();
        BoundHashOperations<String,Object,Object> opsUser = redisTemplate.boundHashOps(keyUser);
        String keyTemple=CartConstant.CART_USER_PREFIX+userInfoTO.getUserKey();
        BoundHashOperations<String,Object,Object> opsTemple = redisTemplate.boundHashOps(keyTemple);
        //遍历临时cart中的数据，写入用户cart并删除临时cart的数据
        Set<Object> templeKeys = opsTemple.keys();
        if(templeKeys.size()>0){
            templeKeys.forEach(key->{
                CartItemVO item = JSON.parseObject(
                        opsTemple.get(key).toString()
                        , CartItemVO.class
                );
                this.addCartItem(Long.parseLong(
                        item.getSkuId())
                        ,item.getCount()
                        ,userInfoTO.getUserId().toString()
                );
                //删除临时cart中的数据
                opsTemple.delete(key);
            });
        }
    }


    /**
     *
     * @param thisKey
     * @return
     *
     * 添加商品到cart
     * cart在redis的名称为：
     *
     *          katzenyasax-mall::cart::<thisKey>
     *
     *  需要用到redis
     *
     * 不过存到CartItemVO中，需要用到sku表中的数据，所以还需要远程调用product模块
     * 不仅要获取sku的实体类，还要根据skuId获取attrs
     * 之后还需要
     *
     */
    @Override
    public CartItemVO addCartItem(Long skuId,Long num, String thisKey) {
        //1.获取商品信息
        R info = productFeign.info(skuId);
        SkuInfoTO skuInfo = JSON.parseObject(
                JSON.toJSONString(
                        info.get("skuInfo")
                ), SkuInfoTO.class
        );

        //2.封装CartItemVo
        CartItemVO item=new CartItemVO();
        item.setSkuId(skuId.toString());
        item.setCheck(false);   //默认false，未选中
        item.setTitle(skuInfo.getSkuTitle());
        item.setImage(skuInfo.getSkuDefaultImg());
        item.setSkuAttrValues(productFeign.getSkuAttrs(skuId));

        item.setPrice(skuInfo.getPrice());
        item.setCount(num);
        item.setTotalPrice(
                skuInfo.getPrice()
                        .multiply(BigDecimal
                                .valueOf(num)
                        )
        );
        //todo 远程调用coupon模块获取减免
        item.setReduce(BigDecimal.ZERO);
        System.out.println(JSON.toJSONString(item));

        //3.从redis中获取数据
        String redis_key=CartConstant.CART_USER_PREFIX+thisKey;
        BoundHashOperations<String,Object,Object> ops = redisTemplate.boundHashOps(redis_key);

        //4.增加商品数量存入，或直接存入
        if(ops.get(skuId.toString())!=null) {
            CartItemVO oldItem = JSON.parseObject(
                    ops.get(skuId.toString()).toString()        //redis中存的就是json了，不需要再json化，只需要toString就行了
                    ,CartItemVO.class
            );
            //新的item
            CartItemVO newItem=oldItem;
            newItem.setCount(oldItem.getCount()+num);
            newItem.setTotalPrice(
                    newItem.getPrice().multiply(
                            BigDecimal.valueOf(newItem.getCount())
                    )
            );
            ops.put(skuId.toString(),JSON.toJSONString(newItem));
            return newItem;
        }else {
            //直接存item
            ops.put(skuId.toString(), JSON.toJSONString(item));
            return item;
        }
    }


    /**
     *
     * @param skuId
     * @param thisKey
     * @return
     *
     * 重定向的页面，从redis中查询该商品
     */
    @Override
    public CartItemVO getCartItem(Long skuId,String thisKey) {
        //从redis中获取数据
        String redis_key=CartConstant.CART_USER_PREFIX+thisKey;
        BoundHashOperations<String,Object,Object> ops = redisTemplate.boundHashOps(redis_key);
        if(ops.get(skuId.toString())!=null){
            CartItemVO item = JSON.parseObject(
                    ops.get(skuId.toString()).toString()        //redis中存的就是json了，不需要再json化，只需要toString就行了
                    ,CartItemVO.class
            );
            return item;
        }
        return null;
    }


    /**
     *
     * @param skuId
     * @param thisKey
     *
     * 从cart中删除对应skuId的商品
     */
    public void deleteCartItem(Long skuId,String thisKey){
        BoundHashOperations ops = redisTemplate.boundHashOps(CartConstant.CART_USER_PREFIX + thisKey);
        ops.delete(skuId.toString());
    }


    /**
     *
     * @param skuId
     * @param thisKey
     * @param check
     *
     * 选中商品
     * 修改cart中对应skuId的cartItemVO的check属性
     *
     * 需要从redis中查询数据
     */
    @Override
    public void check(Long skuId, String thisKey, boolean check) {
        CartItemVO thisItem=this.getCartItem(skuId,thisKey);
        thisItem.setCheck(check);
        BoundHashOperations ops = redisTemplate.boundHashOps(CartConstant.CART_USER_PREFIX + thisKey);
        ops.delete(skuId.toString());
        ops.put(skuId.toString(),JSON.toJSONString(thisItem));
    }


    /**
     *
     * @param skuId
     * @param thisKey
     * @param num
     *
     * 修改商品数量
     */
    @Override
    public void count(Long skuId, String thisKey, Long num) {
        CartItemVO thisItem=this.getCartItem(skuId,thisKey);
        thisItem.setCount(num);
        thisItem.setTotalPrice(thisItem.getPrice().multiply(BigDecimal.valueOf(num)));
        BoundHashOperations ops = redisTemplate.boundHashOps(CartConstant.CART_USER_PREFIX + thisKey);
        ops.delete(skuId.toString());
        ops.put(skuId.toString(),JSON.toJSONString(thisItem));
    }


}
