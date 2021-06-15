package com.taotao.manage.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.commen.bean.ItemCatResult;
import com.taotao.manage.service.ItemCatService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("api/item/cat")
public class ApiItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    //添加了统一jsonp请求处理，所以下面这个方法放弃使用

    /*private static final ObjectMapper MAPPER = new ObjectMapper();*/

    //提供一个查询商品类目的接口
    /*@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> queryItemCats(@RequestParam(value = "callback", required = false) String callback){
        try {

            //调用service查询所有商品类目
            ItemCatResult itemCatResult = this.itemCatService.queryAllToTree();
            if(null == itemCatResult){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            //拼接
            if(StringUtils.isEmpty(callback)){
                //不是jsonp请求
                return ResponseEntity.ok(MAPPER.writeValueAsString(itemCatResult));
            }else{
                //是jsonp请求,拼接上回调函数名
                return ResponseEntity.ok(callback + "("+MAPPER.writeValueAsString(itemCatResult)+")");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }*/
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ItemCatResult> queryItemCats(@RequestParam(value = "callback", required = false) String callback){
        try {

            //调用service查询所有商品类目
            ItemCatResult itemCatResult = this.itemCatService.queryAllToTree();
            if(null == itemCatResult){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

                return ResponseEntity.ok(itemCatResult);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
