package com.katzenyasax.mall.auth.config;


import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.auth.utils.HttpUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@ConfigurationProperties(prefix = "mall.send-code")
@Component
@Data
public class SendCodeConfigurationProperties {
    private String host;
    private String path;
    private String appcode;
    private Long aliveTime;         //验证码存活时间
    private TimeUnit unit;          //时间单位
    private String pre;             //缓存中前缀
    private Long timeTrap;          //发送验证码的最小间隔(秒)

    @Autowired
    StringRedisTemplate redisTemplate;


    /**
     *
     * @param phone
     * @return
     *
     * 给出手机号码phone，要求向其发送随机验证码
     *
     * 并且要包含防刷、等功能
     *
     */
    public R send(String phone){



        //创建操作柄
        ValueOperations<String,String> ops= redisTemplate.opsForValue();
        //拿取数据（code和上一次发送时间），如果有的话，没有就初始化成原点
        String codeAndTime;
        if(StringUtils.isNotEmpty(ops.get(this.pre + phone)))
        //如果redis中存在数据
        {
            codeAndTime=ops.getAndDelete(this.pre+phone);
            //上一次发送的时间
            Long lastTime = Long.parseLong(codeAndTime.split("-")[1]);

            if (System.currentTimeMillis()-lastTime<this.timeTrap*1000) {   //当和上次发送验证码间隔小于60秒时，说明未到时间（注意timeTrap是毫秒，乘1000才是秒）
                return R.error("请60秒后重试！");
            }
        }


        //随机验证码
        Long code = new Random().nextLong(1000, 9999);
        //状态码，初始为200
        int httpResult=200;
        //========== 发送验证码api   ===========================================
        String host = this.host;
        String path = this.path;
        String method = "POST";
        String appcode = this.appcode;
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("content", "code:" + code);
        bodys.put("template_id", "CST_ptdie100");
        bodys.put("phone_number", phone);
        try {
            /**
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            //httpResult改成api返回的状态码
            httpResult = response.getStatusLine().getStatusCode();
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //===========================================================================================
        //将结果存入redis
        ops.set(
                this.pre + phone
                , code.toString() + "-" + System.currentTimeMillis()
                , this.aliveTime
                , this.unit
        );


        //判断状态码
        if(httpResult==400){
            return R.error("请求参数错误");
        }
        else if(httpResult==403){
            return R.error("请求次数过多，请十分钟后再试！");
        }
        else if(httpResult==500){
            return R.error("服务器错误");
        }
        else {
            return R.ok("已发送验证码").put("code",code);
        }



    }

}
