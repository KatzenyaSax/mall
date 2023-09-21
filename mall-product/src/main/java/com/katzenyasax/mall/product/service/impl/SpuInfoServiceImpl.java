package com.katzenyasax.mall.product.service.impl;

import cn.hutool.core.date.DateTime;
import com.katzenyasax.common.to.SkuFullReductionTO;
import com.katzenyasax.common.to.SpuBoundsTO;
import com.katzenyasax.mall.product.dao.*;
import com.katzenyasax.mall.product.entity.*;
import com.katzenyasax.mall.product.feign.CouponFeign;
import com.katzenyasax.mall.product.vo.spu.*;
import com.aliyuncs.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.product.service.SpuInfoService;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {


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

    @Autowired
    SkuImagesDao skuImagesDao;

    @Autowired
    SkuInfoDao skuInfoDao;

    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Autowired
    CouponFeign couponFeign;








    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
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
    //TODO
    //  该方法还有一些保存失败的可能性
    //  例如事务是否回滚？等等
    //  就放在以后看吧


    @Transactional
    @Override
    public void saveSpuVo(SpuSaveVO vo) {
        /** 分为几个部分
         // 1.spu基本信息，存在spu_info中 */

        SpuInfoEntity spuInfoEntity=new SpuInfoEntity();
        BeanUtils.copyProperties(vo,spuInfoEntity);
        spuInfoEntity.setCreateTime(new DateTime());
        spuInfoEntity.setUpdateTime(new DateTime());
        spuInfoDao.insert(spuInfoEntity);

        /** 2.spu描述图片，存在spu_info_desc */

        SpuInfoDescEntity spuInfoDescEntity=new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        //直接get spuInfoEntity的id
        //因为上面的方法先行执行，该spu已经得到了自增的id
        spuInfoDescEntity.setDecript(String.join(",",vo.getDecript()));
        spuInfoDescDao.insert(spuInfoDescEntity);

        /** 3.spu图片集，存在spu_images */

        //注意images是集合，需要遍历添加
        List<String> images=vo.getImages();
        for(String image:images) {
            SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
            spuImagesEntity.setSpuId(spuInfoEntity.getId());
            spuImagesEntity.setImgUrl(image);
            spuImagesDao.insert(spuImagesEntity);
        }

        /** 4.spu的参数，存在product_attr_value */

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


        /** 5.spu的所有sku信息，包括：
         // sku全部保存在vo的sku集合内：*/

        List<Skus> skus=vo.getSkus();

        for(Skus sku:skus) {
            /** 5.1.sku基本信息，存在sku_info */

            SkuInfoEntity skuInfoEntity=new SkuInfoEntity();
            BeanUtils.copyProperties(sku,skuInfoEntity);
            //只复制了skuName/skuTitle/skuSubtitle/price过去
            skuInfoEntity.setSpuId(spuInfoEntity.getId());
            //spu id
            skuInfoEntity.setSkuDesc(String.join(",",sku.getDescar()));
            //描述
            skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
            //brand id
            skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
            //catelog id
            List<Images> sku_images=new ArrayList<>();
            //这个sku_images是下面5.2的内容，现在趁着找default_image的功夫一起弄了
            Images default_sku_image=new Images();
            for(Images image: sku.getImages()){
                if(image.getDefaultImg()==1){
                    default_sku_image=image;
                }
                else if(!StringUtils.isEmpty(image.getImgUrl())){
                    sku_images.add(image);
                }
            }
            skuInfoEntity.setSkuDefaultImg(default_sku_image.getImgUrl());
            //default image
            skuInfoEntity.setSaleCount(0L);
            //手动设置折扣为0
            skuInfoDao.insert(skuInfoEntity);

            /** 5.2.sku的图片信息，存在sku_images */

            for(Images image:sku_images) {
                SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                skuImagesEntity.setSkuId(skuInfoEntity.getSkuId());
                skuImagesEntity.setImgUrl(image.getImgUrl());
                skuImagesEntity.setDefaultImg(image.getDefaultImg());
                skuImagesDao.insert(skuImagesEntity);
            }
            SkuImagesEntity defaultSkuImagesEntity=new SkuImagesEntity();
            defaultSkuImagesEntity.setSkuId(skuInfoEntity.getSkuId());
            defaultSkuImagesEntity.setImgUrl(default_sku_image.getImgUrl());
            defaultSkuImagesEntity.setDefaultImg(default_sku_image.getDefaultImg());
            skuImagesDao.insert(defaultSkuImagesEntity);

            /** 5.3.sku的销售属性，存在sku_sale_attr_value */
            for(Attr attr: sku.getAttr()){
                SkuSaleAttrValueEntity entity=new SkuSaleAttrValueEntity();
                entity.setSkuId(skuInfoEntity.getSkuId());
                entity.setAttrId(attr.getAttrId());
                entity.setAttrName(attr.getAttrName());
                entity.setAttrValue(attr.getAttrValue());
                skuSaleAttrValueDao.insert(entity);
            }



            /** 5.4.sku的优惠、折扣信息，存在sms中
             具体而言，sku_ladder，存储折扣与购买商品件数的关系
             sku_full_reduction，存储满减的相关信息
             sku_bounds，存储积分
             需要用到远程调用
             定义一个CouponFeign作为和coupon服务远程调用的接口
             */


            // 5.4.1，先试一下spu bounds
            Bounds bound=vo.getBounds();
            BigDecimal buyBound=bound.getBuyBounds();
            BigDecimal growBound=bound.getGrowBounds();
            //spu id:spuInfoEntity.getSpuId();
            //现在已经有了bounds需要的参数，接下来要往coupon服务里传
            //所以product和coupon都需要一个相同的对象，分别进行数据的传输和接收
            //将该对象定义在common里面：
            SpuBoundsTO spuBoundsTO=new SpuBoundsTO();
            spuBoundsTO.setSpuId(spuInfoEntity.getId());
            spuBoundsTO.setBuyBounds(buyBound);
            spuBoundsTO.setGrowBounds(growBound);
            couponFeign.saveBounds(spuBoundsTO);

            //此后couponFeign将调用coupon/spubounds/save这一路径的方法，发送的参数为一个to
            //故coupon模块中也需要将接收参数改成to
            //同时还要自定义方法，用于处理to的数据并存储



            //5.4.2，满减信息sku_full_reduction
            //定义SkuFullReductionTO，包含的是所有优惠满减信息，也包括ladder、member price
            //把MemberPrice类拷贝也到to包里面，加入到满减to的成员变量内
            //随后调用feign，feign中应当有一个方法用于让coupon模块保存满减to
            //注意需要存到full reduction、ladder和member price表内



            SkuFullReductionTO skuFullReductionTO=new SkuFullReductionTO();
            BeanUtils.copyProperties(sku,skuFullReductionTO);
            skuFullReductionTO.setSkuId(skuInfoEntity.getSkuId());
            skuFullReductionTO.setFullCount(sku.getFullCount());
            skuFullReductionTO.setCountStatus(sku.getCountStatus());
            //如果sku内满减fullReduction和discount都为0，则不发起远程请求，也即是不需要添加至表内
            //只有二者有一个有意义，那就说明该优惠价有意义，加入表内
            if(skuFullReductionTO.getFullCount().compareTo(BigDecimal.ZERO)==1 || skuFullReductionTO.getDiscount().compareTo(BigDecimal.ZERO)==1) {
                couponFeign.saveFullReduction(skuFullReductionTO);
            }


        }
    }

}