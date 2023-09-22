package com.katzenyasax.mall.ware.vo;


import lombok.Data;

import java.util.List;

@Data
public class PurchaseVO_IdAndErrorPurchaseDetail {
    private Long id;
    private List<ErrorItem> items;

}
