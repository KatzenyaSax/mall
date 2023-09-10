package com.katzenyasax.test.controller;


import com.katzenyasax.common.utils.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class Controller_ShowDataInBootstrap {
    @Value("${a.b.c}")
    String data;
    @RequestMapping("/showData")
    public R showDataInBootstrap(){
        return R.ok(data);
    }
}
