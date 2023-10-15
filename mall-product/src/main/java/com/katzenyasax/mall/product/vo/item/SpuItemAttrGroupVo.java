package com.katzenyasax.mall.product.vo.item;

import com.katzenyasax.mall.product.vo.spu.Attr;
import jakarta.annotation.security.DenyAll;
import lombok.Data;

import java.util.List;

@Data
public class SpuItemAttrGroupVo {
    private String groupName;

    private List<Attr> attrs;
}
