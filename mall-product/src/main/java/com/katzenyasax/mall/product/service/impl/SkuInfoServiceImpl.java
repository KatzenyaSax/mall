package com.katzenyasax.mall.product.service.impl;

import cn.hutool.core.date.DateTime;
import com.katzenyasax.mall.product.dao.*;
import com.katzenyasax.mall.product.entity.*;
import com.katzenyasax.mall.product.vo.spu.BaseAttrs;
import com.katzenyasax.mall.product.vo.spu.SpuSaveVO;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.product.service.SkuInfoService;
import org.springframework.transaction.annotation.Transactional;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {


    @Autowired
    SpuInfoDao spuInfoDao;

    @Autowired
    SpuInfoDescDao spuInfoDescDao;

    @Autowired
    SpuImagesDao spuImagesDao;

    @Autowired
    ProductAttrValueDao productAttrValueDao;

    @Autowired
    AttrDao attrDao;


    /**
     *
     * @param params
     * @return
     *
     * mybatis plus自动生成的方法
     * 保存商品，接受的是前端发送的param
     *
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }


    /**
     *
     * @param vo
     *
     *
     * 保存有关SpuSaveVO对象
     * 该对象为生成的，接收商品数据的对象
     *
     * 专供商品维护/发布商品
     *
     * 该方法过长，使用@Transactional注解保证同步进行
     *
     */
    @Transactional
    @Override
    public void saveSpuVo(SpuSaveVO vo) {
        // 分为几个部分
        // 1.spu基本信息，存在spu_info中

        SpuInfoEntity spuInfoEntity=new SpuInfoEntity();
        BeanUtils.copyProperties(vo,spuInfoEntity);
        spuInfoEntity.setCreateTime(new DateTime());
        spuInfoEntity.setUpdateTime(new DateTime());
        spuInfoDao.insert(spuInfoEntity);

        // 2.spu描述图片，存在spu_info_desc

        SpuInfoDescEntity spuInfoDescEntity=new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
            //直接get spuInfoEntity的id
            //因为上面的方法先行执行，该spu已经得到了自增的id
        spuInfoDescEntity.setDecript(String.join(",",vo.getDecript()));
        spuInfoDescDao.insert(spuInfoDescEntity);

        // 3.spu图片集，存在spu_images

        //注意images是集合，需要遍历添加
        List<String> images=vo.getImages();
        for(String image:images) {
            SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
            spuImagesEntity.setSpuId(spuInfoEntity.getId());
            spuImagesEntity.setImgUrl(image);
            spuImagesDao.insert(spuImagesEntity);
        }

        // 4.spu的参数，存在product_attr_value

        List<BaseAttrs> baseAttrs=vo.getBaseAttrs();
        for(BaseAttrs attr:baseAttrs){
            ProductAttrValueEntity entity=new ProductAttrValueEntity();

            entity.setSpuId(spuInfoEntity.getId());
            entity.setAttrId(attr.getAttrId());
            entity.setAttrName(attrDao.selectOne(new QueryWrapper<AttrEntity>().eq("attr_id",attr.getAttrId())).getAttrName());
            entity.setAttrValue(attr.getAttrValues());
            entity.setQuickShow(attr.getShowDesc());

            productAttrValueDao.insert(entity);
        }


        // 5.spu的所有sku信息，包括：

            // 5.1.sku基本信息，存在sku_info

            // 5.2.sku的图片信息，存在sku_images

            // 5.3.sku的销售属性，存在sku_sale_attr_value

            //TODO
            // 5.4.sku的优惠、折扣信息，存在sms中
            //   具体而言，sku_ladder，存储折扣与购买商品件数的关系
            //   sku_full_reduction，存储满减的相关信息
            //   sku_bounds，存储积分
            //   这一部分就留在后面吧

















    }

}