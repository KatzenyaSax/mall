package com.katzenyasax.mall.ware.service.impl;

import com.alibaba.fastjson.JSON;
import com.katzenyasax.common.to.MemberAddressTO;
import com.katzenyasax.mall.ware.feign.MemberFeign;
import com.katzenyasax.mall.ware.vo.FareVo;
import com.qiniu.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;

import com.katzenyasax.mall.ware.dao.WareInfoDao;
import com.katzenyasax.mall.ware.entity.WareInfoEntity;
import com.katzenyasax.mall.ware.service.WareInfoService;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Autowired
    MemberFeign memberFeign;






    /**
     *
     * @param params
     * @return
     *
     * mybatis plus自动生成的方法
     * 查询所有wareInfo
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                new QueryWrapper<WareInfoEntity>()
        );

        return new PageUtils(page);
    }


    /**
     *
     * @param params
     * @return
     *
     * 根据param获取合法的wareInfo
     * params包含一个key，用于模糊查询
     *
     */
    @Override
    public PageUtils getWareInfo(Map<String, Object> params) {

        QueryWrapper<WareInfoEntity> wrapper=new QueryWrapper<>();
        String key=(String) params.get("key");
        if(!StringUtils.isNullOrEmpty(key)){
            wrapper.like("name",key).or().like("address",key).or().like("areacode",key);
        }
        IPage<WareInfoEntity> finale = this.page(
                new Query<WareInfoEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(finale);
    }


    /**
     *
     * @param addrId
     * @return
     *
     * 前端请求，获取fareVo，即地址
     */
    @Override
    public FareVo getFareVo(Long addrId) {
        //初始化
        FareVo finale=new FareVo();
        //运费为8
        finale.setFare(BigDecimal.valueOf(8));
        //地址
        Object memberReceiveAddress = memberFeign.info(addrId).get("memberReceiveAddress");
        MemberAddressTO address = JSON.parseObject(JSON.toJSONString(memberFeign.info(addrId).get("memberReceiveAddress")), MemberAddressTO.class);
        finale.setAddress(address);
        return finale;
    }

}