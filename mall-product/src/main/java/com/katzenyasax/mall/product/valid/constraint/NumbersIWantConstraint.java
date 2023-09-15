package com.katzenyasax.mall.product.valid.constraint;

import com.katzenyasax.mall.product.valid.NumbersIWant;
import io.swagger.models.auth.In;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class NumbersIWantConstraint implements ConstraintValidator<NumbersIWant,Integer> {
    private Set<Integer> set=new HashSet<>();
    @Override
    public void initialize(NumbersIWant constraintAnnotation) {
        int[] values=constraintAnnotation.value();
        for(int n:values){
            set.add(n);
        }
        log.info(set.toString());
    }
    @Override
    public boolean isValid(Integer integer/** 这里的integer是提交的值 **/, ConstraintValidatorContext constraintValidatorContext) {
        log.info(String.valueOf(set.contains(integer)));
        return set.contains(integer);
    }
}
