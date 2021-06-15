package com.taotao.web.controller;

import com.taotao.commen.bean.HttpResult;
import com.taotao.commen.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private ApiService apiService;

    /**
     * 登录，注册 页面跳转
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "{page}", method = RequestMethod.GET)
    public String toPage(@PathVariable("page") String page) {


        return page;
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    public Map doLogin(@RequestParam("username") String username,
                       @RequestParam("password") String password) {

        HashMap<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        try {
            HttpResult result = this.apiService.doPost("http://sso.taotao.com/service/user/doLogin", map);
            HashMap<String, Integer> hashMap = new HashMap<>();
            hashMap.put("status", result.getCode());
            return hashMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try {
//            if (StringUtils.isNotEmpty(token)) {
//                //登陆成功
//                result.put("status", 200);
//                /**
//                 * 将token放到cookie
//                 */
//                CookieUtils.setCookie(request, response, "TT_USER_TOKEN", token);
//            } else {
//                //登录失败
//                result.put("status", 500);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.put("status", 500);
//        }
        return null;
    }
}
