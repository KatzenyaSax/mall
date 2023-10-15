package com.katzenyasax.mall.search;

import com.alibaba.fastjson.JSON;
import com.katzenyasax.mall.search.config.ESClientConfiguration;
import kotlin.Metadata;
import lombok.Data;
import org.apache.lucene.util.QueryBuilder;
import org.apache.shiro.authc.Account;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.search.MatchQuery;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
class MallSearchTests {

    @Autowired
    private ESClientConfiguration client;


    /**
     * 测试看看client是否正确注入了
     */
    @Test
    void contextLoads() {
        System.out.println(client);
    }


    /**
     *
     * @throws IOException
     * 测试一下能不能存数据
     */
    @Test
    void IndexData() throws IOException {
        /**
         * 任何请求首先要定义一个IndexRequest类型的对象才能实现
         */
        IndexRequest indexRequest=new IndexRequest("data_test");
        indexRequest.id("1");
        //任何情况下都要使用字符串形式
        /**
         * 将要保存的数据准备好
         * 将其转化为json格式
         */
        Data_Test dataTest=new Data_Test();
        dataTest.setName("NAME");
        dataTest.setGender("M");
        dataTest.setAge(20);
        String json= JSON.toJSONString(dataTest);
        indexRequest.source(json, XContentType.JSON);
        /**
         * 进行存储
         */
        IndexResponse indexResponse=client.EsClient().index(indexRequest,ESClientConfiguration.COMMON_OPTIONS);
            //此时的indexResponse用于执行indexRequest
        /**
         * 打印结果测试
         */
        System.out.println("indexResponse:"+indexResponse.toString());
        //indexResponse:IndexResponse[index=data_test,type=_doc,id=1,version=1,result=created,seqNo=0,primaryTerm=1,shards={"total":2,"successful":1,"failed":0}]
    }

    /**
     * 一个实体对象
     */
    @Data
    class Data_Test{
        private String name;
        private String gender;
        private Integer age;
    }


    /**
     * 测试一下，查询数据
     */
    @Test
    public void SearchData() throws IOException {
        /**1.
         * 查询首先要定义一个SearchRequest对象
         * 指定查询的索引
         * 这个对象只发挥存储命令的作用
         * 不发挥构建命令、执行命令的作用
         */
        SearchRequest searchRequest=new SearchRequest();
        searchRequest.indices("bank");

        /**2.
         * 随后创建一个SearchSourceBuilder对象
         * 发挥构建命令的作用
         * 需要将其交给searchRequest，表示器存储的一切命令都来源与这个searchBuilder
         */
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        {
            /**
             * 开始构造
             * 例如实现一个功能：
             * 查出每个年龄段人数及平均年龄，计算平均薪资balance
             * 在kibana中命令为：
             *
             *          # 查出每个年龄段人数及平均年龄，计算平均薪资balance
             *          GET bank/_search
             *          {
             *            "query": {
             *              "match_all": {}
             *            },
             *
             *            "aggs": {
             *              "Age_Term": {
             *                "terms": {
             *                  "field": "age",
             *                  "size": 1000
             *                }
             *              },
             *              "Balance_AVG":{
             *                "avg": {
             *                  "field": "balance"
             *                }
             *              }
             *            }
             *          }
             *
             * 那么构造应该是：
             */
            //这个是最外最大的query，即查询
            //此处matchAllQuery表示查询所有
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            /**
             * 之后聚合函数
             */
            //根据年龄聚合
            TermsAggregationBuilder termsAge = AggregationBuilders.terms("Age_Term").field("age").size(1000);
            searchSourceBuilder.aggregation(termsAge);
            //求出平均薪资
            AvgAggregationBuilder avgBalance=AggregationBuilders.avg("Balance_Avg").field("balance");
            searchSourceBuilder.aggregation(avgBalance);
        }
        System.out.println("检索条件："+ searchSourceBuilder);
        searchRequest.source(searchSourceBuilder);

        /**3.
         * 执行searchRequest，使用client执行
         */
        SearchResponse searchResponse=client.EsClient().search(searchRequest,ESClientConfiguration.COMMON_OPTIONS);

        /**4.
         * 分析结果
         */
        //整个结果
        System.out.println("查询结果："+searchResponse);
        //命中结果
        SearchHits hits=searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for(SearchHit hit:searchHits){
            String object= hit.getSourceAsString();
            BankAccount account=JSON.parseObject(object, BankAccount.class);
            System.out.println("account: "+account);
        }

        //运行时间
        TimeValue took = searchResponse.getTook();
        System.out.println("took: "+took.toString());

        //聚合函数分析结果
        Aggregations aggregations=searchResponse.getAggregations();
        {
            //所有年龄段
            Terms AgeAvg = aggregations.get("Age_Term");
            for (Terms.Bucket bucket : AgeAvg.getBuckets()) {
                String object = bucket.getKeyAsString();
                System.out.println("bucket: " + object);
            }

            //平均薪资
            Avg balanceAvg=aggregations.get("Balance_Avg");
            System.out.println("balance avg: "+balanceAvg.getValue());
        }
    }

    /**
     * 根据解析json数据，自动生成的java bean类
     */
    @Data
    public static class BankAccount{
        private int account_number;
        private int balance;
        private String firstname;
        private String lastname;
        private int age;
        private String gender;
        private String address;
        private String employer;
        private String email;
        private String city;
        private String state;
    }




}
