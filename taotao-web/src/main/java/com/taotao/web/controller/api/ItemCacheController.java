package com.taotao.web.controller.api;

import com.taotao.commen.service.RedisService;
import com.taotao.web.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("item/cache")
public class ItemCacheController {

    @Autowired
    private RedisService redisService;

    //根据缓存的Key删除对应商品的缓存
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ResponseEntity<Void> deleteItemCache(@PathVariable("itemId") Long itemId){
        try {
            this.redisService.del(ItemService.ITEM_KEY + itemId);
            this.redisService.del(ItemService.ITEM_DESC_KEY + itemId);
            this.redisService.del(ItemService.ITEM_PARAM_ITEM_KEY + itemId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
