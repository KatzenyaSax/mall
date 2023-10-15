package com.katzenyasax.mall.search.service;

import com.katzenyasax.mall.search.vo.SearchParam;
import com.katzenyasax.mall.search.vo.SearchResult;

public interface SearchService {

    SearchResult search(SearchParam vo);


}
