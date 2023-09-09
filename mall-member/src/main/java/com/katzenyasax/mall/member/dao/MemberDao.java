package com.katzenyasax.mall.member.dao;

import com.katzenyasax.mall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:14:07
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
