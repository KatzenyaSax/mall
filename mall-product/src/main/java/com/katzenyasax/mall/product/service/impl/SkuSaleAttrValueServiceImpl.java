package com.katzenyasax.mall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.product.dao.SkuSaleAttrValueDao;
import com.katzenyasax.mall.product.entity.SkuSaleAttrValueEntity;
import com.katzenyasax.mall.product.service.SkuSaleAttrValueService;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }



    /**
     *
     * @param skuId
     * @return
     *
     * 根据skuId获取sku的销售属性attrs
     */
    @Override
    public List<String> getSkuAttrs(long skuId) {
        return baseMapper.selectList(
                new QueryWrapper<SkuSaleAttrValueEntity>()
                        .eq("sku_id",skuId)
                )
                .stream()
                .map(thisEntity->
                        thisEntity.getAttrName()+":"+thisEntity.getAttrValue()
                    ).collect(Collectors.toList()
                );
    }

}