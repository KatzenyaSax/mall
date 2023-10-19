package com.katzenyasax.mall.member.exception;

public enum AuthCodeEnume {


    REGISTER_SUCCESS(101000,"注册成功"),

    USERNAME_ALREADY_EXIST(102001,"用户名已存在"),
    PHONE_ALREADY_EXIST(10202,"手机号已存在");








    private int code;
    private String msg;
    AuthCodeEnume(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
