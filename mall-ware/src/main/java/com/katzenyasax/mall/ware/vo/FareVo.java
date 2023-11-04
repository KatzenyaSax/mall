package com.katzenyasax.mall.ware.vo;

import com.katzenyasax.common.to.MemberAddressTO;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-07-04 23:19
 **/

@Data
public class FareVo {

    private MemberAddressTO address;

    private BigDecimal fare;

}
