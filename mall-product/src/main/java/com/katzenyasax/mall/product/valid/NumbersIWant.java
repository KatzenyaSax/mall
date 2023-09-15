package com.katzenyasax.mall.product.valid;

import com.katzenyasax.mall.product.valid.constraint.NumbersIWantConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {NumbersIWantConstraint.class})
public @interface NumbersIWant {
    String message() default "${com.katzenyasax.mall.product.valid.NumbersIWant.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int[] value();
}
