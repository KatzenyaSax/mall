package com.katzenyasax.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.katzenyasax.common.to.SeckillSessionTO;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.coupon.entity.SeckillSessionEntity;

import java.util.List;
import java.util.Map;

/**
 * 秒杀活动场次
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 08:49:54
 */
public interface SeckillSessionService extends IService<SeckillSessionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    R uploadSession(List<Long> sessionIds);

    List<Long> selectInThreeDays();

    List<SeckillSessionTO> selectCurrentSession();

    void removeSessions(List<Long> list);

    R saveSession(SeckillSessionEntity seckillSession);
}

