package com.taotao.search.controller;

import com.taotao.search.bean.SearchResult;
import com.taotao.search.service.SearchService;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    private static final Integer pageSize = 36;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView search(@RequestParam("q") String keywords,
                               @RequestParam(value = "page", defaultValue = "1") Integer pageNum){

        ModelAndView mav = new ModelAndView("search");
        //设置搜索关键词
        mav.addObject("query", keywords);
        //查询solr索引库获取商品信息
        SearchResult searchResult = null;
        try {
            searchResult = searchService.queryItemListByKeyWords(keywords, pageNum, pageSize);
        } catch (SolrServerException e) {
            e.printStackTrace();
            searchResult = new SearchResult();
        }
        mav.addObject("itemList", searchResult.getData());
        //放入当前页码
        mav.addObject("page", pageNum);
        //计算总页数
        //总条数除以pageSize
        int total = searchResult.getTotal().intValue();
        Integer pages = total % pageSize == 0 ? total / pageSize : (total / pageSize) + 1;
        mav.addObject("pages", pages);
        return mav;
    }
}
