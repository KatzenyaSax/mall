package com.katzenyasax.mall.coupon.service.impl;

import com.katzenyasax.common.to.SpuBoundsTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.coupon.dao.SpuBoundsDao;
import com.katzenyasax.mall.coupon.entity.SpuBoundsEntity;
import com.katzenyasax.mall.coupon.service.SpuBoundsService;


@Service("spuBoundsService")
public class SpuBoundsServiceImpl extends ServiceImpl<SpuBoundsDao, SpuBoundsEntity> implements SpuBoundsService {

    @Autowired
    SpuBoundsDao spuBoundsDao;

    /**
     *
     * @param params
     * @return
     *
     *
     * 自动生成的方法
     * 分页查询所有spu bounds
     *
     *
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuBoundsEntity> page = this.page(
                new Query<SpuBoundsEntity>().getPage(params),
                new QueryWrapper<SpuBoundsEntity>()
        );

        return new PageUtils(page);
    }


    /**
     *
     * @param to
     *
     * 和product远程通信时，product调用的方法
     * 接收的是一个to，和spu的Bounds参数一致
     * 需要保存到spu_bounds表内
     *
     *
     */
    @Override
    public void saveBoundsTO(SpuBoundsTO to) {
        SpuBoundsEntity entity=new SpuBoundsEntity();
        BeanUtils.copyProperties(to,entity);
        spuBoundsDao.insert(entity);
    }

}