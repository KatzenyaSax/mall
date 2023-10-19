package com.katzenyasax.mall.auth.vo;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserRegisterVo {
    @NotEmpty(message = "未提交用户名！")
    @Length(min = 6,max = 18,message = "用户名必须是6-18位的字符")
    private String userName;
    @NotEmpty(message = "未提交密码！")
    @Length(min = 6,max = 18,message = "密码必须是6-18位的字符")
    private String password;
    @NotEmpty(message = "未提交手机号码！")
    @Pattern(regexp = "1[0-9]{10}",message = "手机号码必须是11位数字！")
    private String phone;
    @NotEmpty(message = "未提交验证码！")
    @Pattern(regexp = "[0-9]{4}",message = "验证码必须是4位数字！")
    private String code;
}
