package com.katzenyasax.mall.ware.service.impl;

import com.qiniu.util.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.ware.dao.PurchaseDetailDao;
import com.katzenyasax.mall.ware.entity.PurchaseDetailEntity;
import com.katzenyasax.mall.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    /**
     *
     * @param params
     * @return
     *
     * mybatis plus自动生成的方法
     * 查询所有purchaseDetails
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                new QueryWrapper<PurchaseDetailEntity>()
        );

        return new PageUtils(page);
    }




    /**
     * 列表
     *
     * 根据status和wareId查询采购需求
     */
    @Override
    public PageUtils getPurchaseDetails(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> wrapper=new QueryWrapper<>();
        String key=(String)params.get("key");
        String status=(String) params.get("status");
        String wareId=(String) params.get("wareId");

        if(!StringUtils.isNullOrEmpty(key)){
            wrapper.and(obj->{
               obj.like("purchase_id",key).or().eq("sku_id",key);
            });
        }
        if(!StringUtils.isNullOrEmpty(status)){
            wrapper.eq("status",status);
        }
        if(!StringUtils.isNullOrEmpty(wareId)){
            wrapper.eq("ware_id",wareId);
        }

        IPage<PurchaseDetailEntity> finale = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(finale);
    }

}