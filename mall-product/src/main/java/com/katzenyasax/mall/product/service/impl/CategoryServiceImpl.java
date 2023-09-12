package com.katzenyasax.mall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.product.dao.CategoryDao;
import com.katzenyasax.mall.product.entity.CategoryEntity;
import com.katzenyasax.mall.product.service.CategoryService;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }



    @Override
    public List<CategoryEntity> listAsTree() {
        //查出所有分类
        List<CategoryEntity> entities=baseMapper.selectList(null);
        //获取一级子类
        List<CategoryEntity> oneCategory=entities.stream().filter(categoryEntity -> categoryEntity.getCatLevel()==1).toList();
        //获取所有二级子类
        List<CategoryEntity> twoCategory=entities.stream().filter(categoryEntity -> categoryEntity.getCatLevel()==2).toList();
        //获取所有三级子类
        List<CategoryEntity> threeCategory=entities.stream().filter(categoryEntity -> categoryEntity.getCatLevel()==3).toList();
        //从三级子类开始遍历，将三级子类组装到二级子类
        for(CategoryEntity category3:threeCategory){
            for(CategoryEntity category2:twoCategory){
                if(category3.getParentCid()==category2.getCatId()){
                    category2.getChildren().add(category3);
                }
            }
        }
        //从二级子类开始遍历，将二级子类组装到一级子类
        for(CategoryEntity category2:twoCategory){
            for(CategoryEntity category1:oneCategory){
                if(category2.getParentCid()==category1.getCatId()){
                    category1.getChildren().add(category2);
                }
            }
        }
        return oneCategory;
    }





    @Override
    public void hideByIds(List<Long> list) {
        //TODO 1.判断数据是否被引用
        //2.隐藏数据
        //  直接删除就可以了
        baseMapper.deleteBatchIds(list);
    }


}