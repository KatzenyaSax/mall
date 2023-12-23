package com.katzenyasax.mall.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.katzenyasax.common.constant.SeckillConstant;
import com.katzenyasax.common.to.SeckillSessionTO;
import com.katzenyasax.common.to.SeckillSkuRelationTO;
import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.coupon.dao.SeckillSkuRelationDao;
import com.katzenyasax.mall.coupon.entity.SeckillSkuRelationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.coupon.dao.SeckillSessionDao;
import com.katzenyasax.mall.coupon.entity.SeckillSessionEntity;
import com.katzenyasax.mall.coupon.service.SeckillSessionService;


@Service("seckillSessionService")
public class SeckillSessionServiceImpl extends ServiceImpl<SeckillSessionDao, SeckillSessionEntity> implements SeckillSessionService {


    @Autowired
    SeckillSkuRelationDao seckillSkuRelationDao;

    @Autowired
    RedisTemplate redisTemplate;



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SeckillSessionEntity> page = this.page(
                new Query<SeckillSessionEntity>().getPage(params),
                new QueryWrapper<SeckillSessionEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 上线秒杀场次
     *
     * @return
     */
    @Override
    public R uploadSession(List<Long> sessionIds) {
        sessionIds.forEach(id->{
            SeckillSessionEntity thisSession = baseMapper.selectById(id);
            thisSession.setStatus(1);
            /**
             * 在数据库中修改状态
             */
            baseMapper.updateById(thisSession);
            /**
             * 查询对应的秒杀商品，并存信号量
             */
            seckillSkuRelationDao.selectList(new QueryWrapper<SeckillSkuRelationEntity>().eq("promotion_session_id",id)).forEach(thisRelation->{
                redisTemplate.boundHashOps(SeckillConstant.SECKILL_SKU_SEMAPHORE + thisRelation.getPromotionSessionId()).put(
                        thisRelation.getSkuId().toString()
                        ,thisRelation.getSeckillCount().toString()
                );
            });
        });
        return R.ok();
    }


    /**
     * 查询三日后将开始的秒杀场次，以List<Long>形式返回值
     */
    @Override
    public List<Long> selectInThreeDays() {
        List<SeckillSessionEntity> allDown = baseMapper.selectList(new QueryWrapper<SeckillSessionEntity>().eq("status", 0));
        List<Long> ids=new ArrayList<>();
        allDown.forEach(thisSession->{
            if(thisSession.getStartTime().getDay()-new Date().getDay()<=2){
                ids.add(thisSession.getId());
            }
        });
        return ids;
    }

    /**
     * 查询最近三日的session
     */
    @Override
    public List<SeckillSessionTO> selectCurrentSession() {
        List<SeckillSessionEntity> allDown = baseMapper.selectList(new QueryWrapper<SeckillSessionEntity>().eq("status",1));
        List<SeckillSessionEntity> entities=new ArrayList<>();
        allDown.forEach(thisSession->{
            //若此时处于某个秒杀场次
            if(new Date().after(thisSession.getStartTime()) && new Date().before(thisSession.getEndTime())){
                entities.add(thisSession);
            }
            else if(thisSession.getStartTime().getDay()-new Date().getDay()<=2){
                entities.add(thisSession);
            }
        });
        return entities.stream().map(thisSession->{
            //单个to
            SeckillSessionTO thisTO = JSON.parseObject(JSON.toJSONString(thisSession), SeckillSessionTO.class);
            //得到该to对应的relationTOs
            List<SeckillSkuRelationTO> relationTOs = seckillSkuRelationDao.selectList(
                    new QueryWrapper<SeckillSkuRelationEntity>().eq("promotion_session_id", thisTO.getId())
            ).stream().map(
                    thisRelation -> JSON.parseObject(JSON.toJSONString(thisRelation), SeckillSkuRelationTO.class)
            ).collect(Collectors.toList());
            //封装
            thisTO.setRelationTOList(relationTOs);
            return thisTO;
        }).collect(Collectors.toList());
    }


    /**
     * 删除session，数据库与redis一同删除
     */
    @Override
    public void removeSessions(List<Long> list) {
        list.forEach(id->{
            /**
             * 从数据库中删除
             */
            baseMapper.deleteById(id);
            /**
             * 从redis删除
             */
            redisTemplate.opsForHash().delete(SeckillConstant.SECKILL_SKU_PREFIX+id.toString());
        });
    }


    /**
     * 报错session至数据库与redis
     */
    @Override
    public R saveSession(SeckillSessionEntity seckillSession) {
        /**
         * 存入数据库
         */
        baseMapper.insert(seckillSession);
        /**
         * 存入redis
         */
        redisTemplate.opsForValue().set(
                SeckillConstant.SECKILL_SESSION+seckillSession.getId()
                ,JSON.toJSONString(seckillSession)
        );
        /**
         * 存信号量等
         */
        List<Long> ids = new ArrayList<>();
        ids.add(seckillSession.getId());
        this.uploadSession(ids);
        return R.ok();
    }

}