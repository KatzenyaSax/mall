package com.katzenyasax.mall.search.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class SearchController {

    @GetMapping("list.html")
    public String listHtml(){
        return "list";
    }




}
