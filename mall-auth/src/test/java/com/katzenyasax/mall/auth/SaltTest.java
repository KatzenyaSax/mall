package com.katzenyasax.mall.auth;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SaltTest {

    @Test
    public void salt(){
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
    }







}
