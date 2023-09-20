package com.katzenyasax.mall.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.katzenyasax.mall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

@Data
public class AttrGroupVO_WithAttrs {
    private static final long serialVersionUID = 1L;

    /**
     * 分组id
     */
    @TableId
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    private List<AttrEntity> attrs;
}
