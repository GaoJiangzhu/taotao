package com.taotao.cart.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.bean.Item;
import com.taotao.cart.pojo.Cart;
import com.taotao.commen.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CartCookieService {

    private static final String COOKIE_NAME = "TT_COOKIE";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Integer COOKIE_TIME = 60 * 60 * 24 * 30;

    @Autowired
    private ItemService itemService;

    public void addItem2Cart(Long itemId, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //判断cookie中是否存在购物车
        /*String jsonData = CookieUtils.getCookieValue(request, COOKIE_NAME);
        List<Cart> cartList = null;
        if(StringUtils.isEmpty(jsonData)){
            //没有购物车
            cartList = new ArrayList<>();
        }else{
            //存在购物车,需要将商品拿出来加一
            cartList = MAPPER.readValue(jsonData, MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
        }*/
        List<Cart> cartList = queryCarts(request);
        //获取的是一个购物车的集合. 要给购物车集合中的某一个购物车 修改商品数量
        Cart c = null;
        for (Cart cart : cartList) {
            //比对商品的Id
            if(cart.getItemId().longValue() == itemId.longValue()){
                c = cart;
                break;
            }
        }
        if(c == null){
            //没有当前购物车
            c = new Cart();
            //填充信息
            c.setCreated(new Date());
            c.setUpdated(new Date());
            c.setNum(1);
            c.setItemId(itemId);
            //下面的商品基本信息需要查询,根据商品Id到后台查询
            Item item = this.itemService.queryItemById(itemId);
            c.setItemTitle(item.getTitle());
            c.setItemImage(StringUtils.split(item.getImage(), ",")[0]);
            c.setItemPrice(item.getPrice());
            cartList.add(c);


        }else{
            //更改数量
            c.setNum(c.getNum() + 1);
            c.setUpdated(new Date());
        }
        //需要将购物车信息写入cookie
        CookieUtils.setCookie(request, response, COOKIE_NAME, MAPPER.writeValueAsString(cartList), COOKIE_TIME);
        /*Cookie cookie = new Cookie(COOKIE_NAME, MAPPER.writeValueAsString(cartList));
        cookie.setPath(request.getContextPath());
        cookie.setMaxAge(COOKIE_TIME);
        response.addCookie(cookie);*/
    }

    public List<Cart> queryCarts(HttpServletRequest request) throws Exception {

        //判断cookie中是否存在购物车
        String jsonData = CookieUtils.getCookieValue(request, COOKIE_NAME);
        /*Cookie[] cookies = request.getCookies();
        String jsonData = "";
        for (Cookie cookie : cookies) {
            if(StringUtils.equals(COOKIE_NAME, cookie.getName())){
                jsonData = cookie.getValue();
                break;
            }
        }*/
        if(StringUtils.isEmpty(jsonData)){
            //没有购物车
            return new ArrayList<>(0);
        }else{
            //存在购物车,需要将商品拿出来加一
            return MAPPER.readValue(jsonData,
                    MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
        }
    }


    public void updateNum(Long itemId, Integer num, HttpServletRequest request, HttpServletResponse response)throws Exception {

        //先取出所有的购物车信息
        List<Cart> cartList = queryCarts(request);

        //筛选当前商品对应的购物车
        Cart c = null;
        for (Cart cart : cartList) {
            //比对商品的id
            if(cart.getItemId().longValue() == itemId.longValue()){
                //修改数量
                c = cart;
                c.setUpdated(new Date());
                c.setNum(c.getNum() + 1);
                break;
            }
        }

        //写入cookie
        CookieUtils.setCookie(request, response, COOKIE_NAME, MAPPER.writeValueAsString(cartList), COOKIE_TIME);
        /*Cookie cookie = new Cookie(COOKIE_NAME, MAPPER.writeValueAsString(cartList));
        cookie.setPath(request.getContextPath());
        cookie.setMaxAge(COOKIE_TIME);
        response.addCookie(cookie);*/
    }

    public void deleteCart(Long itemId, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //先取出所有的购物车信息
        List<Cart> cartList = queryCarts(request);

        //筛选当前商品对应的购物车
        for (Cart cart : cartList) {
            //比对商品的id
            if(cart.getItemId().longValue() == itemId.longValue()){
                //删除操作
                cartList.remove(cart);
                break;
            }
        }
        //写入cookie
        CookieUtils.setCookie(request, response, COOKIE_NAME, MAPPER.writeValueAsString(cartList), COOKIE_TIME);
        /*Cookie cookie = new Cookie(COOKIE_NAME, MAPPER.writeValueAsString(cartList));
        cookie.setPath(request.getContextPath());
        cookie.setMaxAge(COOKIE_TIME);
        response.addCookie(cookie);*/
    }
}
