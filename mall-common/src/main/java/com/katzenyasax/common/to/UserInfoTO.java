package com.katzenyasax.common.to;

import lombok.Data;
import lombok.ToString;
@ToString
@Data
public class UserInfoTO {

    /**
     * 若已登录，则userId非空，且为当前用户的id
     * 若未登录，则userKey是临时用户
     */
    private Long userId;

    private String userKey;

    /**
     * 是否临时用户
     */
    private Boolean tempUser = false;

}
