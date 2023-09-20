package com.katzenyasax.mall.product.service.impl;

import com.katzenyasax.mall.product.dao.BrandDao;
import com.katzenyasax.mall.product.dao.CategoryDao;
import com.katzenyasax.mall.product.entity.CategoryEntity;
import com.katzenyasax.mall.product.service.BrandService;
import com.katzenyasax.mall.product.service.CategoryService;
import com.katzenyasax.mall.product.vo.BrandVO_OnlyIdAndName;
import com.katzenyasax.mall.product.vo.CategoryVO_OnlyIdAndName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
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

    @Autowired
    BrandDao brandDao;
    @Autowired
    CategoryDao categoryDao;


    @Autowired
    CategoryBrandRelationDao categoryBrandRelationDao;


    /**
     *
     * @param params
     * @return
     *
     * mybatis plus自动生成的方法
     * 获取所有关系对象
     *
     */
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
     * @param catelogId
     * @return
     *
     * 通过一个catelogId，查询所有与之关联的brand
     * 需要用到分类和品牌的dao，也就是本家的dao
     *
     */
    @Override
    public List<BrandVO_OnlyIdAndName> selectBrandsThatRelatedWithCatelogId(Long catelogId) {
        //思路是，先从关系表中查询所有与catelogId关联的关系对象，封装为关系对象的集合
        //随后遍历集合，每次遍历都拿取id和name封装至vo，加入返回值的集合finale
        List<CategoryBrandRelationEntity> relations=categoryBrandRelationDao.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id",catelogId));
        List<BrandVO_OnlyIdAndName> finale=new ArrayList<>();
        for(CategoryBrandRelationEntity entity:relations){
            Long id=entity.getBrandId();
            String name=entity.getBrandName();
            BrandVO_OnlyIdAndName vo=new BrandVO_OnlyIdAndName();
            vo.setBrandId(id);
            vo.setBrandName(name);
            finale.add(vo);
        }
        return finale;
    }








    /**
     *
     * @param brandId
     * @return
     *
     * 通过一个brandId，查询所有与之关联的category
     * 需要用到分类和品牌的dao，也就是本家的dao
     *
     *
     *
     *
     *
     *
     */
    @Override
    public List<CategoryVO_OnlyIdAndName> selectCategoriesThatRelatedWithBrand(Long brandId) {
        //思路查询brand的一模一样
        List<CategoryBrandRelationEntity> relations=categoryBrandRelationDao.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("brand0_id",brandId));
        List<CategoryVO_OnlyIdAndName> finale=new ArrayList<>();
        for(CategoryBrandRelationEntity entity:relations){
            Long id=entity.getBrandId();
            String name=entity.getBrandName();
            CategoryVO_OnlyIdAndName vo=new CategoryVO_OnlyIdAndName();
            vo.setCatelogId(id);
            vo.setCatelogName(name);
            finale.add(vo);
        }
        return finale;
    }












    /**
   *
    *
    *   数据一致性问题的解决
    *   让brand或category修改，可能是修改名字也有可能是删除时，对关系表也进行修改
    *
    *
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