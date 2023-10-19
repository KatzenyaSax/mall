package com.katzenyasax.mall.auth.service;


import com.katzenyasax.common.to.UserLoginTo;
import com.katzenyasax.common.utils.R;

public interface LoginService {

    R login(UserLoginTo to);
}
