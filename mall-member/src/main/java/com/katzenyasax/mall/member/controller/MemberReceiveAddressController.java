package com.katzenyasax.mall.member.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.katzenyasax.common.to.MemberAddressTO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.katzenyasax.mall.member.entity.MemberReceiveAddressEntity;
import com.katzenyasax.mall.member.service.MemberReceiveAddressService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.R;



/**
 * 会员收货地址
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:14:07
 */
@RestController
@RequestMapping("member/memberreceiveaddress")
public class MemberReceiveAddressController {
    @Autowired
    private MemberReceiveAddressService memberReceiveAddressService;


    /**
     *
     * @param id
     * @return
     *
     * 获取该用户的所有地址
     */
    @GetMapping("/getByMemberId/{id}")
    public List<MemberAddressTO> getByMemberId(@PathVariable String id){
        return memberReceiveAddressService.getByMemberId(id);
    }








    //============================

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("member:memberreceiveaddress:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberReceiveAddressService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("member:memberreceiveaddress:info")
    public R info(@PathVariable("id") Long id){
		MemberReceiveAddressEntity memberReceiveAddress = memberReceiveAddressService.getById(id);

        return R.ok().put("memberReceiveAddress", memberReceiveAddress);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("member:memberreceiveaddress:save")
    public R save(@RequestBody MemberReceiveAddressEntity memberReceiveAddress){
		memberReceiveAddressService.save(memberReceiveAddress);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("member:memberreceiveaddress:update")
    public R update(@RequestBody MemberReceiveAddressEntity memberReceiveAddress){
		memberReceiveAddressService.updateById(memberReceiveAddress);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("member:memberreceiveaddress:delete")
    public R delete(@RequestBody Long[] ids){
		memberReceiveAddressService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
