package com.example.mall.thirdparty.controller;


import com.aliyun.oss.OSSClientBuilder;
import org.springframework.web.bind.annotation.RestController;

import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class OssController {
    @Value("${alibaba.cloud.oss.endpoint}")
    private String endpoint;    //从yml文件中读取
    @Value("${alibaba.cloud.oss.bucket}")
    private String bucket;      //从yml文件中读取
    @Value("${alibaba.cloud.access-key}")
    private String accessId;  //从yml文件中读取





    @GetMapping("/thirdparty/oss/policy")
    @CrossOrigin
    public Map<String, String> policy(){

        String endpoint = "oss-cn-beijing.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
        String accessKeyId = "LTAI5tSMQjRn2aWaXWYYezqU";
        String accessKeySecret = "4RTcGMYo6UGNGAlvoicr4bVgw3ysWH";
        String bucketName = "kaztenyasax-mall";
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);


        //https://md-ossbucket.oss-cn-beijing.aliyuncs.com/QQ%E6%88%AA%E5%9B%BE20210609114525.png  host的格式为 bucketname.endpoint
        String host = "https://" + bucket + "." + endpoint;
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date()); //格式化一个当前的服务器时间
        String dir = format+"/"; // 用户上传文件时指定的前缀,我们希望以日期作为一个目录
        Map<String, String> respMap =null; //返回结果
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));
        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        System.out.println(respMap.toString());
        return respMap;
    }


}
