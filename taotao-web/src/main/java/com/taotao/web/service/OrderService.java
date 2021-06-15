package com.taotao.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.commen.bean.HttpResult;
import com.taotao.commen.service.ApiService;
import com.taotao.web.bean.Item;
import com.taotao.web.bean.Order;
import com.taotao.web.bean.User;
import com.taotao.web.threadLocal.UserThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OrderService {

    @Autowired
    private ApiService apiService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    //根据商品ID查询商品信息
    public Item queryItemByItemId(Long itemId) {

        try {
            String url = "http://manage.taotao.com/rest/api/item/" + itemId;
            String jsonData = this.apiService.doGet(url);
            //将字符串反序列化成对象
            if(StringUtils.isNotEmpty(jsonData)){
                return MAPPER.readValue(jsonData, Item.class);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String createOrder(Order order) {
        //调用订单系统创建订单
        String url = "http://order.taotao.com/order/create";
        //将Order序列化成json字符串,发送post请求,数据为json字符串
        try {
            //从threadlocal中获取信息
            User user = UserThreadLocal.get();
            //将用户的信息填充到订单中
            order.setUserId(user.getId());
            order.setBuyerNick(user.getUsername());
            HttpResult httpResult = this.apiService.doPostJson(url, MAPPER.writeValueAsString(order));
            if(httpResult.getCode().intValue() == 200){//判断请求状态成功
                //获取订单系统返回的数据
                String body = httpResult.getBody();
                JsonNode jsonNode = MAPPER.readTree(body);
                if(jsonNode.get("status").asInt() == 200){//订单创建成功
                    return jsonNode.get("data").asText();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Order queryOrderById(String orderId) {
        //根据订单号查询订单信息
        try {
            String url = "http://order.taotao.com/order/query/" + orderId;
            String jsonData = this.apiService.doGet(url);
            if(StringUtils.isNotEmpty(jsonData)){
                return MAPPER.readValue(jsonData, Order.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
