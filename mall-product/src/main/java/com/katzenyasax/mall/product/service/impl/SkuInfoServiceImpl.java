package com.katzenyasax.mall.product.service.impl;

import cn.hutool.core.date.DateTime;
import com.aliyuncs.utils.StringUtils;
import com.katzenyasax.common.to.SkuFullReductionTO;
import com.katzenyasax.common.to.SpuBoundsTO;
import com.katzenyasax.mall.product.dao.*;
import com.katzenyasax.mall.product.entity.*;
import com.katzenyasax.mall.product.feign.CouponFeign;
import com.katzenyasax.mall.product.vo.spu.*;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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




    /**
     * 列表
     *
     * 根据param列出sku信息
     * param中的参数有：key、catelogId、brandId、min、max
     * 注意表内的字段名还是catalogId
     *
     */
    @Override
    public PageUtils getSkuInfo(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> wrapper=new QueryWrapper<>();
        String key=(String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            log.info("key not null: "+key);
            wrapper.and(w->{
                w.like("sku_name",key).or().like("sku_title",key).or().like("sku_id",key).or().like("sku_subtitle",key);
            });
        }
        String  catelogId=(String) params.get("catelogId");
        if(catelogId!=null && !"0".equalsIgnoreCase(catelogId)){
            log.info("catelogId not null: "+catelogId);
            wrapper.eq("catalog_id",catelogId);
        }
        String  brandId=(String) params.get("brandId");
        if(brandId!=null && !"0".equalsIgnoreCase(brandId)){
            log.info("brandId not null: "+brandId);
            wrapper.eq("brand_id",brandId);
        }
        String max=(String)params.get("max");
        String min=(String)params.get("min");
        if(!StringUtils.isEmpty(min)){
            wrapper.ge("price",min);
        }
        if(!StringUtils.isEmpty(max) && !"0".equalsIgnoreCase(max)){
            wrapper.le("price",max);
        }
        IPage<SkuInfoEntity> finale = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                wrapper
        );
        return new  PageUtils(finale);
    }







    /**
     *
     * @param skuId
     * @return
     *
     * ware模块调用的方法
     * 根据传来的skuId
     * 查询sku的name
     */
    @Override
    public String getSkuName(Long skuId) {
        SkuInfoEntity entity=baseMapper.selectById(skuId);
        return entity.getSkuName();
    }


}