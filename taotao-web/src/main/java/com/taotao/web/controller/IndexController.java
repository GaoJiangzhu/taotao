package com.taotao.web.controller;

import com.taotao.web.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping
public class IndexController {

    @Autowired
    private IndexService indexService;

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public ModelAndView toIndex() {

        ModelAndView mv = new ModelAndView("index");
        //获取大广告的数据,返回给前台
        String ad1 = this.indexService.queryAd1();
        //获取右上角小广告的数据
//        String ad2 = this.indexService.queryAd2();
        /**
         * todo
         * 获取其他位置的广告数据,,右侧小广告...........
         */
        mv.addObject("ad1", ad1);
//        mv.addObject("ad2", ad2);

        return mv;

    }
}
