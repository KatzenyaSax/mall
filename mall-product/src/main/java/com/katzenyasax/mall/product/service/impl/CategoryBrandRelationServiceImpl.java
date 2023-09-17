package com.katzenyasax.mall.product.service.impl;

import com.katzenyasax.mall.product.dao.BrandDao;
import com.katzenyasax.mall.product.dao.CategoryDao;
import com.katzenyasax.mall.product.entity.CategoryEntity;
import com.katzenyasax.mall.product.service.BrandService;
import com.katzenyasax.mall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.product.dao.CategoryBrandRelationDao;
import com.katzenyasax.mall.product.entity.CategoryBrandRelationEntity;
import com.katzenyasax.mall.product.service.CategoryBrandRelationService;

@Slf4j
@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }









    //先要获取brandId和catelogId对应的name：
    //所以需要调用BrandService和CategoryService
    @Autowired
    BrandDao brandDao;
    @Autowired
    CategoryDao categoryDao;
    @Override
    public void saveName(CategoryBrandRelationEntity categoryBrandRelation) {
        //获取了name
        String brandName=brandDao.selectById(categoryBrandRelation.getBrandId()).getName();
        String catelogName=categoryDao.selectById(categoryBrandRelation.getCatelogId()).getName();
        //设置name
        categoryBrandRelation.setBrandName(brandName);
        categoryBrandRelation.setCatelogName(catelogName);
        //返回设置过name的关系对象
        this.save(categoryBrandRelation);
    }











   /**
   *
    *
    *   数据一致性问题的解决
    * 让brand或category调用修改时，对关系表也进行修改
    *
   *
   * */

    public void updateCategory(Long catelogId,String  catelogName){
        //获取所有关系对象
        List<CategoryBrandRelationEntity> entitiesAll=baseMapper.selectList(null);
        //使用stream过滤器过滤
        List<CategoryBrandRelationEntity> entities = entitiesAll.stream().filter(
                categoryBrandRelationEntity -> categoryBrandRelationEntity.getCatelogId().equals(catelogId)).filter(                            //id必须相同
                categoryBrandRelationEntity -> categoryBrandRelationEntity.getCatelogName()!=catelogName).collect(                              //name必须不同，否则视为未变更
                        Collectors.toList());
        //遍历，修改所有
        for(CategoryBrandRelationEntity entity:entities){
            entity.setCatelogName(catelogName);
            this.updateById(entity);
        }

    }
    public void updateBrand(Long brandId,String brandName){
        //获取所有关系对象
        List<CategoryBrandRelationEntity> entitiesAll=baseMapper.selectList(null);
        //使用stream过滤器过滤
        List<CategoryBrandRelationEntity> entities = entitiesAll.stream().filter(
                categoryBrandRelationEntity -> categoryBrandRelationEntity.getBrandId().equals(brandId)).filter(                                //id必须相同
                categoryBrandRelationEntity -> categoryBrandRelationEntity.getBrandName()!=brandName).collect(                          //name必须不同，否则视为未更改
                Collectors.toList());
        log.info("entities: "+entities.toString());
        //遍历，修改所有
        for(CategoryBrandRelationEntity entity:entities){
            entity.setBrandName(brandName);
            log.info(entities.toString());
            this.updateById(entity);
        }
    }

    public void deleteCategory(Long catelogId){
        //获取所有关系对象
        List<CategoryBrandRelationEntity> entitiesAll=baseMapper.selectList(null);
        //使用stream过滤器过滤
        List<CategoryBrandRelationEntity> entities = entitiesAll.stream().filter(
                categoryBrandRelationEntity -> categoryBrandRelationEntity.getCatelogId().equals(catelogId)).collect(Collectors.toList());
        //遍历，修改所有
        for(CategoryBrandRelationEntity entity:entities){
            baseMapper.deleteById(entity);
        }

    }
    public void deleteBrand(Long brandId){
        //获取所有关系对象
        List<CategoryBrandRelationEntity> entitiesAll=baseMapper.selectList(null);
        //使用stream过滤器过滤
        List<CategoryBrandRelationEntity> entities = entitiesAll.stream().filter(
                categoryBrandRelationEntity -> categoryBrandRelationEntity.getBrandId().equals(brandId)).collect(Collectors.toList());
        //遍历，修改所有
        for(CategoryBrandRelationEntity entity:entities){
            baseMapper.deleteById(entity);
        }
    }

















}