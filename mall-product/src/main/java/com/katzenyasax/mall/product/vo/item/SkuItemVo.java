package com.katzenyasax.mall.product.vo.item;

import com.katzenyasax.mall.product.entity.SkuImagesEntity;
import com.katzenyasax.mall.product.entity.SkuInfoEntity;
import com.katzenyasax.mall.product.entity.SpuInfoDescEntity;
import com.katzenyasax.mall.product.vo.spu.Attr;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuItemVo {
    //1、sku基本信息的获取  pms_sku_info
    private SkuInfoEntity info;

    private boolean hasStock = true;

    //2、sku的图片信息    pms_sku_images
    private List<SkuImagesEntity> images;

    //3、获取spu的销售属性组合
    private List<SkuItemSaleAttrVo> saleAttr;

    //4、获取spu的介绍
    private SpuInfoDescEntity desc;

    //5、获取spu的规格参数信息
    private List<SpuItemAttrGroupVo> groupAttrs;

    //6、秒杀商品的优惠信息
    private SeckillSkuVo seckillSkuVo;




    @Data
    public static class SkuItemSaleAttrVo {
        private Long attrId;

        private String attrName;

        private List<AttrValueWithSkuIdVo> attrValues;
    }

    @Data
    public static class AttrValueWithSkuIdVo {
        private String attrValue;

        private String skuIds;
    }

    @Data
    public static class SpuItemAttrGroupVo {
        private String groupName;

        private List<Attr> attrs;
    }




    @Data
    public static class SeckillSkuVo {
        /**
         * 活动id
         */
        private Long promotionId;
        /**
         * 活动场次id
         */
        private Long promotionSessionId;
        /**
         * 商品id
         */
        private Long skuId;
        /**
         * 秒杀价格
         */
        private BigDecimal seckillPrice;
        /**
         * 秒杀总量
         */
        private Integer seckillCount;
        /**
         * 每人限购数量
         */
        private Integer seckillLimit;
        /**
         * 排序
         */
        private Integer seckillSort;

        //当前商品秒杀的开始时间
        private Long startTime;

        //当前商品秒杀的结束时间
        private Long endTime;

        //当前商品秒杀的随机码
        private String randomCode;
    }






}
