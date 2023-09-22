package com.katzenyasax.mall.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.katzenyasax.common.constant.ProductConstant;
import com.katzenyasax.mall.product.dao.*;
import com.katzenyasax.mall.product.entity.*;
import com.katzenyasax.mall.product.service.AttrAttrgroupRelationService;
import com.katzenyasax.mall.product.service.CategoryService;
import com.katzenyasax.mall.product.vo.AttrVO_WithAttrGroupId;
import com.katzenyasax.mall.product.vo.AttrVO_WithGroupIdAndPaths;
import com.katzenyasax.mall.product.vo.AttrVO_WithGroupNameAndCatelogName;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {


    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private AttrGroupDao attrGroupDao;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductAttrValueDao productAttrValueDao;


    /**
     * @param params
     * @return
     *
     * mybatis plus生成的方法
     * 查询所有参数，不论attr_type
     *
     *
     */

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }









    /**
     *
     *
     * @param params
     * @param catelogId
     * @return
     *
     * 查询所有的普通参数
     * 查询所有attr_type为0或2的参数
     *
     *
     */
    @Override
    public PageUtils queryPageBase(Map<String, Object> params, Integer catelogId) {
        QueryWrapper<AttrEntity> wrapper=new QueryWrapper<AttrEntity>().and(obj0->obj0.eq(
                "attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()).or().eq(
                "attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BOTH.getCode()));

        if(!StringUtils.isEmpty((String)params.get("key"))){
            wrapper.and(obj->obj.like(
                    "attr_name",params.get("key")).or().like(
                    "value_select",params.get("key")));
        }
        if(catelogId!=0) {
            wrapper.and(obj->obj.eq("catelog_id",catelogId));
        }
        IPage<AttrEntity> page=this.page(new Query<AttrEntity>().getPage(params),wrapper);


        //接下来要添加上分组名和分类名
        //将查询好的AttrEntity们放入list
        List<AttrEntity> list=page.getRecords();
        List<AttrVO_WithGroupNameAndCatelogName> finale = list.stream().map(attrEntity -> {
            AttrVO_WithGroupNameAndCatelogName vo = new AttrVO_WithGroupNameAndCatelogName();
            //创建vo对象
            BeanUtil.copyProperties(attrEntity, vo);
            //将attrEntity的所有基本属性复制到vo

            //随后要为vo添加分组名和分类名
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            //通过已知的catelogId获取对应的category对象
            String catelogName = categoryEntity.getName();
            //通过category对象，直接获取catelogName
            vo.setCatelogName(catelogName);

            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
            //通过已知的attrId，获取属性和参数的关系对象
            if(attrAttrgroupRelationEntity!=null) {
            //一定要判断，否则如果查到attr_id在关系表内不存在的话就会报错而无法允许
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelationEntity.getAttrGroupId());
                //通过关系对象，获取对应的attrGroupId，并以此获取对应的attrGroup对象
                String groupName = attrGroupEntity.getAttrGroupName();
                //通过获取的attrGroup对象直接获取groupName
                vo.setGroupName(groupName);
            }
            return vo;
        }).collect(Collectors.toList());


        PageUtils pageUtils=new PageUtils(page);
        pageUtils.setList(finale);
        return pageUtils;
    }


    /**
     *

     * @param params
     * @param catelogId
     * @return
     *
     * 查询所有的销售属性
     * 查询所有attr_type为1或2的参数
     *
     */

    @Override
    public PageUtils queryPageSale(Map<String, Object> params, Integer catelogId) {
        QueryWrapper<AttrEntity> wrapper=new QueryWrapper<AttrEntity>().and(obj0->obj0.eq(
                "attr_type",ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()).or().eq(
                "attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BOTH.getCode()));
        if(!StringUtils.isEmpty((String)params.get("key"))){
            wrapper.and(obj->obj.like(
                    "attr_name",params.get("key")).or().like(
                    "value_select",params.get("key")));
        }
        if(catelogId!= ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()) {
            wrapper.and(obj->obj.eq("catelog_id",catelogId));
        }
        IPage<AttrEntity> page=this.page(new Query<AttrEntity>().getPage(params),wrapper);

        //接下来要添加上分组名和分类名
        //将查询好的AttrEntity们放入list
        List<AttrEntity> list=page.getRecords();
        List<AttrVO_WithGroupNameAndCatelogName> finale = list.stream().map(attrEntity -> {
            AttrVO_WithGroupNameAndCatelogName vo = new AttrVO_WithGroupNameAndCatelogName();
            //创建vo对象
            BeanUtil.copyProperties(attrEntity, vo);
            //将attrEntity的所有基本属性复制到vo

            //随后要为vo添加分组名和分类名
            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            //通过已知的catelogId获取对应的category对象
            String catelogName = categoryEntity.getName();
            //通过category对象，直接获取catelogName
            vo.setCatelogName(catelogName);
            return vo;
        }).collect(Collectors.toList());
        PageUtils pageUtils=new PageUtils(page);
        pageUtils.setList(finale);
        return pageUtils;
    }





    /**
     *
     *
     * @param attrVO
     *
     * 通过AttrVO_WithAttrGroupId新增参数
     * AttrVO_WithAttrGroupId是一个自定义vo，原型为AttrEntity
     * 但是逼AttrEntity多了一个groupId
     *
     * 作用从前端是接收数据
     *
     */

    @Override
    public void saveByAttrVO(AttrVO_WithAttrGroupId attrVO) {
        //通过AttrVO对象来存储新增参数
        //通过根据attrVO的attrGroupId来存储属性和参数的关系
        //1.存储attr
        AttrEntity attrEntity=new AttrEntity();
        BeanUtil.copyProperties(attrVO,attrEntity);     //将attrVO的同名数据复制到attrEntity，也即是除了attrGroupId
        this.save(attrEntity);                          //存储
        //2.存储关系
        Long attrGroupId=attrVO.getAttrGroupId();
        Long attrId=attrEntity.getAttrId();
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity=new AttrAttrgroupRelationEntity();
        attrAttrgroupRelationEntity.setAttrGroupId(attrGroupId);
        attrAttrgroupRelationEntity.setAttrId(attrId);
        attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
    }





    /**
     * @param attrId
     * @return
     *
     * 通过attrId查询信息（专供参数修改页面）
     * 查询的信息使用AttrVO_WithGroupIdAndPaths返回前端
     * 原型为AttrEntity，但是多了attrGroupId和path
     * 分别代表所属属性，和所属分类的完整路径
     *
     */
    @Override
    public AttrVO_WithGroupIdAndPaths getAttrWithGroupIdAndPath(Long attrId) {
        //思路是，先获取AttrEntity，将其复制到一个AttrVO_WithGroupIdAndPaths
        //然后通过attrId，直接查询catelogId，因为attrEntity里面自带了catelogId
        //之后直接调用方法获取完整路径
        //然后，如果不单独为销售属性，则从关系表中根据attr_id获取groupId
        AttrEntity attrEntity=this.getById(attrId);
        AttrVO_WithGroupIdAndPaths vo=new AttrVO_WithGroupIdAndPaths();
        BeanUtil.copyProperties(attrEntity,vo);
        //完成了数据的复制
        Long catelogId = vo.getCatelogId();
        Long[] path = getCategoryPath(catelogId);
        vo.setCatelogPath(path);
        //获取了catelogId的完整路径
        if(vo.getAttrType()!=ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()) {
            Long attrGroupId = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId)).getAttrGroupId();
            vo.setAttrGroupId(attrGroupId);
            //获取了attrGroupId
        }
        return vo;
    }


    /**
     * @param id
     * @return
     *
     * 根据一个第三级的商品分类，获取其完整路径
     * 调用方法的时CategoryService
     *
     */

    public Long[] getCategoryPath(Long id) {
        Long[] pathF=new Long[3];
        //最多三级，因此数组长度为3
        pathF[2]=id;
        //最后一个数就是该元素（孙子）的id
        CategoryEntity son = categoryService.getById(id);
        //儿子对象
        pathF[1]=son.getParentCid();
        //数组第二个元素为儿子的id
        CategoryEntity father=categoryService.getById(son.getParentCid());
        //父亲对象
        pathF[0]=father.getParentCid();
        //数组第一个元素为父亲的id
        return pathF;
        //直接返回
    }


    /**
     *
     * @param vo
     *
     * 修改参数信息，同时修改groupId
     * 也即是不仅更新一个AttrEntity，还要更新attr对应的groupId
     * 所以也需要用到attrAttrGroupRelationDao，调用其update方法
     *
     * 此外整个过程需要一同进行一同失败
     * 因此要加上事务
     * @Transictional
     *
     *
     */
    @Transactional
    @Override
    public void updateAttrWithGroupId(AttrVO_WithAttrGroupId vo) {
        //获取AttrEntity，用于保存
        AttrEntity attrEntity=new AttrEntity();
        BeanUtil.copyProperties(vo,attrEntity);
        this.updateById(attrEntity);

        //获取关系对象，并存入数据
        Long attrGroupId=vo.getAttrGroupId();
        Long attrId=vo.getAttrId();
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity=new AttrAttrgroupRelationEntity();
        attrAttrgroupRelationEntity.setAttrId(attrId);
        attrAttrgroupRelationEntity.setAttrGroupId(attrGroupId);

        //判断，如果欲修改的id在关系表中不存在，则说明参数原本并未和任何属性进行关联，故说明该操作其实是新增关系操作
        //若存在则为单词的修改关系操作
        //判断关系是否存在，使用selectCount方法
        Long count=attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",vo.getAttrId()));
        if(count!=0) {
            //如果已存在该关系的话，修改
            attrAttrgroupRelationDao.update(attrAttrgroupRelationEntity, new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
        }
        else {
            //若不存在该关系，新增
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }
    }



    /**
     *
     * @param spuId
     * @return
     *
     * 根据spuId查询与之关联的所有参数
     * 需要ProductAttrValueDao
     *
     */
    @Override
    public List<ProductAttrValueEntity> getSpuById(Long spuId) {

        List<ProductAttrValueEntity> finale=productAttrValueDao.selectList(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id",spuId));
        return finale;

    }






    /**
     * @param list,spuId
     * @return
     *
     * 更新参数
     *
     * 要根据传回的spuId和productAttrValueEntity集合
     * 更新这些productAttrValueEntity
     *
     */
    @Override
    public void updateSpuAttr(List<ProductAttrValueEntity> list, Long spuId) {
        //更新时，需要将原先的数据删除
        //删除也是根据attrId和spuId进行的
        //然后再保存新的


        for(ProductAttrValueEntity entity:list){
            Long attrId=entity.getAttrId();
            productAttrValueDao.delete(new QueryWrapper<ProductAttrValueEntity>().eq("attr_id",attrId).eq("spu_id",spuId));
            entity.setSpuId(spuId);
            productAttrValueDao.insert(entity);
        }

    }


}