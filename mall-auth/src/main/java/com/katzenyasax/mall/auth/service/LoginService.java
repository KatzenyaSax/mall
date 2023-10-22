package com.katzenyasax.mall.auth.service;


import com.katzenyasax.common.to.UserLoginTO;
import com.katzenyasax.common.utils.R;

public interface LoginService {

    R login(UserLoginTO to);
}
