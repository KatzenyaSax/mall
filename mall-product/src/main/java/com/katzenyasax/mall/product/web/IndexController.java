package com.katzenyasax.mall.product.web;


import cn.hutool.crypto.Mode;
import com.katzenyasax.mall.product.entity.CategoryEntity;
import com.katzenyasax.mall.product.service.CategoryService;
import com.katzenyasax.mall.product.vo.catalogVO.Catalog2VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;


    /**
     *
     * @param model
     * @return
     *
     * 查出所有的一级分类
     * 返回值为index，表示将index.html返回
     * 其次手动定义一个model存储一级菜单连带传给前端
     *
     */
    @GetMapping({"/","/index"})
    public String getIndex(Model model){
        /*
        查出所有的一级分类
        对于分类，我们有categoryService进行查询，但是默认查的是三级分类
        我们再在里面创建一个方法查一级分类就好了
         */
        List<CategoryEntity> ones=categoryService.listOne();
        /*
        model是springmvc提供的一个接口，用于传递数据的
         */
        model.addAttribute("categoriesOne",ones);
        return "index";
    }


    /**
     *
     * @return
     *
     * 查询一级分类下所有的二三级菜单，返回一级分类的Map
     *
     */
    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catalog2VO>> getCatalogJson(){
        Long l=System.currentTimeMillis();
        Map<String, List<Catalog2VO>> map=categoryService.getCatalogJson();
        System.out.println("list token："+(System.currentTimeMillis()-l));
        return map;
    }


}
