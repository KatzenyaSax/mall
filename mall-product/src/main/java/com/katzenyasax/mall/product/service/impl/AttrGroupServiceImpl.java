package com.katzenyasax.mall.product.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.katzenyasax.mall.product.entity.CategoryEntity;
import com.katzenyasax.mall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.product.dao.AttrGroupDao;
import com.katzenyasax.mall.product.entity.AttrGroupEntity;
import com.katzenyasax.mall.product.service.AttrGroupService;
import org.w3c.dom.Attr;

@Slf4j
@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }








    @Override
    public PageUtils queryPage(Map<String, Object> params, Integer catelogId) {
        if(catelogId==0){
            String key=(String) params.get("key");
            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>().like("attr_group_id", key).or().like("attr_group_name", key).or().like("descript", key);
            //wrapper：sql评判标准，查找时会自动根据评判标准筛选不满足标准的对象
            IPage<AttrGroupEntity> pageFinale = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
            return new PageUtils(pageFinale);
        }
        else {
            QueryWrapper<AttrGroupEntity> wrapper =new QueryWrapper<AttrGroupEntity>().eq("catelog_id",catelogId);
            //wrapper：sql评判标准，查找时会自动根据评判标准筛选不满足标准的对象
            IPage<AttrGroupEntity> pageFinale=this.page(new Query<AttrGroupEntity>().getPage(params),wrapper);
            return new PageUtils(pageFinale);
            //catelogId不为0，但是key也不为空
        }
    }



    @Override
    public Long[] getPath(Long Id) {
        CategoryService categoryService=new CategoryServiceImpl();
        return categoryService.getCategoryPath(Id);
        //直接调用Category的方法返回
    }

}