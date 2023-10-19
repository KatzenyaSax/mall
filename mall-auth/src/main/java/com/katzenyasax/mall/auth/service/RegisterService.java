package com.katzenyasax.mall.auth.service;


import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.auth.vo.UserRegisterVo;

public interface RegisterService {

    R Register(UserRegisterVo vo);

    boolean isRightCode(UserRegisterVo vo);

    void deleteCode(String phone);
}
