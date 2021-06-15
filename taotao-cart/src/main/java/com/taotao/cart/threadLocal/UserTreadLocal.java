package com.taotao.cart.threadLocal;

import com.taotao.cart.pojo.User;

public class UserTreadLocal {

    //定义threadLocal对象
    private static final ThreadLocal<User> LOCAL = new ThreadLocal<>();

    public static void set(User user){
        LOCAL.set(user);
    }

    public static User get(){
        return LOCAL.get();
    }
}
