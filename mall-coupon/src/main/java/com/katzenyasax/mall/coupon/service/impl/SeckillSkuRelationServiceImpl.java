package com.katzenyasax.mall.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.katzenyasax.common.constant.SeckillConstant;
import com.katzenyasax.common.to.SeckillSessionTO;
import com.katzenyasax.common.to.SeckillSkuRelationTO;
import com.katzenyasax.common.to.SkuInfoTO;
import com.katzenyasax.common.utils.UtilsOfKatzenyaSax;
import com.katzenyasax.mall.coupon.feign.ProductFeign;
import com.qiniu.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.coupon.dao.SeckillSkuRelationDao;
import com.katzenyasax.mall.coupon.entity.SeckillSkuRelationEntity;
import com.katzenyasax.mall.coupon.service.SeckillSkuRelationService;


@Service("seckillSkuRelationService")
public class SeckillSkuRelationServiceImpl extends ServiceImpl<SeckillSkuRelationDao, SeckillSkuRelationEntity> implements SeckillSkuRelationService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ProductFeign productFeign;



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SeckillSkuRelationEntity> page = this.page(
                new Query<SeckillSkuRelationEntity>().getPage(params),
                null
        );

        return new PageUtils(page);
    }


    /**
     * 获取场次关联的商品
     */
    @Override
    public PageUtils listSeckillSku(Map<String, Object> params) {
        /**
         * 从redis中获取数据，封装进list
         */
        /*BoundHashOperations ops = redisTemplate.boundHashOps(SeckillConstant.SECKILL_SKU_PREFIX);
        Set<Object> keys = ops.keys();
        List<SeckillSkuRelationEntity> skus=new ArrayList<>();
        keys.forEach(
               thisKey-> {
                   SeckillSkuRelationEntity thisRelation = JSON.parseObject(
                           ops.get(thisKey).toString()
                           , SeckillSkuRelationEntity.class
                   );
                   if (thisRelation.getPromotionSessionId().equals(params.get("promotionSessionId"))){
                       skus.add(thisRelation);
                   }
               }
        );

        Page finale = UtilsOfKatzenyaSax.getPages(
                (Integer) params.get("page")
                , (Integer) params.get("limit")
                , skus
        );
        //return new PageUtils(finale);*/

        /**
         * 从数据库中获取数据
         */
        if(!StringUtils.isNullOrEmpty((String) params.get("promotionSessionId"))){
            IPage<SeckillSkuRelationEntity> page = this.page(
                    new Query<SeckillSkuRelationEntity>().getPage(params)
                    , new QueryWrapper<SeckillSkuRelationEntity>().eq("promotion_session_id", (String) params.get("promotionSessionId"))
            );
            return new PageUtils(page);
        }
        return null;
    }


    /**
     * 保存关联商品
     */
    @Override
    public void saveSeckillSku(SeckillSkuRelationEntity seckillSkuRelation) {
        /**
         * 存入数据库
         */
        baseMapper.insert(seckillSkuRelation);
        /**
         * 存入redis
         */
        //从product查skuInfo
        SkuInfoTO skuInfo = JSON.parseObject(
                JSON.toJSONString(productFeign.info(seckillSkuRelation.getSkuId()).get("skuInfo"))
                ,SkuInfoTO.class
        );
        //finale，最终数据封装
        SeckillSkuRelationTO finale=new SeckillSkuRelationTO();
        //使用BeanUtils赋值
        BeanUtils.copyProperties(seckillSkuRelation,finale);
        //封装
        finale.setSkuInfoTO(skuInfo);
        //存入redis
        redisTemplate.boundHashOps(SeckillConstant.SECKILL_SKU_PREFIX+seckillSkuRelation.getPromotionSessionId()).put(
                seckillSkuRelation.getSkuId().toString()
                , JSON.toJSONString(finale)
        );
    }


    /**
     * 删除sessionSkuRelation，数据库与redis
     */
    @Override
    public void removeSessionSkuRelation(List<Long> list) {
        list.forEach(relationId->{
            SeckillSkuRelationEntity thisRelation = baseMapper.selectById(relationId);
            /**
             * 从redis中删除该session的hash表中key为skuId的键值对
             */
            redisTemplate.boundHashOps(SeckillConstant.SECKILL_SKU_PREFIX+thisRelation.getPromotionSessionId()).delete(thisRelation.getSkuId().toString());
            /**
             * 在数据库中删除该relation
             */
            baseMapper.deleteById(relationId);
        });
    }


}