package com.katzenyasax.common.constant;

import lombok.Data;

public class ProductConstant {


    /**
     *
     * 这个是product里面的所有常量的表单
     *
     * 消除魔法值
     *
     *
     *
     */

    public enum AttrEnum {
        ATTR_TYPE_BASE(1,"普通参数"),
        ATTR_TYPE_SALE(0,"销售属性"),
        ATTR_TYPE_BOTH(2,"二者皆是");
        Integer code;
        String msg;
        AttrEnum(Integer code,String msg){
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
