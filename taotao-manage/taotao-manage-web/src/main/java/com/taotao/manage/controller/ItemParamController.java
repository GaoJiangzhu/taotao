package com.taotao.manage.controller;

import com.github.pagehelper.PageInfo;
import com.taotao.manage.pojo.EasyUIResult;
import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.pojo.ItemParam;
import com.taotao.manage.service.ItemCatService;
import com.taotao.manage.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("item/param")
public class ItemParamController {

    @Autowired
    private ItemParamService itemParamService;

    //根据商品类目id查询对应模板
    @RequestMapping(value = "{itemCatId}", method = RequestMethod.GET)
    public ResponseEntity<ItemParam> queryItemById(@PathVariable("itemCatId") Long itemCatId){

        try {
            ItemParam itemParam = new ItemParam();
            itemParam.setItemCatId(itemCatId);
            ItemParam param = this.itemParamService.queryOne(itemParam);
            if(null == param){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            return ResponseEntity.ok(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    //添加商品类目模板
    @RequestMapping(value = "{itemCatId}",method = RequestMethod.POST)
    public ResponseEntity<Void> addItemParam(@PathVariable("itemCatId") Long itemCatId,
                                             @RequestParam("paramData") String paramData){

        try {
            this.itemParamService.saveItemParam(itemCatId, paramData);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    //分页查询模板数据
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemParamList(@RequestParam(value = "page",defaultValue = "1") Integer pageNum,
                                                           @RequestParam(value = "rows",defaultValue = "30") Integer pageSize){

        try {
            PageInfo<ItemParam> pageInfo = this.itemParamService.queryPageListByWhere(pageNum, pageSize, null);
            EasyUIResult easyUIResult = new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
            if(pageInfo.getList() == null || pageInfo.getList().isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
