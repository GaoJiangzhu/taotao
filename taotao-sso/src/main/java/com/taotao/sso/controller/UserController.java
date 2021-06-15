package com.taotao.sso.controller;

import com.taotao.commen.utils.CookieUtils;
import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String toRegister() {

        return "register";
    }

    /**
     * 判断数据是否可用
     */
    @RequestMapping(value = "check/{param}/{type}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> checkEnabled(@PathVariable("param") String param,
                                                @PathVariable("type") Integer type) {
        try {
            Boolean bol = this.userService.checkEnabled(param, type);
            return ResponseEntity.ok(!bol);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 注册
     * 不加ResponseBody报错404
     */
    @ResponseBody
    @RequestMapping(value = "doRegister", method = RequestMethod.POST)
    public Map<String, Object> doRegister(@Valid User user, BindingResult bindingResult) {

        Map<String, Object> result = new HashMap<>();
        //BindingResult bindingResult获取错误信息的作用
        //当数据跳过前台直接请求后台时候,验证数据是否符合要求
        //是否存在不合法的数据信息
        if (bindingResult.hasErrors()) {
            //获取错误信息
            List<ObjectError> errors = bindingResult.getAllErrors();
            List<String> msgList = new ArrayList<>();
            for (ObjectError error : errors) {
                msgList.add(error.getDefaultMessage());
            }
            result.put("status", "400");//表示参数有问题
            result.put("data", StringUtils.join(msgList, "|"));//参数有问题之后返回提示信息
            return result;
        }

        try {
            Boolean bol = this.userService.doRegister(user);
            if (bol) {
                result.put("status", "200");
                return result;
            }
            result.put("status", "500");
            result.put("data", "不好意思，注册失败");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "500");
            result.put("data", "不好意思，注册失败");
        }
        return result;
    }

    /**
     * 登录页面
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String toLogin() {

        return "login";
    }

    /**
     * 单点登录
     * 登陆成功需要生成token
     * 并且放到cookie中
     */
    @ResponseBody
    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    public Map<String, Object> doLogin(@RequestParam("username") String username,
                                       @RequestParam("password") String password,
                                       HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> result = new HashMap<>();
        try {
            result = new HashMap<>();
            String token = this.userService.doLogin(username, password);
            if (StringUtils.isNotEmpty(token)) {
                //登陆成功
                result.put("status", 200);
                /**
                 * 将token放到cookie
                 */
                CookieUtils.setCookie(request, response, "TT_USER_TOKEN", token);
            } else {
                //登录失败
                result.put("status", 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", 500);
        }
        return result;
    }

    /**
     * 用户退出
     *
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        final String cookieValue = CookieUtils.getCookieValue(request, "TT_USER_TOKEN");
        this.userService.logout(cookieValue);
    }

}
