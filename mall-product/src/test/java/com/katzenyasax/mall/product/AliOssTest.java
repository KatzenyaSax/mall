package com.katzenyasax.mall.product;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;



public class AliOssTest {

    String endpoint = "oss-cn-beijing.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
    String accessKeyId = "LTAI5tSMQjRn2aWaXWYYezqU";
    String accessKeySecret = "4RTcGMYo6UGNGAlvoicr4bVgw3ysWH";
    String bucketName = "kaztenyasax-mall";


    @Test
    public void create() {
        OSS ossClient;
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        boolean exists = ossClient.doesBucketExist(bucketName);
        System.out.println("exists = " + exists);
        if (exists == false) { //判定是否存在此存储空间
            // 创建存储空间。
            ossClient.createBucket(bucketName);
        }
        ossClient.shutdown();
    }

    @Test
    public void upload(){



        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件流。
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("C:\\Users\\ASUS\\Desktop\\东方project\\A38FC81C7BFD7B637F9FD6A7B9F2EDF8.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ossClient.putObject(bucketName, "test545.jpg", inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

}
