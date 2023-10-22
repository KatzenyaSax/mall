package com.katzenyasax.mall.member.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.to.UserLoginTO;
import com.katzenyasax.common.to.UserRegisterTO;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;
import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.member.dao.MemberDao;
import com.katzenyasax.mall.member.entity.MemberEntity;
import com.katzenyasax.mall.member.service.MemberService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.katzenyasax.mall.member.exception.AuthCodeEnume.*;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    /**
     *
     * @param to
     * @return
     *
     * 注册用户
     *
     */
    @Override
    public R register(UserRegisterTO to) {

        //在此之前判断数据库中是否已有重复的手机号、用户名
        //手机号和用户名作为唯一标识（后续可更新）
        if (baseMapper.selectList(new QueryWrapper<MemberEntity>().eq("username", to.getUserName())).size() > 0) {
            System.out.println("MemberService：已存在用户名为" + to.getUserName() + "的用户！");
            return R.error(USERNAME_ALREADY_EXIST.getMsg());
        }
        if (baseMapper.selectList(new QueryWrapper<MemberEntity>().eq("mobile", to.getPhone())).size() > 0) {
            System.out.println("emberService：已存在手机号为" + to.getUserName() + "的用户！");
            return R.error(PHONE_ALREADY_EXIST.getMsg());
        }

        //封装对象，新用户
        MemberEntity finale=new MemberEntity();
        //三个to参数，密码需要加密
        finale.setUsername(to.getUserName());
        finale.setMobile(to.getPhone());

        //加盐加密，BCryptPasswordEncoder自带加盐和读取盐值功能
        finale.setPassword(
                new BCryptPasswordEncoder().encode(to.getPassword())
        );

        //初始化参数
        finale.setLevelId(1L);
        finale.setNickname(to.getUserName());
        finale.setIntegration(0);
        finale.setGender(0);
        finale.setStatus(1);
        finale.setCreateTime(new DateTime());

        //存入数据库
        baseMapper.insert(finale);

        //反馈
        return R.ok(REGISTER_SUCCESS.getMsg());
    }


    /**
     * 用户登录
     */
    @Override
    public R login(UserLoginTO to) {
        //输入的账号有可能是用户名，也有可能是手机号，所以要同时查
        List<MemberEntity> members=baseMapper.selectList(
                new QueryWrapper<MemberEntity>()
                        .eq("username",to.getLoginacct())
                        .or().eq("mobile",to.getLoginacct())
        );

        //1.账号不存在
        if(members.size()==0){
            System.out.println("账户不存在");
            return R.error("账户不存在");
        }

        //2.密码错误
        //如果查得到，只可能查到一个，直接查索引为0的元素
        //其password就是密文密码
        MemberEntity thisAccount=members.get(0);
        //加密器
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        //对密码进行加密
        String passwordEncoded=encoder.encode(to.getPassword());
        //匹配
        if(!encoder.matches(to.getPassword(), thisAccount.getPassword())){
            return R.error("密码错误");
        }

        //完成正确反馈
        return R.ok("登陆成功").put("LoginUser",thisAccount);
    }

}