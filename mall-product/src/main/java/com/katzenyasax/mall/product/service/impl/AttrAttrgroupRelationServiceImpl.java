package com.katzenyasax.mall.product.service.impl;

import com.katzenyasax.mall.product.entity.AttrEntity;
import com.katzenyasax.mall.product.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.product.dao.AttrAttrgroupRelationDao;
import com.katzenyasax.mall.product.entity.AttrAttrgroupRelationEntity;
import com.katzenyasax.mall.product.service.AttrAttrgroupRelationService;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Autowired
    private AttrService attrService;













    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }



    //根据AttrEntity保存到关系表
    //因为AttrEntity中存有了
    @Override
    public void saveByAttrEntity(AttrEntity attr) {
        //获取了已经存入attr表内的实体的自增id
        //因为只有在存入表内后，才能得到id
        Long attrId=attrService.getById(attr).getAttrId();
        Long attrGroupId=attr.getAttrId();
    }

}