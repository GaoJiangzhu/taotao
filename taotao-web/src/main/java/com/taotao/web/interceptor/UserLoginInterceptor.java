package com.taotao.web.interceptor;

import com.taotao.commen.utils.CookieUtils;
import com.taotao.web.bean.User;
import com.taotao.web.service.UserService;
import com.taotao.web.threadLocal.UserThreadLocal;
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


    /**
     * 在进入handler之前执行
     *
     * @param request
     * @param response
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        //判断是否登录
        String token = CookieUtils.getCookieValue(request, COOKIE_NAME);
        if (StringUtils.isEmpty(token)) {
            //没有登录
            response.sendRedirect("http://sso.taotao.com/user/login.html");
            return false;
        }
        User user = this.userService.queryUserByToken(token);
        //根据token查找用户信息,单点登录查询,接口调用
        if (user == null) {

            response.sendRedirect("http://sso.taotao.com/user/login.html");
            return false;
        }
        //查询到用户的信息之后放到threadlocal本地线程内
        //作用:在同一个线程内可以共享数据
        UserThreadLocal.set(user);
        return true;//放行
    }

    /**
     * 在进入handler之后执行
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 在试图渲染之后执行
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //清空threadlocal中用户信息
        UserThreadLocal.set(null);
    }
}
