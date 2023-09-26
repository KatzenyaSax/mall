package com.katzenyasax.mall.product.service.impl;

import com.katzenyasax.mall.product.vo.catalogVO.Catalog2VO;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        /*//查出所有分类
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
        }*/


        //查出所有一级菜单：
        List<CategoryEntity> listI=this.listOne();
        return listI.stream().map(
                I->{
                    List<CategoryEntity> listII=baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid",I.getCatId()));
                    List<CategoryEntity> ii = listII.stream().map(
                            II->{
                                List<CategoryEntity> listIII=baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid",II.getCatId()));
                                List<CategoryEntity> iii = listIII.stream().map(
                                        III -> {
                                            III.setChildren(null);
                                            return III;
                                        }
                                ).collect(Collectors.toList());
                                II.setChildren(iii);
                                return II;
                            }
                    ).collect(Collectors.toList());
                    I.setChildren(ii);
                    return I;
                }
        ).collect(Collectors.toList());
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


    /**
     * @return
     *
     * 查出所有的一级分类
     * 返回一级分类实体的集合
     *
     */
    @Override
    public List<CategoryEntity> listOne() {
        //查出所有分类
        List<CategoryEntity> entities=baseMapper.selectList(null);
        //获取一级子类
        List<CategoryEntity> oneCategory=entities.stream().filter(categoryEntity -> categoryEntity.getCatLevel()==1).toList();
        return oneCategory;
    }




    /**
     *
     * @return
     *
     * 查询一级分类下所有的二三级菜单，返回一级分类的Map
     *
     * 思路是：
     * 遍历一级菜单：
     * {
     *      遍历到单个一级菜单时，获取其所有二级菜单
     *      遍历二级菜单：
     *      {
     *          遍历到单个二级菜单时，获取其所有三级菜单
     *          遍历三级菜单：
     *           {
     *               将三级菜单信息封装
     *               将封装信息返回上一级，作为本单个二级菜单下三级菜单集合的一部分
     *           }
     *           将遍历结果封装为集合
     *           将封装信息返回上一级，作为本单个一级菜单下二级菜单集合的一部分
     *      }
     *      将遍历结果封装为集合
     *      将封装信息直接赋给本单个一级菜单，作为本当一级菜单下二级菜单集合
     * }
     */
    @Override
    public Map<String, List<Catalog2VO>> getCatalogJson() {
        //查出所有一级菜单：
        List<CategoryEntity> listI=this.listOne();
        Map<String, List<Catalog2VO>> finale = listI.stream().collect(Collectors.toMap(
                k -> k.getCatId().toString(),
                //遍历到单个一级菜单
                I -> {
                    //查出该一级菜单下所有二级菜单：
                    List<CategoryEntity> listII = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", I.getCatId()));
                    List<Catalog2VO> catalogII = listII.stream().map(
                            //遍历到单个二级菜单
                            II -> {
                                //查出该二级菜单下所有三级菜单：
                                List<CategoryEntity> listIII = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", II.getCatId()));
                                List<Catalog2VO.Catalog3VO> catalogIII = listIII.stream().map(
                                        //遍历到单个三级菜单
                                        III -> {
                                            Catalog2VO.Catalog3VO iii = new Catalog2VO.Catalog3VO();
                                            //iii是该三级菜单的封装对象
                                            iii.setCatalog2Id(II.getCatId().toString());
                                            iii.setId(III.getCatId().toString());
                                            iii.setName(III.getName());
                                            return iii;
                                        }
                                ).collect(Collectors.toList());
                                //此时catalogIII就是该二级菜单下面的所有三级菜单

                                Catalog2VO ii = new Catalog2VO();
                                //ii是该二级菜单的封装对象
                                ii.setCatalog1Id(I.getCatId().toString());
                                ii.setId(II.getCatId().toString());
                                ii.setName(II.getName());
                                ii.setCatalog3List(catalogIII);
                                return ii;
                            }
                    ).collect(Collectors.toList());
                    //此时catalogII就是一级菜单下的所有二级菜单

                    return catalogII;
                }
        ));
        return finale;
    }



}