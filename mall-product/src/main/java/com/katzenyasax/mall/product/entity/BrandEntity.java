package com.katzenyasax.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.katzenyasax.mall.product.valid.InsertGroup;
import com.katzenyasax.mall.product.valid.NumbersIWant;
import com.katzenyasax.mall.product.valid.UpdateGroup;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.UniqueElements;

/**
 * 品牌
 * 
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:16:41
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
	@NotNull(message = "更新数据时，必须指定id",groups = {UpdateGroup.class})
	@Null(message = "插入时，禁止指定id",groups = {InsertGroup.class})
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "插入时，必须指定name",groups = {InsertGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@URL(message = "品牌logo必须是合法的URL",groups = {InsertGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
	@NotBlank(message = "插入时，禁止指定descript",groups = {InsertGroup.class})
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	//@NotNull(message = "插入时，禁止指定showStatus",groups = {InsertGroup.class})
	@NumbersIWant(value = {0,1},groups = {InsertGroup.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@Pattern(regexp="[a-zA-Z]",message = "插入时，禁止指定firstLetter",groups = {InsertGroup.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(message = "插入时，禁止指定sort",groups = {InsertGroup.class})
	@Min(value = 0,message = "排序必须大于等于0")
	private Integer sort;

}
