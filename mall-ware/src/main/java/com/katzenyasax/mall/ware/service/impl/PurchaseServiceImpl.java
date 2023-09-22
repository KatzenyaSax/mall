package com.katzenyasax.mall.ware.service.impl;

import cn.hutool.core.date.DateTime;
import com.katzenyasax.common.constant.WareConstant;
import com.katzenyasax.mall.ware.dao.PurchaseDetailDao;
import com.katzenyasax.mall.ware.dao.WareSkuDao;
import com.katzenyasax.mall.ware.entity.PurchaseDetailEntity;
import com.katzenyasax.mall.ware.entity.WareSkuEntity;
import com.katzenyasax.mall.ware.feign.ProductFeign;
import com.katzenyasax.mall.ware.vo.ErrorItem;
import com.katzenyasax.mall.ware.vo.PurchaseVO_IdAndErrorPurchaseDetail;
import com.katzenyasax.mall.ware.vo.PurchaseVO_Merge;
import com.qiniu.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.ware.dao.PurchaseDao;
import com.katzenyasax.mall.ware.entity.PurchaseEntity;
import com.katzenyasax.mall.ware.service.PurchaseService;

@Slf4j
@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    PurchaseDetailDao purchaseDetailDao;

    @Autowired
    WareSkuDao wareSkuDao;




    /**
     *
     * @param params
     * @return
     *
     * mybatis plus自动生成的方法
     * 查询所有采购单
     *
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    /**
     *
     * @param params
     * @return
     *
     * 获取未领取的采购单
     * 也即是状态为0还未分配的采购单，或是状态为1刚分配给人还未处理的采购单
     */
    @Override
    public PageUtils getUnreceive(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status",0).or().eq("status",1)
        );
        return new PageUtils(page);
    }










    /**
     *
     * @param vo
     *
     * 合并从采购需求为采购单
     * params包含purchaseId和purchaseDetailId数组
     * 代表要合并需求到哪一个采购单
     * 和所有需要合并到采购单的需求
     */
    @Override
    public void mergePurchse(PurchaseVO_Merge vo) {
        Long purchaseId=vo.getPurchaseId();
        Long[] items=vo.getItems();

        if(purchaseId!=null){
            for(Long detailId:items){
                PurchaseDetailEntity entity=purchaseDetailDao.selectById(detailId);
                entity.setPurchaseId(purchaseId);
                entity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
                purchaseDetailDao.updateById(entity);
            }
            //遍历item，修改item对应的数据
            PurchaseEntity entity=baseMapper.selectById(purchaseId);
            entity.setUpdateTime(new DateTime());
            baseMapper.updateById(entity);
            //要修改采购单的更新日期
        }
        else{
            PurchaseEntity purchase=new PurchaseEntity();
            purchase.setPriority(1);
            purchase.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchase.setCreateTime(new DateTime());
            purchase.setUpdateTime(new DateTime());
            baseMapper.insert(purchase);
            //新建一个purchaseEntity，并存储
            Long newPurchaseId=purchase.getId();
            //此时才purchaseEntity已经被存入，被分配了id
            for(Long detailId:items){
                PurchaseDetailEntity entity=purchaseDetailDao.selectById(detailId);
                entity.setPurchaseId(newPurchaseId);
                entity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
                purchaseDetailDao.updateById(entity);
            }
        }
    }



    /**
     *
     * @param ids
     * @return
     *
     * 领取采购单
     * 根据前端传回的采购单的id封装为的ids，改变采购单的状态status
     *
     */
    @Override
    public void receivePurchase(List<Long> ids) {
        //分为三步：
        //1.判断id对应的purchase是否为已分配状态
        //2.改变purchase的状态为已领取
        //3.改变每个purchase对应的purchaseDetails的状态为采购中

        for(Long id:ids){
            PurchaseEntity purchase=baseMapper.selectById(id);
            if(purchase.getStatus()==WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()){
                //只有已分配、未领取的采购单才会进入更改流程
                purchase.setStatus(WareConstant.PurchaseStatusEnum.RECEIVED.getCode());
                //更改采购单的状态为已领取
                List<PurchaseDetailEntity> purchaseDetailEntities=purchaseDetailDao.selectList(new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id",id));
                //获取当前循环中采购单对应的所有采购需求
                for(PurchaseDetailEntity entity:purchaseDetailEntities){
                    entity.setStatus(WareConstant.PurchaseDetailStatusEnum.RECEIVED.getCode());
                    purchaseDetailDao.updateById(entity);
                }
                //将当前循环对应的采购需求的状态改为采购中
            }
            purchase.setUpdateTime(new DateTime());
            baseMapper.updateById(purchase);
        }
    }



    /**
     *
     * @param vo
     * @return
     *
     * 完成采购
     * 并将对应的所有采购需求改为采购完成
     * 此外若items不为空，需要更改items中对应的采购需求的状态为采购失败，采购单也改成异常
     * 而采购成功的需求则更改状态为完成，并且入库
     * 入库则需要WareSkuDao
     *
     */
    @Autowired
    ProductFeign productFeign;
    @Override
    public void donePurchase(PurchaseVO_IdAndErrorPurchaseDetail vo) {
        Long purchaseId=vo.getId();
        List<ErrorItem> errors=vo.getItems();
        List<Long> errorIds=new ArrayList<>();
        if(errors!=null) {
            for (ErrorItem error : errors) {
                errorIds.add(error.getItemId());
            }
        }
        //获取所有采购失败的需求对象的id

        PurchaseEntity purchaseEntity=baseMapper.selectById(purchaseId);
        //获取采购单对象
        List<PurchaseDetailEntity> purchaseDetailEntities=purchaseDetailDao.selectList(new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id",purchaseId));
        //获取对应的全部需求对象

        Boolean flag=true;
        //判断该采购单是否全部完成采购

        for(PurchaseDetailEntity entity:purchaseDetailEntities){
            Long itemId=entity.getId();
            if(errorIds.contains(itemId)&&!errorIds.isEmpty()){
                //如果该需求属于异常需求的集合，修改状态为异常，不入库
                entity.setStatus(WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode());
                purchaseDetailDao.updateById(entity);
                flag=false;
            }
            else{
                //若该需求正常完成采购，入库
                entity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISHED.getCode());
                //入库的思路是：
                //查询该需求的wareId和skuId
                //先去到wareId对应的仓库，在从仓库中查询skuId
                //若skuId存在，即该仓库存在同样商品的库存，则直接对该库存数量stock进行增加
                //若不存在，则新建一个
                WareSkuEntity wareSkuEntity=wareSkuDao.selectOne(new QueryWrapper<WareSkuEntity>().eq("ware_id",entity.getWareId()).eq("sku_id",entity.getSkuId()));
                if(wareSkuEntity==null){
                    //若库存中不存在同一仓库的统一商品，则新创建一个商品入库
                    //而wareId是添加需求时必须指定的，因此默认一件需求必有wareId
                    WareSkuEntity newEntity=new WareSkuEntity();
                    newEntity.setSkuId(entity.getSkuId());
                    newEntity.setWareId(entity.getWareId());
                    newEntity.setStock(entity.getSkuNum());
                    //查询sku的name需要远程调用product的skuInfo，利用skuID查询
                    log.info("Sku Id:"+entity.getSkuId());
                    newEntity.setSkuName(productFeign.getSkuName(entity.getSkuId()));
                    newEntity.setStockLocked(0);
                    //完成了sku的定义
                    wareSkuDao.insert(newEntity);
                    //完成入库
                }
                else{
                    //若库存中存在同一商品，则加到商品数量上
                    Integer stock=wareSkuEntity.getStock();
                    wareSkuEntity.setStock(stock+entity.getSkuNum());
                    wareSkuDao.updateById(wareSkuEntity);
                }
                purchaseDetailDao.updateById(entity);
                //更新需求状态

            }
            //若该需求正常采购完成，入库
        }

        if(flag){
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.FINISHED.getCode());
            //若flag为true，即没有采购失败的需求，则purchase的状态为完成
        }
        else{
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.HASERROR.getCode());
            //若flag为false，即有采购失败的需求，则purchase的状态为有异常
        }

        purchaseEntity.setUpdateTime(new DateTime());
        //统一更新时间

        baseMapper.updateById(purchaseEntity);
        //更新数据

    }


}