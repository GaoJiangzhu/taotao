package com.taotao.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.commen.service.ApiService;
import com.taotao.search.bean.Item;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ItemService {

    @Autowired
    private ApiService apiService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public Item queryItemById(Long itemId) {

        String url = "http://manage.taotao.com/rest/api/item/" + itemId;
        try {
            String jsonData = this.apiService.doGet(url);
            if(StringUtils.isNotEmpty(jsonData)){

                return MAPPER.readValue(jsonData, Item.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
