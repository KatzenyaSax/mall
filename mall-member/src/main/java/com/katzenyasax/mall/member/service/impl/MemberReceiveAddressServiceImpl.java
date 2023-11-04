package com.katzenyasax.mall.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.katzenyasax.common.to.MemberAddressTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.member.dao.MemberReceiveAddressDao;
import com.katzenyasax.mall.member.entity.MemberReceiveAddressEntity;
import com.katzenyasax.mall.member.service.MemberReceiveAddressService;


@Service("memberReceiveAddressService")
public class MemberReceiveAddressServiceImpl extends ServiceImpl<MemberReceiveAddressDao, MemberReceiveAddressEntity> implements MemberReceiveAddressService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberReceiveAddressEntity> page = this.page(
                new Query<MemberReceiveAddressEntity>().getPage(params),
                new QueryWrapper<MemberReceiveAddressEntity>()
        );

        return new PageUtils(page);
    }


    /**
     *
     * @param memberId
     * @return
     *
     *
     * order远程调用的方法
     *
     * 获得对应userId的地址
     */
    @Override
    public List<MemberAddressTO> getByMemberId(String memberId) {
        List<MemberAddressTO> finale=new ArrayList<>();
        baseMapper.selectList(
                new QueryWrapper<MemberReceiveAddressEntity>()
                        .eq("member_id", memberId)
        ).forEach(address->{
            finale.add(JSON.parseObject(JSON.toJSONString(address),MemberAddressTO.class));
        });
        return finale;
    }

}