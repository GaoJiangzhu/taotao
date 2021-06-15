package com.taotao.cart.service;

import com.github.abel533.entity.Example;
import com.taotao.cart.bean.Item;
import com.taotao.cart.mapper.CartMapper;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.threadLocal.UserTreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ItemService itemService;

    //登录状态下添加商品到购物车
    public void addItem2Cart(Long itemId) {

        //判断当前用户是否存在该商品的购物车
        Cart record = new Cart();
        record.setItemId(itemId);
        record.setUserId(UserTreadLocal.get().getId());
        Cart cart = this.cartMapper.selectOne(record);

        //逻辑思想:如果找到了购物车对象那么就将商品数量累加; 如果没有找到该购物车,那么久就创建购物车对象
        if(null == cart){
            //没有当前购物车
            cart = new Cart();
            //填充信息
            cart.setUserId(UserTreadLocal.get().getId());
            cart.setCreated(new Date());
            cart.setUpdated(new Date());

            cart.setNum(1);
            cart.setItemId(itemId);
            //下面的商品基本信息需要查询,根据商品Id到后台查询
            Item item = this.itemService.queryItemById(itemId);
            cart.setItemTitle(item.getTitle());
            cart.setItemImage(StringUtils.split(item.getImage(), ",")[0]);
            cart.setItemPrice(item.getPrice());
            this.cartMapper.insert(cart);
        }else{
            //当前购物车存在,那么商品数量相加,默认加一 TODO
            cart.setNum(cart.getNum() + 1);
            cart.setUpdated(new Date());
            this.cartMapper.updateByPrimaryKey(cart);
        }
    }

    /**
     * 登录状态下查询购物车
     * @return
     */
    public List<Cart> queryCarts() {

        Example example = new Example(Cart.class);
        //设置排序
        example.setOrderByClause("created DESC");
        //设置查询条件
        example.createCriteria().andEqualTo("userId", UserTreadLocal.get().getId());
        List<Cart> cartList = this.cartMapper.selectByExample(example);
        return cartList;
    }


    /**
     * 修改购物车商品数量
     * @param itemId
     * @param num
     */
    public void updateNum(Long itemId, Integer num) {

        //需要修改的值
        Cart cart = new Cart();
        cart.setNum(num);
        cart.setUpdated(new Date());
        //设置修改条件
        Example example = new Example(Cart.class);
        example.createCriteria().andEqualTo("itemId", itemId)
                .andEqualTo("userId", UserTreadLocal.get().getId());
        this.cartMapper.updateByExampleSelective(cart, example);

    }

    public void deleteCart(Long itemId) {

        Cart cart = new Cart();
        cart.setItemId(itemId);
        cart.setUserId(UserTreadLocal.get().getId());
        this.cartMapper.delete(cart);
    }
}
