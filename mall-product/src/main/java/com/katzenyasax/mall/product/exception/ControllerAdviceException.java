package com.katzenyasax.mall.product.exception;


import com.katzenyasax.common.exception.BizCodeEnume;
import com.katzenyasax.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j      //输出日志
//@ControllerAdvice(basePackages = "com.katzenyasax.mall.product.controller")
@RestControllerAdvice(basePackages = "com.katzenyasax.mall.product.controller")
//包含了：ControllerAdvice和ResponseBodylocalhost:10100/api/product/brand/save


public class ControllerAdviceException {


    /**
            处理数据不合法异常
            @Valid
     **/

    //@ResponseBody   //要以json格式返回数据
    @ExceptionHandler(value = MethodArgumentNotValidException.class)      //表示可处理的异常
    public R handlerValidException(MethodArgumentNotValidException e){
        log.error("数据不合法",e.getMessage(),e.getClass());     //日志输出异常
        BindingResult result=e.getBindingResult();
        Map<String,String> map=new HashMap<>();
        result.getFieldErrors().forEach((item)->{
            String msg=item.getDefaultMessage();
            String fld=item.getField();
            map.put(fld,msg);
        });
        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(),BizCodeEnume.VAILD_EXCEPTION.getMsg()).put("data",map);
    }





    /**

            处理所有异常

    */

    @ExceptionHandler(value = Throwable.class)
    public R handlerThrowable(Throwable e){
        return R.error();
    }





}
