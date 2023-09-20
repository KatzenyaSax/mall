package com.katzenyasax.mall.product.service.impl;

import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.katzenyasax.mall.product.dao.AttrAttrgroupRelationDao;
import com.katzenyasax.mall.product.dao.AttrDao;
import com.katzenyasax.mall.product.entity.AttrAttrgroupRelationEntity;
import com.katzenyasax.mall.product.entity.AttrEntity;
import com.katzenyasax.mall.product.entity.CategoryEntity;
import com.katzenyasax.mall.product.service.CategoryService;
import com.katzenyasax.mall.product.vo.AttrAttrGroupVO_JustReceiveData;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Attr;

@Slf4j
@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {


    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    private AttrDao attrDao;


    /**
     *
     * @param params
     * @return
     *
     *
     * mybatis plus自动生成的方法
     * 默认查找全部属性
     *
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }





    /**
     *
     * @param params
     * @param catelogId
     * @return
     *
     * 重载的queryPage方法，用于查找属性
     * 返回值为一个page对象，储存的是属性对象
     *
     * 如果catelogId为0，查找全部
     * 否则查找对应catelogId的值
     *
     */
    @Override
    public PageUtils getGroupWithId(Map<String, Object> params, Integer catelogId) {
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





    /**
     *
     * @param Id
     * @return
     *
     *
     *
     * 这是调用的CategoryService中的方法
     * 该方法通过一个categoryId，获取其在三级分类中的完整路径
     *
     *
     */
    @Override
    public Long[] getPath(Long Id) {
        CategoryService categoryService=new CategoryServiceImpl();
        return categoryService.getCategoryPath(Id);
        //直接调用Category的方法返回
    }


    /**
     *
     * @param attrGroupId
     * @return
     *
     * 查询所有和属性已关联的参数
     *
     * 专供属性的关联界面
     *
     * 返回值为AttrEntity的List集合
     *
     *
     */
    @Override
    public List<AttrEntity> getAttrRelatedWithGroup(Integer attrGroupId) {
        //思路是，先在关系表中查询所有相关对象relations，封装
        // 再遍历relations遍历获取attrId，不需要封装，直接在遍历中就获取attr集合封装

        List<AttrAttrgroupRelationEntity> relations=attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id",attrGroupId));
        //获取了所有的关系对象
        List<AttrEntity> attrS=new ArrayList<>();
        for(AttrAttrgroupRelationEntity entity:relations){
            attrS.add(attrDao.selectById(entity.getAttrId()));
        }
        //获取了所有的attrIds

        return attrS;
    }



    /**
     *
     * @param attrGroupId
     * @return
     *
     * 查询所有和属性未发生关联关联的参数
     *
     * 专供属性的新增关联界面
     *
     * 返回值为AttrEntity的Page集合
     *
     *
     */
    @Override
    public PageUtils getAttrRelatedNOTWithGroup(@RequestParam Map<String, Object> params,Integer attrGroupId) {
        //首先得到groupEntity，别问先做
        //思路是，先用关系的dao，获取所有与groupId关联的关系对象，封装为relations
        //遍历关系对象的集合，attrId，封装成集合List
        //然后直接使用attrDao，根据param直接获取所有参数，这些参数的catelogId要和groupEntity的catelogId一致
        //在所有参数中剔除attrId在集合List内的参数，就得到了所有未关联的参数
        //0.获取该groupId对应的group实体
        AttrGroupEntity groupEntity=this.getById(attrGroupId);
        //1.获取所有与groupId关联的关系对象
        List<AttrAttrgroupRelationEntity> relations=attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id",attrGroupId));
        //2.封装attrId
        List<Long> attrIds=new ArrayList<>();
        for(AttrAttrgroupRelationEntity entity:relations){
            attrIds.add(entity.getAttrId());
        }
        //3.获取全部参数，前提是要处于同一分类
        //4.剔除处于attrIds的所有attrId，
        // 但是要获取的是一个wrapper，也即是一个方案，后面直接根据该方案查询参数
        QueryWrapper<AttrEntity> wrapper=new QueryWrapper<AttrEntity>().eq("catelog_id",groupEntity.getCatelogId()).notIn("attr_id",attrIds);
        //4.1如果有模糊关键字key，还有匹配模糊搜索
        String key=(String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and((obj)->{obj.like("attr_id",key).or().like("attr_name",key);});
        }
        //5.根据params和wrapper，获取page对象
        IPage page=attrDao.selectPage(new Query<AttrEntity>().getPage(params),wrapper);
        PageUtils finale=new PageUtils(page);
        return finale;
    }





    /**
     *
     * @param vos
     *
     * 接收的是vo的集合
     * 根据vo的集合内数据新增属性和参数关系
     *
     * 因此注入了attrAttrGroupRelationDao
     *
     */

    @Override
    public void addRelation(List<AttrAttrGroupVO_JustReceiveData> vos) {
        //思路是，遍历vos，创建关系对象传入每个vo的数据
        //将每个数据根据关系的dao，进行存入
        for(AttrAttrGroupVO_JustReceiveData vo:vos){
            AttrAttrgroupRelationEntity relation=new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(vo,relation);
            attrAttrgroupRelationDao.insert(relation);
        }
    }


}