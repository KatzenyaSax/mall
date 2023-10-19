package com.katzenyasax.common.to;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
public class UserLoginTo {
    @NotEmpty(message = "未提交用户名或手机号！")
    @Length(min = 6,max = 18,message = "必须是6-18位的字符或11位数字")
    private String loginacct;
    @NotEmpty(message = "未提交密码！")
    @Length(min = 6,max = 18,message = "密码必须是6-18位的字符")
    private String password;
}
