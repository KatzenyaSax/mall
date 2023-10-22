package com.katzenyasax.mall.auth.service.impl;

import com.katzenyasax.common.to.UserRegisterTO;
import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.auth.config.SendCodeConfigurationProperties;
import com.katzenyasax.mall.auth.feign.MemberFeign;
import com.katzenyasax.mall.auth.service.RegisterService;
import com.katzenyasax.mall.auth.vo.UserRegisterVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;


@Service("registerService")
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    SendCodeConfigurationProperties sendCodeConfigurationProperties;

    @Autowired
    MemberFeign memberFeign;


    /**
     *
     * @param vo
     *
     * 根据vo注册用户
     * 需要调用远程模块mall-member
     *
     */
    @Override
    public R Register(UserRegisterVo vo) {
        UserRegisterTO to=new UserRegisterTO();
        //复制to
        BeanUtils.copyProperties(vo,to);
        R r = memberFeign.register(to);
        return r;
    }

    /**
     *
     * @param vo
     * @return
     *
     * 主要根据vo传来的手机号和验证码判定是否为正确的验证码
     *
     */
    @Override
    public boolean isRightCode(UserRegisterVo vo) {
        String code=vo.getCode();
        String phone=vo.getPhone();

        //获取redis操作柄
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        //获取redis中对应手机号的最新验证码
        //到这一步时，有效期没过时才进行判断
        String data=ops.get(sendCodeConfigurationProperties.getPre()+phone);
        if(StringUtils.isNotEmpty(data)){
            if(code.equals(data.split("-")[0])){

               return true;
            }
        }

        return false;
    }

    /**
     *
     * @param phone
     *
     * 删除code
     */
    @Override
    public void deleteCode(String phone) {
        //获取redis操作柄
        redisTemplate.delete(sendCodeConfigurationProperties.getPre() + phone);
    }
}
