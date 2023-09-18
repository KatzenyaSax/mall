package com.katzenyasax.mall.product.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.product.dao.BrandDao;
import com.katzenyasax.mall.product.entity.BrandEntity;
import com.katzenyasax.mall.product.service.BrandService;

@Slf4j
@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {


    /**
     *
     * @param params
     * @return
     *
     *
     * 获取品牌信息
     * 品牌管理主页面
     * 可以根据关键字key进行模糊查询
     *
     *
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //获取关键字key
        String key=(String) params.get("key");
        log.info(String.valueOf((key==null)));
        //若关键字key不为空：
        if(key!=null){
            //进行对name和descript的模糊匹配
            QueryWrapper<BrandEntity> wrapper=new QueryWrapper<BrandEntity>().like("name",key).or().like("descript",key);
            return new PageUtils(this.page(new Query<BrandEntity>().getPage(params),wrapper));
        }
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                new QueryWrapper<BrandEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void updateStatusById(BrandEntity brand) {

    }

}