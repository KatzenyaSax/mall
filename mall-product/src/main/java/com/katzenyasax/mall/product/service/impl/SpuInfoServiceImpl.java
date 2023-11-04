package com.katzenyasax.mall.product.service.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.katzenyasax.common.to.SkuEsModel;
import com.katzenyasax.common.to.SkuFullReductionTO;
import com.katzenyasax.common.to.SpuBoundsTO;
import com.katzenyasax.common.to.SpuInfoTO;
import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.product.config.ThisThreadPool;
import com.katzenyasax.mall.product.dao.*;
import com.katzenyasax.mall.product.entity.*;
import com.katzenyasax.mall.product.feign.CouponFeign;
import com.katzenyasax.mall.product.feign.SearchFeign;
import com.katzenyasax.mall.product.feign.WareFeign;
import com.katzenyasax.mall.product.vo.item.SkuItemSaleAttrVo;
import com.katzenyasax.mall.product.vo.item.SkuItemVo;
import com.katzenyasax.mall.product.vo.spu.*;
import com.aliyuncs.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.product.service.SpuInfoService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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

    @Autowired
    BrandDao brandDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    ThisThreadPool threadPool;




    /**
     *
     * @param params
     * @return
     *
     * mybatis plus自动生成的方法
     * 查询所有spuInfoEntity
     */
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
        spuInfoEntity.setPublishStatus(0);
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
            if(skuFullReductionTO.getFullCount().compareTo(BigDecimal.ZERO) > 0 || skuFullReductionTO.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
                couponFeign.saveFullReduction(skuFullReductionTO);
            }


        }
    }


    /**
     *
     * @param params
     * @return
     *
     * 查询符合条件的SpuInfoEntity并封装为page返回
     *
     * 包括模糊查询
     */
    @Override
    @Transactional
    public PageUtils getSpuInfo(@NotNull Map<String, Object> params) {
        //params里面有key、catelogId、brandId、status

        QueryWrapper<SpuInfoEntity> wrapper=new QueryWrapper<SpuInfoEntity>();
        String key=(String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            log.info("key not null: "+key);
            wrapper.and(w->{
               w.like("spu_name",key).or().like("spu_description",key).or().like("id",key);
            });
        }
        String  catelogId=(String) params.get("catelogId");
        if(catelogId!=null && !"0".equalsIgnoreCase(catelogId)){
            log.info("catelogId not null: "+catelogId);
            wrapper.eq("catalog_id",catelogId);
        }
        String  brandId=(String) params.get("brandId");
        if(brandId!=null && !"0".equalsIgnoreCase(brandId)){
            log.info("brandId not null: "+brandId);
            wrapper.eq("brand_id",brandId);
        }

        String  status=(String) params.get("status");
        if(!StringUtils.isEmpty(status)) {
            log.info("status not null: "+status);
            wrapper.eq("publish_status", status);
        }

        IPage<SpuInfoEntity> finale = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(finale);

    }


    /**
     *
     * @param spuId
     *
     * 根据spuId上架商品
     *
     * 功能拓展：要求上架时，将该spu的所有信息、和spuId对应的所有sku存储至es
     *
     */
    @Autowired
    WareFeign wareFeign;
    @Autowired
    SearchFeign searchFeign;
    @Override
    @Transactional
    public void upSpu(Long spuId) {
        SpuInfoEntity entity=baseMapper.selectById(spuId);
        entity.setPublishStatus(1);
        baseMapper.updateById(entity);

        /**
         * 接下来进行存储至es
         */
        //1.查出所有sku
        List<SkuInfoEntity> skus=skuInfoDao.selectList(new QueryWrapper<SkuInfoEntity>().eq("spu_id",spuId));

        /*2.处理attrs
         * 注意在这个spu下，每个sku对应的attrs都是一样的
         * 所以只需要查一遍，获取attrs后直接赋给所有sku就行
         * 并且还要注意，查的应该是search_type=1，即可以用于查询的attr
         * 在product_attr_value表中查，里面有attr和spu_id的对应关系，还有AttrEsModel要用的所有东西
         */
        List<ProductAttrValueEntity> attrEntities=this.getAttrThatCanBeSearchedBySpuId(spuId);
        List<SkuEsModel.Attrs> attrs=new ArrayList<>();
        //复制
        if((attrEntities!=null&&!attrEntities.isEmpty())) {
            //当spuId没有关联的sku时，attr直接为空集合
            for (ProductAttrValueEntity attrEntity : attrEntities) {
                SkuEsModel.Attrs attr = new SkuEsModel.Attrs();
                BeanUtils.copyProperties(attrEntity, attr);
                attrs.add(attr);
            }
        }
        System.out.println(JSON.toJSONString(attrs));

        //3.封装信息
        List<SkuEsModel> skuEsModels=new ArrayList<>();
        for (SkuInfoEntity info : skus) {
            SkuEsModel model=new SkuEsModel();
            /**
             * 拷贝数据
             * 不同的字段为：
             * skuPrice     price
             * skuImg       skuDefaultImg
             *
             * 缺失的字段：
             * hasStock，关联spu的stock
             * hotScore，根据点击率获取，后端不管
             * brandName，根据brandId查
             * brandImg，根据brandId查
             * catalogName，根据catalogId查
             *
             */
            //3.1复制对象
            BeanUtils.copyProperties(info,model);

            //3.2处理不同名称字段
            model.setSkuPrice(info.getPrice());
            model.setSkuImg(info.getSkuDefaultImg());

            //3.3处理缺失字段
            model.setBrandName(brandDao.selectById(model.getBrandId()).getName());
            model.setCatalogName(categoryDao.selectById(model.getCatalogId()).getName());
            model.setBrandImg(brandDao.selectById(model.getBrandId()).getLogo());
                //TODO 热度评分需要更复杂的操作
                //  目前就给设置个0得了
            model.setHotScore(0L);
            //需要远程调用ware了：通过sku_id在ware的数据库的ware_sku中查询stock；若stock不为空，且有一个键值对的value不为0，则hasStock设置为true
            model.setHasStock(false);
            Map<Long, Integer> stock = wareFeign.getStockBySkuId(model.getSkuId());
            if (!stock.isEmpty() && stock != null) {
                for (Map.Entry<Long, Integer> entry : stock.entrySet()) {
                    if (entry.getValue() != 0) {
                        model.setHasStock(true);
                        break;
                    }
                }
            }


            //3.4处理attrs
            //注意每一个sku的attrs都是在一个
            model.setAttrs(attrs);

            //3.5将该循环内的model加入skuEsModels中sk
            skuEsModels.add(model);
        }
        System.out.println(JSON.toJSONString(skuEsModels));

        //4.远程添加至es
        R r=searchFeign.SkuUp(skuEsModels);
        System.out.println(r.toString());

        //TODO 一些高级问题如何解决？例如接口幂等性、抑或是open feign的重试机制？

    }



    /**
     *
     * @param spuId
     *
     * 根据spuId下架商品
     *
     *
     */
    @Override
    public void downSpu(Long spuId) {
        SpuInfoEntity entity=baseMapper.selectById(spuId);
        entity.setPublishStatus(2);
        baseMapper.updateById(entity);
    }


    /**
     *
     * @param skuId
     * @return finale
     *
     *
     * 根据skuId，获取符合详情页的所有内容
     * 返回值为一个SkuItemVo对象
     *
     *
     */
    @Override
    public SkuItemVo getSkuItem(String skuId) {
        System.out.println("SpuInfoService: getSkuItem");
        //结果封装
        SkuItemVo finale=new SkuItemVo();

        CompletableFuture<SkuInfoEntity> thread1=CompletableFuture.supplyAsync(()->{
            //1.sku基本信息，直接通过mapper和skuId获取
            SkuInfoEntity skuInfo=skuInfoDao.selectById(skuId);
            finale.setInfo(skuInfo);
            return skuInfo;
        });

        CompletableFuture<Void> thread2=CompletableFuture.runAsync(()->{
            //2.图片信息
            finale.setImages(skuImagesDao
                    .selectList(new QueryWrapper<SkuImagesEntity>()
                            .eq("sku_id", skuId)
                    )
            );
        },threadPool.TPE());

        CompletableFuture<Void> thread3 = thread1.thenAcceptAsync(res -> {
            //3.spu销售信息组合
            List<SkuItemVo.SkuItemSaleAttrVo> saleAttr = this.getSkuItemSaleAttrVo(res.getSpuId());
            finale.setSaleAttr(saleAttr);
        }, threadPool.TPE());

        CompletableFuture<Void> thread4 = thread1.thenAcceptAsync(res -> {
            //4.spu介绍
            finale.setDesc(spuInfoDescDao
                    .selectById(res.getSpuId())
            );
        }, threadPool.TPE());

        CompletableFuture<Void> thread5 = thread1.thenAcceptAsync(res -> {
            //5.spu规格参数
            List<SkuItemVo.SpuItemAttrGroupVo> groupAttrs = this.getSpuItemAttrGroupVo(res.getSpuId());
            finale.setGroupAttrs(groupAttrs);
        }, threadPool.TPE());

        //6.商品的优惠信息
        // TODO 远程调用coupon模块，获取优惠信息

        //判断1-6的线程是否完成：
        CompletableFuture.allOf(
                thread1
                ,thread2
                ,thread3
                ,thread4
                ,thread5
        );

        System.out.println(JSON.toJSONString(finale));
        return finale;
    }




    /**
     * =====================================================================================
     */


    /**
     * 非接口方法
     * 根据spuId查询所有可检索的attr
     * 即searchType=1的所有attr
     * 返回AttrEntity的List
     */
    public List<ProductAttrValueEntity> getAttrThatCanBeSearchedBySpuId(Long spuId){
        //获取与spuId关联、且searchType为1的PAVE
        //获取所有的attrId，根据spuId，从pav表中
        List<Long> attrIds=new ArrayList<>();
        for (ProductAttrValueEntity entity : productAttrValueDao.selectList(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId))) {
            attrIds.add(entity.getAttrId());
        }
        //构造内容物为空的返回值
        List<ProductAttrValueEntity> finale=new ArrayList<>();
        //遍历attrIds
        //遍历时，根据attrId查询attrEntity，若发现search_id为1，则通过该attrId获取pave，并加入返回值
        for(Long attrId:attrIds){
            if(attrDao.selectById(attrId).getSearchType()==1){
                ProductAttrValueEntity entity=productAttrValueDao.selectOne(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id",spuId).and(
                        w->w.eq("attr_id",attrId)));
                finale.add(entity);
            }
        }

        return finale;

    }




    /**
     *
     *
     * @param spuId
     * @return
     *
     * 根据spuId，返回所有与之相关的Sku、销售属性的vo
     *      1.0版本：销售属性之间自由组合，但是不能映射到确定的一个sku
     *      2.0版本：销售属性后面封装与之相关的skuIds，返回前端统一处理
     *
     *
     */
    private List<SkuItemVo.SkuItemSaleAttrVo> getSkuItemSaleAttrVo(Long spuId) {

        /**
         * 根据spuId来查，spuId对应多个skuId
         * 有且只有一个spu
         *
         * 大致思路是，
         *      1.通过skuId获取spuId（skuInfo）
         *          1.1.再从spuId获取对应的所有skuId_RelatingSpu（pms_sku_info）
         *          1.2.通过spuId获取所有spu对应的attr_RelatingSpu（pms_product_attr_value）
         *              1.2.1.通过遍历attr_RelatingSpu，获取所有关联spu的saleAttr_RelatingSpu（pms_sku_sale_attr_value）
         *
         */

        //所有的销售属性和sku的关联表
        List<SkuSaleAttrValueEntity> relation_skuSale_attrValue=skuSaleAttrValueDao.selectList(null);

        //1.1.spu关联的所有的skuId_RelatingSpu
        List<Long> skuIds_RelatingSpu=skuInfoDao.selectList(
                        new QueryWrapper<SkuInfoEntity>().eq(
                                "spu_id",spuId)
                ).stream()
                .map(e-> e.getSkuId())
                .collect(Collectors.toList());


        //所有属性AllAttrs
        //因为该spu对应的attrId并非只有一个，为了避免循环查库，这里直接查询所有attr
        List<AttrEntity> AllAttrs=attrDao.selectList(null);

        //与spu对应的所有属性attr_RelatingSpu
        //在pms_product_attr_value中查询，spuId和attrId为一对多的关系，因此这里直接查询了关联spuId的所有attr
        List<Long> attr_RelatingSpu=productAttrValueDao.selectList(
                        new QueryWrapper<ProductAttrValueEntity>()
                                .eq("spu_id",spuId)
                )
                .stream().map(e->{
                            return e.getAttrId();
                        }
                )
                .collect(Collectors.toList());


        //1.2.1与spu对应的所有销售属性saleAttr_RelatingSpu
        //从所有属性AllAttr中查询，主要条件为attrId必须与spu关联（即包含于attr_RelatingSpu）、且必须是销售属性（即attrType不为0）
        List<AttrEntity> saleAttr_RelatingSpu=new ArrayList<>();
        AllAttrs.forEach(entity->{
            if(attr_RelatingSpu.contains(entity.getAttrId()) && entity.getAttrType()!=1){
                saleAttr_RelatingSpu.add(entity);
            }
        });

        //遍历与spu关联的销售属性saleAttr_RelatingSpu
        List<SkuItemVo.SkuItemSaleAttrVo> saleAttr = saleAttr_RelatingSpu.stream().map(sale -> {
            //该遍历下，每个当前元素都是大集合的单个元素

            //当前元素封装对象vo
            SkuItemVo.SkuItemSaleAttrVo vo = new SkuItemVo.SkuItemSaleAttrVo();

            //vo的两个单字成员变量
            vo.setAttrId(sale.getAttrId());
            vo.setAttrName(sale.getAttrName());

            //vo的一个集合成员变量初始化
            vo.setAttrValues(new ArrayList<>());


            //存储attrValue的集合，用于判断去重
            List<String> templeValue=new ArrayList<>();
            //遍历所有sku和销售属性的关系
            relation_skuSale_attrValue.forEach(relation -> {
                //确保attrValue不重复
                if(!templeValue.contains(relation.getAttrValue())) {
                    //skuId包含于指定的集合内、且attrId为当前遍历销售属性时，新添加参数设置
                    if (skuIds_RelatingSpu.contains(relation.getSkuId()) && relation.getAttrId() == sale.getAttrId()) {
                        //小集合单个元素
                        SkuItemVo.AttrValueWithSkuIdVo skuValue = new SkuItemVo.AttrValueWithSkuIdVo();
                        //小元素设置成员变量
                        skuValue.setSkuIds(relation.getSkuId().toString());
                        skuValue.setAttrValue(relation.getAttrValue());
                        //小元素添加到当前vo的集合变量
                        vo.getAttrValues().add(skuValue);

                        //除此之外将该attrValue加入临时templeValue表，确保下一次进入时不会有重复的attrValue
                        templeValue.add(relation.getAttrValue());
                    }
                }
                else{
                    //否则，代表该attrId已经设置了集合，直接在其skuValue后拼接,skuId就行了

                    //查找该skuId是在哪一个attrId下
                    vo.getAttrValues().forEach(value->{
                        if(value.getAttrValue().equals(relation.getAttrValue())){
                            value.setSkuIds(value.getSkuIds()+","+relation.getSkuId());
                        }
                    });
                }
            });

            return vo;
        }).collect(Collectors.toList());

        return saleAttr;
    }


    /**
     *
     * @param spuId
     * @return
     *
     * 根据spuId，返回所有与之相关的attr和attrGroup的vo
     *
     */
    private List<SkuItemVo.SpuItemAttrGroupVo> getSpuItemAttrGroupVo(Long spuId) {
/**
 * 商品介绍里面的
 */
        /**
         * 1.pms_product_attr_value表，通过spuId获取每个attrId
         * 2.pms_attr_attrgroup_relation，通过attrId获取attrGroupId
         * 3.pms_attrgroup，通过attrGroupId获取attrGroupName
         */
        //用这个map，代替SpuItemAttrGroupVo的List，因为这个满足KV对条件
        Map<String,List<Attr>> groupAndAttr=new HashedMap();
        productAttrValueDao.selectList(
                new QueryWrapper<ProductAttrValueEntity>()
                        .eq("spu_id",spuId)
        ).forEach(
                pavEntity->{
                    /**
                     * 我只要attrId
                     */
                    //获取attrId
                    Long attrId=pavEntity.getAttrId();
                    //获取该attr对应的attrGroupId
                    Long attrGroupId=attrAttrgroupRelationDao.selectOne(
                                    new QueryWrapper<AttrAttrgroupRelationEntity>()
                                            .eq("attr_id",attrId))
                            .getAttrGroupId();
                    //获取该attr对应的groupName
                    String attrGroupName=attrGroupDao.selectById(attrGroupId).getAttrGroupName();
                    if (groupAndAttr.containsKey(attrGroupName)) {
                        //如果map中存在了以groupName为键的数据
                        //就直接加入该键值对下值的list中
                    }
                    else{
                        //若map中还不存在以groupName为键的数据
                        //则新建一个键值对存入
                        groupAndAttr.put(attrGroupName,new ArrayList<>());
                    }

                    Attr attr=new Attr();
                    attr.setAttrId(attrId);
                    attr.setAttrName(attrDao.selectById(attrId).getAttrName());
                    attr.setAttrValue(productAttrValueDao.selectOne(
                                            //同时匹配attrID和spuId
                                            new QueryWrapper<ProductAttrValueEntity>()
                                                    .eq("spu_id",spuId)
                                                    .and(w->w
                                                            .eq("attr_id",attrId))
                                    )
                                    .getAttrValue()
                    );
                    groupAndAttr.get(attrGroupName).add(attr);
                }
        );

        List<SkuItemVo.SpuItemAttrGroupVo> groupAttrs=new ArrayList<>();
        groupAndAttr.entrySet().forEach(set->{
            String groupName=set.getKey();
            List<Attr> attrs=set.getValue();
            SkuItemVo.SpuItemAttrGroupVo vo=new SkuItemVo.SpuItemAttrGroupVo();
            vo.setGroupName(groupName);
            vo.setAttrs(attrs);
            groupAttrs.add(vo);
        });


        return groupAttrs;

    }


    /**
     * 被order模块远程调用的方法
     * 获取全部spu的weight
     * 返回其对应的所有sku的weights
     */
    @Override
    public Map<String, BigDecimal> allSpuWeights() {
        Map<String, BigDecimal> spuWeights = baseMapper.selectList(null).stream().collect(Collectors.toMap(
                e -> e.getId().toString()
                , e -> e.getWeight()
        ));
        //根据所有的sku对应的spu查spuWeights表内数据，并将skuId和spuWeight拼接为map
        Map<String, BigDecimal> finale=new HashMap<>();
        skuInfoDao.selectList(null).forEach(
                e->{
                    if(spuWeights.get(e.getSpuId().toString())!=null){
                        finale.put(e.getSkuId().toString(),spuWeights.get(e.getSpuId().toString()));
                    }
                }
        );
        return finale;
    }


    /**
     *
     * @param skuId
     * @return
     *
     * 被order模块远程调用
     * 根据skuId获取完整的spuInfoTO
     */
    @Override
    public SpuInfoTO getBySkuId(Long skuId) {
        SpuInfoEntity entity=spuInfoDao.selectById(
                skuInfoDao.selectOne(new QueryWrapper<SkuInfoEntity>()
                        .eq("sku_id",skuId))
                        .getSpuId()
        );
        SpuInfoTO finale= new SpuInfoTO();
        BeanUtils.copyProperties(entity,finale);
        return finale;
    }


}