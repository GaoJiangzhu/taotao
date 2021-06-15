package com.taotao.web.controller;

import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;
import com.taotao.web.bean.Item;
import com.taotao.web.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    //到商品详情页
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ModelAndView toItemDetail(@PathVariable("itemId") Long itemId){
        ModelAndView mv = new ModelAndView("item");
        //查询商品的基本信息
        Item item = this.itemService.queryItemById(itemId);
        mv.addObject("item", item);
        //查询商品的描述信息
        ItemDesc itemDesc = this.itemService.queryItemDescById(itemId);
        mv.addObject("itemDesc", itemDesc);
        //查询商品规格参数
        //并且放到一段html(是一个表格)中去展示
        String itemParam = this.itemService.queryItemParamItemByItemId(itemId);
        mv.addObject("itemParam", itemParam);
        return mv;
    }
}
