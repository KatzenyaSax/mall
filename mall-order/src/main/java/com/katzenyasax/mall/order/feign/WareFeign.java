package com.katzenyasax.mall.order.feign;


import com.katzenyasax.common.to.OrderItemTO;
import com.katzenyasax.common.to.WareOrderDetailTO;
import com.katzenyasax.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient("mall-ware")
public interface WareFeign {
    /**
     * 一次性拿取所有有货的商品作为一个map
     */
    @GetMapping("ware/waresku/skuStocks")
    Map<Long,Boolean> getSkuStocks();


    /**
     * 运费和地址
     * 口令为data
     *
     * //todo 先直接返回8
     */
    @RequestMapping("ware/wareinfo/fare")
    R getFare(@RequestParam Long addrId);


    /**
     * order远程调用
     * 锁定orderItems的库存，若库存不足还要返回不足提示
     */
    @RequestMapping("ware/waresku/lockWare")
    Map<Long,Long> lockWare(@RequestBody List<OrderItemTO> items);



    /**
     * order远程调用
     * 存ware_order_task和ware_order_task_detail
     */
    @RequestMapping("ware/waresku/saveTasks")
    void saveTasks(@RequestBody List<WareOrderDetailTO> to);

}
