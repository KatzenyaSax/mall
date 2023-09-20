package com.katzenyasax.mall.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.katzenyasax.mall.product.valid.InsertGroup;
import com.katzenyasax.mall.product.valid.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

@Data
public class BrandVO_OnlyIdAndName {
    private Long brandId;
    private String brandName;
}
