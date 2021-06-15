package com.taotao.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.commen.service.ApiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexService {

    @Autowired
    private ApiService apiService;

    private static final ObjectMapper MAPPER = new ObjectMapper();


    /**
     * todo
     * 获取右侧小广告数据以及其他位置广告数据
     */
    public String queryAd2() {

        try {
            String jsonData = this.apiService.doGet("http://manage.taotao.com/rest/api/content?categoryId=38&page=1&rows=1");
            //解析数据
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            ArrayNode rows = (ArrayNode) jsonNode.get("rows");
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            for (JsonNode row : rows) {
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                map.put("width", 310);
                map.put("height", 70);
                map.put("src", row.get("pic").asText());
                map.put("href", row.get("url").asText());
                map.put("alt", row.get("title").asText());
                map.put("widthB", 210);
                map.put("heightB", 70);
                map.put("srcB", row.get("pic").asText());
                result.add(map);
            }
            return MAPPER.writeValueAsString(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取后台大广告数据
     *
     * @return
     */
    public String queryAd1() {

        try {
            //发送请求给后台获取大广告的数据
            String jsonData = this.apiService.doGet("http://manage.taotao.com/rest/api/content?categoryId=39&page=1&rows=6");
            if (StringUtils.isEmpty(jsonData)) {
                return null;
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            ArrayNode arrayNode = (ArrayNode) jsonNode.get("rows");
            Map<String, Object> map = null;
            List<Map<String, Object>> lists = new ArrayList<>();
            for (JsonNode node : arrayNode) {
                map = new LinkedHashMap<>();
                map.put("srcB", node.get("pic").asText());
                map.put("height", 240);
                map.put("alt", node.get("title").asText());
                map.put("width", 670);
                map.put("src", node.get("pic2").asText());
                map.put("widthB", 550);
                map.put("href", node.get("url").asText());
                map.put("heightB", 240);
                lists.add(map);
            }
            return MAPPER.writeValueAsString(lists);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
