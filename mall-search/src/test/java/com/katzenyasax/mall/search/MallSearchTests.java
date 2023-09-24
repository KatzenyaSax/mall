package com.katzenyasax.mall.search;

import com.katzenyasax.mall.search.config.ESClientConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class MallSearchTests {

    @Autowired
    private ESClientConfiguration client;
    @Test
    void contextLoads() {
        System.out.println(client);
    }

}
