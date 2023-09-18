package com.katzenyasax.mall.product.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
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
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }


    /**
     * @return
     *
     *
     * 获取商品分类
     * 按照树形三级分类
     *
     *
     */

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




    //逻辑删除
    @Override
    public void hideByIds(List<Long> list) {
        //TODO 1.判断数据是否被引用
        //2.隐藏数据
        //  直接删除就可以了
        baseMapper.deleteBatchIds(list);
    }





    //用于排序
    @Override
    public void Sort(CategoryEntity[] category) {

    }


    /**
     * @param id
     * @return
     *
     * 回显分类的完整路径
     * （专供品牌修改的数据回显）
     *
     *
     * 可重复使用
     *
     */


    public Long[] getCategoryPath(Long id) {
        Long[] pathF=new Long[3];
        //最多三级，因此数组长度为3
        pathF[2]=id;
        //最后一个数就是该元素（孙子）的id
        CategoryEntity son = this.getById(id);
        //儿子对象
        pathF[1]=son.getParentCid();
        //数组第二个元素为儿子的id
        CategoryEntity father=this.getById(son.getParentCid());
        //父亲对象
        pathF[0]=father.getParentCid();
        //数组第一个元素为父亲的id
        return pathF;
        //直接返回
    }




}