package com.katzenyasax.mall.ware.service.impl;

import com.qiniu.util.StringUtils;
import io.jsonwebtoken.Header;
import org.apache.commons.collections.map.FixedSizeMap;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.ware.dao.WareSkuDao;
import com.katzenyasax.mall.ware.entity.WareSkuEntity;
import com.katzenyasax.mall.ware.service.WareSkuService;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    /**
     *
     * @param params
     * @return
     *
     * mybatis plus自动生成的方法
     * 查询所有sku
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
        );

        return new PageUtils(page);
    }




    /**
     * 列表
     * params中包含wareId和skuId
     * 要求根据这俩查询sku
     */
    @Override
    public PageUtils getSkuInfo(Map<String, Object> params) {
        String wareId=(String) params.get("wareId");
        String skuId=(String) params.get("skuId");


        QueryWrapper<WareSkuEntity> wrapper=new QueryWrapper<>();
        if(!StringUtils.isNullOrEmpty(wareId)){
            wrapper.eq("ware_id",wareId);
        }
        if(!StringUtils.isNullOrEmpty(skuId)){
            wrapper.eq("sku_id",skuId);
        }

        IPage<WareSkuEntity> finale = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(finale);
    }




    /**
     * 响应product模块的方法
     * skuId，获取skuId对应的stock
     * 返回值为一个map，包含该skuId对应的商品所处的不同仓库的id和各仓库内的库存
     */
    @Override
    public Map<Long, Integer> getStockBySkuxId(Long skuId) {
        List<WareSkuEntity> wareSkuEntities=baseMapper.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id",skuId));
        Map<Long,Integer> finale=new HashMap<>();
        if(!wareSkuEntities.isEmpty()) {
            for (WareSkuEntity entity : wareSkuEntities) {
                finale.put(entity.getWareId(), entity.getStock()-entity.getStockLocked());
                //这里库存应该是总库存减去冻结的库存
            }
        }
        return finale;
    }

}