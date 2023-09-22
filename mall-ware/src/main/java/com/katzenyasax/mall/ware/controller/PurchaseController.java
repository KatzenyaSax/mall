package com.katzenyasax.mall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.katzenyasax.mall.ware.vo.PurchaseVO_IdAndErrorPurchaseDetail;
import com.katzenyasax.mall.ware.vo.PurchaseVO_Merge;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.katzenyasax.mall.ware.entity.PurchaseEntity;
import com.katzenyasax.mall.ware.service.PurchaseService;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.R;



/**
 * 采购信息
 *
 * @author katzenyasax
 * @email a18290531268@163.com
 * @date 2023-09-09 13:17:29
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;



    /**
     * 查询未领取的采购单
     * 也即是状态为0还未分配的采购单，或是状态为1刚分配给人还未处理的采购单
     */
    @RequestMapping("/unreceive/list")
    public R listUnreceive(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.getUnreceive(params);

        return R.ok().put("page", page);
    }

    /**
     *
     * @param vo
     * @return
     *
     * 合并从采购需求为采购单
     * params包含purchaseId和数组
     * 代表要合并需求到哪一个采购单
     * 和所有需要合并到采购单的需求
     *
     * purchaseId为空时，需要自己创建一个新的采购单
     *
     */
    @RequestMapping("/merge")
    public R merge(@RequestBody PurchaseVO_Merge vo){
        purchaseService.mergePurchse(vo);
        return R.ok();
    }



    /**
     *
     * @param ids
     * @return
     *
     * 领取采购单
     * 根据前端传回的ids，改变采购单的状态status
     *
     */
    @RequestMapping("/received")
    public R receivePurchase(@RequestBody List<Long> ids){
        purchaseService.receivePurchase(ids);
        return R.ok();
    }



    /**
     *
     * @param vo
     * @return
     *
     * 完成采购
     * 并将对应的所有采购需求改为采购完成
     * 此外若items不为空，需要更改items中对应的采购需求的状态为采购失败，采购单也改成异常
     * 而采购成功的需求则更改状态为完成，并且入库
     *
     */
    @RequestMapping("/done")
    public R receiveDone(@RequestBody PurchaseVO_IdAndErrorPurchaseDetail vo){
        purchaseService.donePurchase(vo);
        return R.ok();
    }






    //============================================
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
