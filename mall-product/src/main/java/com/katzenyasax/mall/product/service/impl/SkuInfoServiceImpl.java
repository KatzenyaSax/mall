package com.katzenyasax.mall.product.service.impl;

import cn.hutool.core.date.DateTime;
import com.katzenyasax.common.to.SkuFullReductionTO;
import com.katzenyasax.common.to.SpuBoundsTO;
import com.katzenyasax.mall.product.dao.*;
import com.katzenyasax.mall.product.entity.*;
import com.katzenyasax.mall.product.feign.CouponFeign;
import com.katzenyasax.mall.product.vo.spu.*;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.product.service.SkuInfoService;
import org.springframework.transaction.annotation.Transactional;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {


    @Autowired
    SpuInfoDao spuInfoDao;

    @Autowired
    SpuInfoDescDao spuInfoDescDao;

    @Autowired
    SpuImagesDao spuImagesDao;

    @Autowired
    ProductAttrValueDao productAttrValueDao;

    @Autowired
    AttrDao attrDao;

    @Autowired
    SkuImagesDao skuImagesDao;

    @Autowired
    SkuInfoDao skuInfoDao;

    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Autowired
    CouponFeign couponFeign;



    /**
     *
     * @param params
     * @return
     *
     * mybatis plus自动生成的方法
     * 保存商品，接受的是前端发送的param
     *
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }




}