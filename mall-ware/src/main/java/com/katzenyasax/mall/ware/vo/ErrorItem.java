package com.katzenyasax.mall.ware.vo;

import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class ErrorItem {
    private Long itemId;
    private Integer status;
    private String reason;
}
