package com.taotao.web.rabbit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.commen.service.RedisService;
import com.taotao.web.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class ItemMQHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private RedisService redisService;

    public void execute(String msg){

        //接收消息之后需要删除该商品对应的缓存
        //拿到消息,获取商品Id
        try {
            JsonNode jsonNode = MAPPER.readTree(msg);
            Long itemId = jsonNode.get("itemId").asLong();
            //删除redis缓存对应的商品数据
            this.redisService.del(ItemService.ITEM_KEY + itemId);
            this.redisService.del(ItemService.ITEM_DESC_KEY + itemId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
