package com.taotao.web.threadLocal;

import com.taotao.web.bean.User;

public class UserThreadLocal {

    //定义一个threadlocal   需要指定泛型,
    private static final ThreadLocal<User> LOCAL = new ThreadLocal<>();

    /**
     * 存取都是根据线程id
     * @return
     */

    //存放数据
    public static void set(User user){
        LOCAL.set(user);
    }
    //取出数据
    public static User get(){
        return LOCAL.get();
    }
}
