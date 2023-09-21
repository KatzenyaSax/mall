package com.katzenyasax.mall.coupon.service.impl;

import com.katzenyasax.common.to.MemberPrice;
import com.katzenyasax.common.to.SkuFullReductionTO;
import com.katzenyasax.mall.coupon.dao.MemberPriceDao;
import com.katzenyasax.mall.coupon.dao.SkuLadderDao;
import com.katzenyasax.mall.coupon.entity.MemberPriceEntity;
import com.katzenyasax.mall.coupon.entity.SkuLadderEntity;
import com.katzenyasax.mall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.coupon.dao.SkuFullReductionDao;
import com.katzenyasax.mall.coupon.entity.SkuFullReductionEntity;
import com.katzenyasax.mall.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    SkuFullReductionDao skuFullReductionDao;

    @Autowired
    SkuLadderDao skuLadderDao;

    @Autowired
    MemberPriceDao memberPriceDao;












    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }


    /**
     *
     * @param to
     *
     * product调用的方法
     * 传入一个满减的to
     * 要求进行处理，并保存到reduction、ladder和member price
     *
     */
    @Override
    public void saveFullReduction(SkuFullReductionTO to) {
        SkuFullReductionEntity skuFullReduction=new SkuFullReductionEntity();
        BeanUtils.copyProperties(to,skuFullReduction);
        //满减
        if(to.getFullCount().compareTo(BigDecimal.ZERO)==1){
            skuFullReductionDao.insert(skuFullReduction);
        }

        SkuLadderEntity skuLadder=new SkuLadderEntity();
        BeanUtils.copyProperties(to,skuLadder);
        //打折
        if(to.getDiscount().compareTo(BigDecimal.ZERO)==1) {
            skuLadder.setAddOther(to.getCountStatus());
            skuLadderDao.insert(skuLadder);
        }

        for(MemberPrice price:to.getMemberPrice()){
            if(price.getPrice().compareTo(BigDecimal.ZERO)==1) {
                MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
                memberPriceEntity.setSkuId(to.getSkuId());
                memberPriceEntity.setMemberLevelId(price.getId());
                memberPriceEntity.setMemberLevelName(price.getName());
                memberPriceEntity.setMemberPrice(price.getPrice());
                memberPriceDao.insert(memberPriceEntity);
            }
        }
    }

}