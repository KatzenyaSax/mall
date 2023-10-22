package com.katzenyasax.mall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.katzenyasax.common.to.UserLoginTO;
import com.katzenyasax.common.to.UserRegisterTO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.katzenyasax.mall.member.entity.MemberEntity;
import com.katzenyasax.mall.member.service.MemberService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.R;



/**
 * 会员
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:14:07
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;




    /**
     *
     * @param to
     * @return
     *
     * 注册会员
     * 被auth模块远程调用的方法
     */
    @RequestMapping("/register")
    R register(@RequestBody UserRegisterTO to){
        R r=memberService.register(to);
        System.out.println("MemberController Register:"+r);
        return r;
    }


    /**
     * 用户登录
     * 被auth远程调用
     */
    @RequestMapping("/login")
    R login(@RequestBody UserLoginTO to){
        R r=memberService.login(to);
        System.out.println("MemberController Login:"+r);
        return r;
    }






    //==================================================

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
