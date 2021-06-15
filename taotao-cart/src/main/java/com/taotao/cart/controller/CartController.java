package com.taotao.cart.controller;

import com.taotao.cart.pojo.Cart;
import com.taotao.cart.pojo.User;
import com.taotao.cart.service.CartCookieService;
import com.taotao.cart.service.CartService;
import com.taotao.cart.threadLocal.UserTreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("cart")
public class CartController {

    //登录状态下
    @Autowired
    private CartService cartService;

    //未登录状态下
    @Autowired
    private CartCookieService cartCookieService;
    //添加商品到购物车
    //添加商品成功后需要做什么?1/添加成功页面  2.查询当前用户对应的购物车界面
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public String addItem2Cart(@PathVariable("itemId") Long itemId,
                               HttpServletRequest request,
                               HttpServletResponse response){

        //判断是否登录,从threadlocal中获取用户
        User user = UserTreadLocal.get();
        if(null == user){
            //未登录
            try {
                this.cartCookieService.addItem2Cart(itemId, request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            //已登录
            this.cartService.addItem2Cart(itemId);
        }
        return "redirect:/cart/list.html";
    }

    //查询购物车信息
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView queryCarts(HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("cart");
        //判断是否登录,从threadlocal中获取用户
        User user = UserTreadLocal.get();
        List<Cart> cartList = null;
        if(null == user){
            //未登录
            try {
                cartList = this.cartCookieService.queryCarts(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            //已登录
            cartList = this.cartService.queryCarts();
        }
        mav.addObject("cartList", cartList);
        return mav;
    }

    //修改商品的数量
    @RequestMapping(value = "update/num/{itemId}/{num}", method = RequestMethod.POST)
    public String updateNum(@PathVariable("itemId") Long itemId,
                            @PathVariable("num") Integer num,
                            HttpServletRequest request,
                            HttpServletResponse response){

        //判断是否登录,从threadlocal中获取用户
        User user = UserTreadLocal.get();
        List<Cart> cartList = null;
        if(null == user){
            //未登录
            try {
                this.cartCookieService.updateNum(itemId, num, request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            //已登录
            this.cartService.updateNum(itemId, num);
        }
        return "redirect:/cart/list.html";
    }

    /**
     * 删除购物车中的商品
     * 删除分为两种:
     * 1.逻辑删除  该状态
     * 2.物理删除  删除数据库
     */
    @RequestMapping(value = "delete/{itemId}", method = RequestMethod.GET)
    public String deleteCart(@PathVariable("itemId") Long itemId,
                             HttpServletRequest request,
                             HttpServletResponse response){

        //判断是否登录,从threadlocal中获取用户
        User user = UserTreadLocal.get();
        List<Cart> cartList = null;
        if(null == user){
            //未登录
            try {
                this.cartCookieService.deleteCart(itemId, request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            //已登录
            this.cartService.deleteCart(itemId);
        }
        return "redirect:/cart/list.html";
    }
}
