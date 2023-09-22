package com.katzenyasax.common.constant;

import io.swagger.models.auth.In;

public class WareConstant {

    public enum PurchaseStatusEnum {

        CREATED(0,"新建"),
        ASSIGNED(1,"已分配"),
        RECEIVED(2,"已领取"),
        FINISHED(3,"已完成"),
        HASERROR(4,"异常");
        Integer code;
        String msg;
        PurchaseStatusEnum(Integer code,String msg){
            this.code=code;
            this.msg=msg;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public enum PurchaseDetailStatusEnum {

        CREATED(0,"新建"),
        ASSIGNED(1,"已分配"),
        RECEIVED(2,"采购中"),
        FINISHED(3,"采购完成"),
        HASERROR(4,"异常");
        Integer code;
        String msg;
        PurchaseDetailStatusEnum(Integer code,String msg){
            this.code=code;
            this.msg=msg;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
