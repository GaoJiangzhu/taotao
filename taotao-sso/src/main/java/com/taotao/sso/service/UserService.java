package com.taotao.sso.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.commen.service.RedisService;
import com.taotao.sso.mapper.UserMapper;
import com.taotao.sso.pojo.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 验证用户注册是否可用
     *
     * @param param
     * @param type
     * @return
     */
    public Boolean checkEnabled(String param, Integer type) {
        //判断要验证的内容
        //数据验证
        User user = new User();
        switch (type) {
            case 1://验证用户名
                user.setUsername(param);
                break;
            case 2://验证手机号
                user.setPhone(param);
                break;
            case 3://验证邮箱
                user.setEmail(param);
                break;
        }

        User u = this.userMapper.selectOne(user);

        return u == null;
    }

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    public Boolean doRegister(User user) {
        user.setId(null);
        user.setCreated(new Date());
        user.setUpdated(new Date());

        //密码需要加密处理
        //Md5加密

        String password = DigestUtils.md5Hex(user.getPassword());
        user.setPassword(password);
        return this.userMapper.insertSelective(user) == 1;
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    public String doLogin(String username, String password) {
        //根据用户名查询
        for (int i = 0; i < 10; i++) {
            System.out.println(username);
            System.out.println(password);
        }
        User user = new User();
        user.setUsername(username);
        User u = this.userMapper.selectOne(user);
        if (u == null) {
            //登录失败
            System.out.println("登录失败");
            return null;
        }
        //验证密码
        //将传过来的密码加密后比对!DigestUtils.md5Hex(password).equals(u.getPassword())
        if (!StringUtils.equals(DigestUtils.md5Hex(password), u.getPassword())) {
            System.out.println("密码不对");
            return null;
        }
        //登陆成功
        //生成令牌
        String token = DigestUtils.md5Hex(username + System.currentTimeMillis());
        //将用户信息放入redis
        try {
            this.redisService.set("TOKEN" + token, MAPPER.writeValueAsString(u), 60 * 30);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "TOKEN" + token;
    }

    public User queryUserByToken(String token) {
        try {
            String jsonData = this.redisService.get(token);
            if (StringUtils.isNotEmpty(jsonData)) {
                //刷新用户token存活时间，重要
                this.redisService.expire(token, 60 * 30);
                return MAPPER.readValue(jsonData, User.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void logout(String token) {
        this.redisService.del(token);
    }
}
