package com.taotao.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.commen.service.ApiService;
import com.taotao.commen.service.RedisService;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.web.bean.Item;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ItemService {

    @Autowired
    private ApiService apiService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static final String ITEM_KEY = "TT_ITEM_DETAIL_KEY";
    public static final String ITEM_DESC_KEY = "TT_ITEM_DESC_KEY";
    public static final String ITEM_PARAM_ITEM_KEY = "TT_ITEM_PARAM_ITEM_KEY";
    public static final Integer ITEM_TIME = 60 * 60 * 24;

    @Autowired
    private RedisService redisService;

    public Item queryItemById(Long itemId) {
        //前台系统,查询数据库?-->请求后台提供的接口来查询
        try {
            try {
                //先查询缓存
                //加上商品编号,才能确保确保从缓存中取出的是不同的商品
                String itemJsonData = this.redisService.get(ITEM_KEY + itemId);
                if (StringUtils.isNotEmpty(itemJsonData)) {
                    //找到直接将获取到的字符串反序列化成item对象
                    return MAPPER.readValue(itemJsonData, Item.class);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String url = "http://manage.taotao.com/rest/api/item/" + itemId;
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isNotEmpty(jsonData)) {
                //将json字符串反序列化成一个Item对象

                try {
                    //将查询到的数据放到redis中
                    this.redisService.set(ITEM_KEY + itemId, jsonData, ITEM_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return MAPPER.readValue(jsonData, Item.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemDesc queryItemDescById(Long itemId) {
        try {
            /**
             *
             * todo
             * 加缓存
             */
            try {

                String itemDescJsonData = this.redisService.get(ITEM_DESC_KEY + itemId);
                if (StringUtils.isNotEmpty(itemDescJsonData)) {
                    //找到直接将获取到的字符串反序列化成item对象
                    return MAPPER.readValue(itemDescJsonData, ItemDesc.class);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String url = "http://manage.taotao.com/rest/api/item/desc/" + itemId;
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isNotEmpty(jsonData)) {
                //将json字符串反序列化成一个Item对象
                try {
                    //将查询到的数据放到redis中
                    this.redisService.set(ITEM_DESC_KEY + itemId, jsonData, ITEM_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return MAPPER.readValue(jsonData, ItemDesc.class);
            }
            /**
             *
             * todo
             * 设置缓存
             */
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String queryItemParamItemByItemId(Long itemId) {
        try {
            /**
             *
             *
             * 加缓存
             */
            try {
                String itemParamItemJsonData = this.redisService.get(ITEM_PARAM_ITEM_KEY + itemId);
                if (StringUtils.isNotEmpty(itemParamItemJsonData)) {
                    //找到直接将获取到的字符串反序列化成item对象
                    return itemParamItemJsonData;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String url = "http://manage.taotao.com/rest/api/item/param/item/" + itemId;
            String jsonData = this.apiService.doGet(url);
            if (StringUtils.isNotEmpty(jsonData)) {
                JsonNode jsonNode = MAPPER.readTree(jsonData);
                String paramData = jsonNode.get("paramData").asText();
                ArrayNode arrayNode = (ArrayNode) MAPPER.readTree(paramData);
                //ArrayNode arrayNode = (ArrayNode)jsonNode.get("paramData");
                if (arrayNode.size() > 0 && arrayNode != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"><tbody>");
                    for (JsonNode node : arrayNode) {
                        sb.append("<tr><th class=\"tdTitle\" colspan=\"2\">" + node.get("group").asText() + "</th></tr>");
                        ArrayNode params = (ArrayNode) node.get("params");
                        for (JsonNode param : params) {
                            sb.append("<tr><td class=\"tdTitle\">" + param.get("k").asText() + "</td><td>" + param.get("v").asText() + "</td></tr>");
                        }
                    }
                    /**
                     *
                     * 设置缓存
                     */
                    sb.append("</tbody></table>");
                    try {
                        this.redisService.set(ITEM_PARAM_ITEM_KEY + itemId, sb.toString(), ITEM_TIME);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return sb.toString();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
