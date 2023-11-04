package com.katzenyasax.mall.ware.service.impl;

import cn.hutool.core.date.DateTime;
import com.katzenyasax.common.to.OrderItemTO;
import com.katzenyasax.common.to.WareOrderDetailTO;
import com.katzenyasax.mall.ware.dao.WareOrderTaskDao;
import com.katzenyasax.mall.ware.dao.WareOrderTaskDetailDao;
import com.katzenyasax.mall.ware.entity.WareOrderTaskDetailEntity;
import com.katzenyasax.mall.ware.entity.WareOrderTaskEntity;
import com.katzenyasax.mall.ware.exception.NoStockException;
import com.qiniu.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.ware.dao.WareSkuDao;
import com.katzenyasax.mall.ware.entity.WareSkuEntity;
import com.katzenyasax.mall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {


    @Autowired
    WareOrderTaskDao wareOrderTaskDao;

    @Autowired
    WareOrderTaskDetailDao wareOrderTaskDetailDao;



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


    /**
     *
     * @return
     *
     * 由order模块调用的方法
     *
     * 获取所有商品有货商品并返回一个map
     *
     */
    @Override
    public Map<Long, Boolean> getSkuStocks() {
        return baseMapper.selectList(null).stream().collect(Collectors.toMap(
                s->s.getSkuId(),
                s->Boolean.TRUE
        ));
    }


    /**
     * @param items
     * @return 锁定库存
     * 返回一个map，表示哪些商品是否全部锁定成功
     */
    @Transactional(rollbackFor = NoStockException.class)
    @Override
    public Map<Long, Long> lockWare(List<OrderItemTO> items) {
        //一次查所有表中数据
        List<WareSkuEntity> allWareSku = baseMapper.selectList(null);
        return items.stream().collect(Collectors.toMap(
                item->item.getSkuId()
                ,item -> {
                    /**
                     * item中有用的信息：skuId，skuQuantity，orderId，orderSn
                     */
                    //查询所有skuId为item的skuId的仓库
                    List<WareSkuEntity> wareSkus = allWareSku.stream().filter(
                            a -> a.getSkuId().equals(item.getSkuId())
                    ).toList();
                    if (wareSkus != null && wareSkus.size() > 0) {
                        //当有有仓库存有该sku时...
                        for (WareSkuEntity ware : wareSkus) {
                            if (ware.getStock()-ware.getStockLocked()>= item.getSkuQuantity()){
                                //有一个仓库的库存足够时，将该仓库的部分库存锁定，即是更新该行数据
                                //来一个更新数据
                                WareSkuEntity newWare=new WareSkuEntity();
                                BeanUtils.copyProperties(ware,newWare);
                                newWare.setStockLocked(ware.getStockLocked()+ item.getSkuQuantity());
                                //更新数据库
                                baseMapper.updateById(newWare);
                                return ware.getId();
                            }
                            else {
                                //一个足够库存的仓库也没有，抛异常
                                throw new NoStockException(item.getSkuId());
                            }
                        }
                    }
                    else {
                        //没有任何仓库存有该sku时，抛异常
                        throw new NoStockException(item.getSkuId());
                    }
                    return null;
                }
        ));
    }


    /**
     * @param to
     *
     * 存入ware_order_task和ware_order_task_detail
     *
     */
    @Override
    public void saveTasks(List<WareOrderDetailTO> to) {
        for (WareOrderDetailTO task : to) {
            WareOrderTaskEntity ifExistOne=wareOrderTaskDao.selectOne(new QueryWrapper<WareOrderTaskEntity>()
                    .eq("order_id",task.getOrderId())
                    .and(m->m.eq("order_sn",task.getOrderSn()))
                    .and(n->n.eq("ware_id",task.getWareId())));

            if (ifExistOne==null) {
                //如果此时wareOrderTask表内无数据，则新建一个对象存入
                WareOrderTaskEntity taskEntity = new WareOrderTaskEntity();
                //构建task
                taskEntity.setOrderId(task.getOrderId());
                taskEntity.setOrderSn(task.getOrderSn());
                taskEntity.setOrderBody(task.getSkuId().toString());
                taskEntity.setCreateTime(new DateTime());
                taskEntity.setWareId(task.getWareId());
                wareOrderTaskDao.insert(taskEntity);
            } else {
                //如果此时wareOrderTask表内已有数据，则将orderBody拼接上该skuId
                ifExistOne.setOrderBody(ifExistOne.getOrderBody()+","+task.getSkuId());
                wareOrderTaskDao.updateById(ifExistOne);
            }

            //再查一遍库，拿到最新的数据
            ifExistOne=wareOrderTaskDao.selectOne(new QueryWrapper<WareOrderTaskEntity>()
                    .eq("order_id",task.getOrderId())
                    .eq("order_sn",task.getOrderSn())
                    .eq("ware_id",task.getWareId()));
            //此时不管如何，ifExistOne都有值，而且都拿到了taskId
            //构建detail
            WareOrderTaskDetailEntity taskDetailEntity=new WareOrderTaskDetailEntity();
            taskDetailEntity.setTaskId(ifExistOne.getId());
            taskDetailEntity.setSkuId(task.getSkuId());
            taskDetailEntity.setSkuNum(Integer.parseInt(task.getSkuNum().toString()));
            //存入
            //?存不了wareId
            wareOrderTaskDetailDao.insert(taskDetailEntity);


        }
    }

}