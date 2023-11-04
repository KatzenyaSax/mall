package com.katzenyasax.mall.ware.exception;

public class NoStockException extends RuntimeException{
    private Long skuId;
    public NoStockException(Long skuId){
        super(skuId+"号商品库存不足！");
    }
    public Long getSkuId() {
        return skuId;
    }
    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }
}
