package com.katzenyasax.mall.product.vo.catalogVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catalog2VO {
    private String catalog1Id;
    private List<Catalog3VO> catalog3List;
    private String id;
    private String name;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Catalog3VO{
        private String catalog2Id;
        private String id;
        private String name;
    }
}
