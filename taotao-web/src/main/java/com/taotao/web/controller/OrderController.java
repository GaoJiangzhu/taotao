package com.taotao.web.controller;

import com.taotao.web.bean.Item;
import com.taotao.web.bean.Order;
import com.taotao.web.bean.User;
import com.taotao.web.interceptor.UserLoginInterceptor;
import com.taotao.web.service.OrderService;
import com.taotao.web.service.UserService;
import com.taotao.web.threadLocal.UserThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    //去订单页确认
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    public ModelAndView toOrderPage(@PathVariable("itemId") Long itemId){

        ModelAndView mv = new ModelAndView("order");
        Item item = this.orderService.queryItemByItemId(itemId);
        mv.addObject("item", item);
        return mv;
    }

    /**
     * 提交订单
     */
    @RequestMapping(value = "submit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> submitOrder(Order order){

        Map<String, Object> result = new HashMap<>();
        try {

            String orderId = this.orderService.createOrder(order);//提交成功返回订单号
            if(StringUtils.isNotEmpty(orderId)){
                result.put("status", 200);
                result.put("data", orderId);
            }else{
                result.put("status", 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", 500);
        }
        return result;
    }

    /**
     * 下单成功后访问成功页面
     */
    @RequestMapping(value = "success", method = RequestMethod.GET)
    public ModelAndView toSuccess(@RequestParam("id") String orderId){
        ModelAndView modelAndView = new ModelAndView("success");
        Order order = this.orderService.queryOrderById(orderId);
        modelAndView.addObject("order", order);

        //预计送达天数
        modelAndView.addObject("date", new DateTime().plusDays(2).toString("MM月dd"));
        return modelAndView;
    }

}
