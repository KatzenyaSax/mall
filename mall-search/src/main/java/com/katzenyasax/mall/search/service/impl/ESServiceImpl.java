package com.katzenyasax.mall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.katzenyasax.common.to.SkuEsModel;
import com.katzenyasax.mall.search.config.ESClientConfiguration;
import com.katzenyasax.mall.search.constant.ESConstant;
import com.katzenyasax.mall.search.service.ESService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ESServiceImpl implements ESService {




    /**
     *
     * @param skuEsModels
     * @return
     *
     * 上传skuEsModels到es
     *
     */
    @Autowired
    RestHighLevelClient highLevelClient;
    @Override
    public void SkuUp(List<SkuEsModel> skuEsModels) {
        //创建服务器
        //就是highLevelClient
        System.out.println("skuEsModels"+skuEsModels.toString());
        //创建批量操作指令
        BulkRequest bulkRequest=new BulkRequest();
        for(SkuEsModel model:skuEsModels){
            //创建索引请求，表示在哪一个索引下进行操作
            IndexRequest indexRequest=new IndexRequest(ESConstant.PRODUCT_INDEX);
            //表示在该索引的哪一个id进行操作，由于此处的skuId是唯一的，因此id直接使用skuId
            indexRequest.id(model.getSkuId().toString());
            //获取存储数据的json格式
            String data= JSON.toJSONString(model);
            //指定索引请求的json请求来源
            indexRequest.source(data, XContentType.JSON);
            //将该次遍历得到的json指令拼接到总的批量操作
            bulkRequest.add(indexRequest);
        }

        //执行批量操作指令，记得抛异常
        try {
            highLevelClient.bulk(bulkRequest,ESClientConfiguration.COMMON_OPTIONS);
        } catch (IOException e) {
            log.info("！！保存失败！！");
        }


    }
}
