package com.taotao.cart.interceptor;

import com.taotao.cart.pojo.User;
import com.taotao.cart.service.UserService;
import com.taotao.cart.threadLocal.UserTreadLocal;
import com.taotao.commen.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserLoginInterceptor implements HandlerInterceptor {

    public static final String COOKIE_NAME = "TT_USER_TOKEN";

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        //获取token
        String token = CookieUtils.getCookieValue(request, COOKIE_NAME);
        if(StringUtils.isEmpty(token)){
            //不论登录或者没有登录都得放行
            return true;
        }
        User user = this.userService.queryUserByToken(token);
        if(null == user){
            //登录超时 相当于没登录 放行
            return true;
        }

        //登陆成功,将用户信息放到threadlocal
        UserTreadLocal.set(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        UserTreadLocal.set(null);
    }
}
