package com.katzenyasax.mall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.katzenyasax.common.to.SkuEsModel;
import com.katzenyasax.mall.search.config.ESClientConfiguration;
import com.katzenyasax.mall.search.constant.ESConstant;
import com.katzenyasax.mall.search.service.SearchService;
import com.katzenyasax.mall.search.vo.SearchParam;
import com.katzenyasax.mall.search.vo.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class SearchServiceImpl implements SearchService {


    @Autowired
    RestHighLevelClient client;
    private AggregationBuilder brandAgg;


    /**
     *
     * @param params
     * @return
     *
     * 根据vo提供的条件，查询符合的对象
     *
     * 构建dsl语句查询
     *
     */
    @Override
    public SearchResult search(SearchParam params) {
        //查询结果
        SearchResult finale;


        //1.创建查询请求，
        SearchRequest searchRequest;

        //1.1、构建聚合语句
        //自定义一个方法
        searchRequest = this.buildRequest(params);

        try {
            //2.执行查询请求，获取的是根据param条件匹配的数据
            SearchResponse searchResponse=client.search(searchRequest, ESClientConfiguration.COMMON_OPTIONS);
            System.out.println("响应："+searchResponse.toString());

            //3.分析结果
            finale=this.buildResult(params,searchResponse);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }




        System.out.println("结果："+JSON.toJSONString(finale));
        return finale;
    }

    /**
     *
     * @param searchResponse
     * @return
     *
     * 封装的方法，根据响应处理数据到SearchResponse
     */
    private SearchResult buildResult(SearchParam param,SearchResponse searchResponse) {
        //实例化对象
        SearchResult result=new SearchResult();

        //1.分析hits
        SearchHits hits = searchResponse.getHits();

        //1.1、总记录数
        result.setTotal(hits.getTotalHits().value);

        //1.2、总页码，计算
        result.setTotalPages(
                (int) ((hits.getTotalHits().value%ESConstant.PRODUCT_PAGESIZE)==0
                ?hits.getTotalHits().value/ESConstant.PRODUCT_PAGESIZE
                :hits.getTotalHits().value/ESConstant.PRODUCT_PAGESIZE+1)
        );

        //1.3、当前页码
        result.setPageNum(param.getPageNum());

        //1.4、products
        //先给product放进一个list，防止空指针
        result.setProduct(new ArrayList<>());
        //要查的是_source，将每个hit转成Json格式字符串，再用FastJSON转成skuEsModel对象，再将其存入result的product
        if(searchResponse.getHits().getHits()!=null&&searchResponse.getHits().getHits().length>0){
            for(SearchHit hit:searchResponse.getHits().getHits()){
                result.getProduct().add(JSON.parseObject(
                        hit.getSourceAsString(),
                        SkuEsModel.class
                        )
                );
            }
        }

        //完成了hits，接下来完成aggregations


        //返回值的几个集合
        List<SearchResult.BrandVo> brands=new ArrayList<>();
        /**
         * 当前查询到的结果，所有涉及到的所有属性
         */
        List<SearchResult.AttrVo> attrs=new ArrayList<>();
        /**
         * 当前查询到的结果，所有涉及到的所有分类
         */
        List<SearchResult.CatalogVo> catalogs=new ArrayList<>();

        //2.1、获取brand聚合
        ParsedLongTerms brandAgg = searchResponse.getAggregations().get("brandAgg");
        for (Terms.Bucket bucket : brandAgg.getBuckets()) {
            //当前bucket的vo对象
            SearchResult.BrandVo brandVo=new SearchResult.BrandVo();
            //brandId
            brandVo.setBrandId(Long.parseLong(bucket.getKeyAsString()));
            //获取name子聚合
            ParsedStringTerms brandNameAgg = bucket.getAggregations().get("brandNameAgg");
            //获取brandName
            if((brandNameAgg.getBuckets())!=null&&brandNameAgg.getBuckets().size()>0) {
                String brandName = brandNameAgg
                        .getBuckets()
                        .get(0)
                        .getKeyAsString();
                brandVo.setBrandName(brandName);
            }else {
                brandVo.setBrandName(null);
            }

            //获取Img子聚合
            ParsedStringTerms brandImgAgg = bucket.getAggregations().get("brandImgAgg");
            //获取brandImg
            if((brandImgAgg.getBuckets())!=null&&brandImgAgg.getBuckets().size()>0) {
                String brandImg = brandImgAgg
                        .getBuckets()
                        .get(0)
                        .getKeyAsString();
                brandVo.setBrandImg(brandImg);
            }else{
                brandVo.setBrandImg(null);
            }

            brands.add(brandVo);
        }

        //2.2、获取catalog聚合
        ParsedLongTerms catalogAgg = searchResponse.getAggregations().get("catalogAgg");
        for (Terms.Bucket bucket : catalogAgg.getBuckets()) {
            //获取vo对象
            SearchResult.CatalogVo catalogVo=new SearchResult.CatalogVo();
            //catalogId
            catalogVo.setCatalogId(Long.parseLong(bucket.getKeyAsString()));
            //获取Name子聚合
            ParsedStringTerms catalogNameAgg=bucket.getAggregations().get("catalogNameAgg");
            if((catalogNameAgg.getBuckets())!=null&&catalogNameAgg.getBuckets().size()>0) {
                //获取catalogName
                String catalogName = catalogNameAgg
                        .getBuckets()
                        .get(0)
                        .getKeyAsString();
                catalogVo.setCatalogName(catalogName);
            }else {
                catalogVo.setCatalogName(null);
            }
            catalogs.add(catalogVo);

        }

        //2.3、获取attr聚合
        ParsedNested attrAgg = searchResponse.getAggregations().get("attrAgg");
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attrIdAgg");
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
            //当前bucket的vo对象
            SearchResult.AttrVo attrVo=new SearchResult.AttrVo();

            //获取id
            attrVo.setAttrId(Long.parseLong(bucket.getKeyAsString()));

            //获取attrName子聚合
            ParsedStringTerms attrNameAgg=bucket.getAggregations().get("attrNameAgg");
            if((attrNameAgg.getBuckets())!=null&&attrNameAgg.getBuckets().size()>0) {
                //获取attrName
                String attrName = attrNameAgg
                        .getBuckets()
                        .get(0)
                        .getKeyAsString();
                attrVo.setAttrName(attrName);
            }else{
                attrVo.setAttrName(null);
            }

            //获取attrValue子聚合，结果是一个list，需要遍历
            ParsedStringTerms attrValueAgg=bucket.getAggregations().get("attrValueAgg");
            if(!(attrValueAgg.getBuckets()==null)&&attrValueAgg.getBuckets().size()>0){
                List<String> attrValue=new ArrayList<>();
                for (Terms.Bucket thisBucket : attrValueAgg.getBuckets()) {
                       attrValue.add(thisBucket.getKeyAsString());
                }
                attrVo.setAttrValue(attrValue);
            }else{
                attrVo.setAttrValue(null);
            }

            attrs.add(attrVo);
        }


        result.setBrands(brands);
        result.setCatalogs(catalogs);
        result.setAttrs(attrs);



        //构建面包屑导航
        List<SearchResult.NavVo> navVos=new ArrayList<>();





        return result;

    }

    /**
     *
     * @param params
     * @return
     *
     * 封装的方法，根据param构建dsl查询条件
     *
     * 应当实现的功能为： 匹配关键字；
     *                  属性、分类、品牌、价格、有无库存；
     *                  排序、分页、高亮;
     *                  聚合分析
     *
     */
    private SearchRequest buildRequest(SearchParam params) {
        //构建dsl语句的建造器
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();


        //先来一个bool查询，创建的对象bool就是整个bool查询语句
        BoolQueryBuilder bool = QueryBuilders.boolQuery();

        //1.1、匹配关键字，如果关键字不为空的话
        if(StringUtils.isNotEmpty(params.getKeyword())){
            //相当于将一个match拼接到bool语句的后面了
            bool.must(QueryBuilders.matchQuery("skuTitle",params.getKeyword()));
        }

        //1.2、匹配分类
        if(params.getCatalogId()!=null) {
            bool.filter(QueryBuilders.termQuery("catalogId", params.getCatalogId()));
        }
        //1.3、匹配品牌，品牌可多选
        if(params.getBrandId()!=null ) {
            for (Long brandId : params.getBrandId()) {
                bool.filter(QueryBuilders.termQuery("brandId", brandId));
            }
        }
        //TODO 品牌如果是List<Long>，会报错，暂时未解决



        //1.4、匹配有无库存
        if(params.getHasStock()!=null) {
            bool.filter(QueryBuilders.termQuery("hasStock", params.getHasStock()));
        }
        //1.5、匹配价格，格式为a_b，即再a和b之间，a和b可为空
        if(StringUtils.isNotEmpty(params.getSkuPrice())){
            String[] prices=params.getSkuPrice().split("_");
            //这个是两个价格，为空则表示不需要匹配
            RangeQueryBuilder range=QueryBuilders.rangeQuery("skuPrice");

            //价格不为空，则进行匹配
            if(StringUtils.isNotEmpty(prices[0])){
                range.lte(Integer.parseInt(prices[0]));
            }
            if(StringUtils.isNotEmpty(prices[1])){
                range.gte(Integer.parseInt(prices[1]));
            }
            //匹配价格
            bool.filter(range);
        }

        //1.6、匹配属性
        if(params.getAttrs()!=null && params.getAttrs().size()>0){

            //attr的格式为attrId_attrName:attrSplit，表示
            for(String attr:params.getAttrs()) {

                //这个bool_attrs是该attr嵌入式内部的bool查询，最终放到nested
                //每一个attr都要生成单独的nested，且和其他的filter、must平级
                /*BoolQueryBuilder bool_attrs=QueryBuilders.boolQuery();*/

                //分割数据
                Long attrId=Long.parseLong(attr.split("_")[0]);
                String attrValue=attr.split("_")[1];

                //TODO 貌似任何匹配字段为集合、数组等非单字时查不出来

                System.out.println("attrValue: "+attr.split("_")[1]);
             /*   //内部bool查询
                bool_attrs
                        .must(QueryBuilders.termQuery("attr.attrId",attrId))
                        .must(QueryBuilders.termQuery("attr.attrValue",attrValue));

                //将bool_attrs放入该attr的nested
                NestedQueryBuilder nested =QueryBuilders.nestedQuery("attrs",bool_attrs, ScoreMode.None);

                //将该nested直接放入bool，且和filter和must平级
                bool.filter(nested);*/


                /**
                 * 优雅写法：
                 */

                bool.filter(QueryBuilders.nestedQuery(
                        "attrs",
                        QueryBuilders.boolQuery().
                                must(QueryBuilders.
                                        termQuery("attrs.attrId",attrId)).
                                must(QueryBuilders.
                                        termQuery("attrs.attrValue",attrValue)),
                        ScoreMode.None)
                );


            }
        }

        //完成了bool查询
        searchSourceBuilder.query(bool);





        //2.1、排序
        //排序条件：sort=price/salecount/hotscore_desc/asc

        if(StringUtils.isNotEmpty(params.getSort())){
            //分割数据
            String word= params.getSort().split("_")[0];
            String sort= params.getSort().split("_")[1];

            if(sort.equals("desc")){
                searchSourceBuilder.sort(word, SortOrder.DESC);
            }
            else if(sort.equals("asc")){
                searchSourceBuilder.sort(word, SortOrder.ASC);
            }
        }

        //2.2、分页
        //在ESConstant中定义一个PRODUCT_PAGESIZE
        //起始个数为：(页数-1)*每页的长度
        searchSourceBuilder.from(ESConstant.PRODUCT_PAGESIZE*(params.getPageNum()-1) );
        searchSourceBuilder.size(ESConstant.PRODUCT_PAGESIZE);

        //2.3、高亮
        //只高亮关键字
        if(StringUtils.isNotEmpty(params.getKeyword())){
            /*HighlightBuilder highlight=new HighlightBuilder();

            highlight.field("skuTitle");
            highlight.preTags("<b style='color:red'>");
            highlight.postTags("</b>");*/

            /**
             * 优雅写法：
             */
            searchSourceBuilder.highlighter(
                    new HighlightBuilder().
                            field("skuTitle").
                            preTags("<b style='color:red'>").
                            postTags("</b>")
            );

        }

        //完成了页面设置
        //接下来进行聚合分析



        //聚合品牌
        /*TermsAggregationBuilder brandAgg= AggregationBuilders.terms("brandAgg");
        brandAgg.field("brandId").size(100);

        //品牌的子聚合
        TermsAggregationBuilder brandNameAgg=AggregationBuilders.terms("brandNameAgg");
        brandNameAgg.field("brandName.keyword").size(1);

        TermsAggregationBuilder brandImgAgg=AggregationBuilders.terms("brandImgAgg");
        brandImgAgg.field("brandImg.keyword").size(1);

        brandAgg.subAggregation(brandNameAgg).subAggregation(brandImgAgg);*/

        /**
         * 优雅写法：
         */
        //聚合品牌
        TermsAggregationBuilder brandAgg= AggregationBuilders.
                terms("brandAgg").
                field("brandId").size(100).
                subAggregation(AggregationBuilders.
                        terms("brandNameAgg").
                        field("brandName").
                        size(1)
                ).
                subAggregation(
                        AggregationBuilders.
                        terms("brandImgAgg").
                        field("brandImg").
                        size(1)
                );
        searchSourceBuilder.aggregation(brandAgg);



        //聚合分类：
        TermsAggregationBuilder catalogAgg=AggregationBuilders.
                terms("catalogAgg").
                field("catalogId").
                size(100).
                subAggregation(AggregationBuilders.
                        terms("catalogNameAgg").
                        field("catalogName").
                        size(1)
                );
        searchSourceBuilder.aggregation(catalogAgg);

        //聚合属性
        NestedAggregationBuilder attrAgg=AggregationBuilders.
                nested("attrAgg","attrs").
                subAggregation(AggregationBuilders.
                        terms("attrIdAgg").
                        field("attrs.attrId").
                        size(100).
                        subAggregation(AggregationBuilders.
                                terms("attrNameAgg").
                                field("attrs.attrName").
                                size(100)
                        ).
                        subAggregation(AggregationBuilders.
                                terms("attrValueAgg").
                                field("attrs.attrValue").
                                size(100)
                        )
                );
        searchSourceBuilder.aggregation(attrAgg);


        System.out.println("SDL请求："+searchSourceBuilder.toString());


        return new SearchRequest(new String[] {ESConstant.PRODUCT_INDEX},searchSourceBuilder);






    }
}
