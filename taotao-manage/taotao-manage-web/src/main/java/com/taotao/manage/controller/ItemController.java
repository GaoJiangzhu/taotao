package com.taotao.manage.controller;

import com.taotao.manage.pojo.EasyUIResult;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.service.ItemService;
import org.apache.log4j.Logger;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("item")
public class ItemController {

    //定义一个日志记录器
    private static final Logger LOGGER = Logger.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;


    //添加商品
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> addItem(Item item, @RequestParam("desc") String desc,
                                        @RequestParam("itemParams") String itemParams){

        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("添加商品入口参数" + item + ",描述信息" + desc);
        }

        try {
            //添加商品的基本数据和商品描述
            this.itemService.saveItemAndItemDesc(item, desc, itemParams);

            //记录添加商品成功
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("添加成功:itemId:" + item.getId());
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            //e.printStackTrace();
            LOGGER.error("添加商品异常" + e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }


    /**
     * 分页查询商品列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemListByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "rows", defaultValue = "30") Integer pageSize){

        try {
            EasyUIResult easyUIResult = this.itemService.queryItemListByPage(pageNum, pageSize);
            if(easyUIResult == null || easyUIResult.getRows().isEmpty()){

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            //e.printStackTrace();
            LOGGER.error("失败" + e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }


    /**
     * 修改商品信息
     * @param item
     * @param desc
     * @param itemParams
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItem(Item item, @RequestParam("desc") String desc,
                                           @RequestParam("itemParams") String itemParams){

        try {
            this.itemService.updateItemAndDesc(item, desc, itemParams);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 删除商品
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteItem(@RequestParam("ids") Long[] ids){

            for(Long a:ids){
                System.out.println(a);
            }
//        try {
//            this.itemService.deleteItem(ids);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
