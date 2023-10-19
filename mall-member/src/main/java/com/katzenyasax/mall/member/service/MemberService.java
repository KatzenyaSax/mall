package com.katzenyasax.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.katzenyasax.common.to.UserLoginTo;
import com.katzenyasax.common.to.UserRegisterTo;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:14:07
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    R register(UserRegisterTo to);

    R login(UserLoginTo to);
}

