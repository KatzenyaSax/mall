package com.katzenyasax.mall.search.controller;


import com.katzenyasax.mall.search.service.SearchService;
import com.katzenyasax.mall.search.vo.SearchParam;
import com.katzenyasax.mall.search.vo.SearchResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
@Controller
public class SearchController {

    @Autowired
    SearchService searchService;


    /**
     *
     * @return
     *
     * 根据条件VO，检索满足条件的
     *
     * HttpServletRequest获取url
     *
     */
    @GetMapping("list.html")
    public SearchResult listHtml(SearchParam param, HttpServletRequest request) throws UnsupportedEncodingException {


        String queryString = request.getQueryString();
        //queryString=URLEncoder.encode(queryString,"UTF-8");
        param.set_queryString(queryString);

        SearchResult result=searchService.search(param);
        return result;
    }


    @GetMapping("results")
    public SearchResult results(SearchParam vo){
        SearchResult result=searchService.search(vo);
        return result;
    }



}
