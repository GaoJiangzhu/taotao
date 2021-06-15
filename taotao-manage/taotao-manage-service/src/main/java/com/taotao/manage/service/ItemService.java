package com.taotao.manage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.commen.service.ApiService;
import com.taotao.manage.mapper.ItemMapper;
import com.taotao.manage.pojo.EasyUIResult;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemService extends BaseService<Item>{

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemDescService itemDescService;

    @Autowired
    private ItemParamItemService itemParamItemService;

    @Autowired
    private ApiService apiService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public void saveItemAndItemDesc(Item item, String desc, String itemParams) {

        item.setId(null);//强制让主键自增
        item.setStatus(1);//正常状态
        super.save(item);

        //添加商品的描述
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(item.getId());
        this.itemDescService.save(itemDesc);
        //添加商品模板对应的数据
        ItemParamItem record = new ItemParamItem();
        record.setItemId(item.getId());
        record.setParamData(itemParams);
        this.itemParamItemService.saveSelective(record);

        //发送消息给搜索系统
        sendMsg(item.getId(), "insert");
    }

    /**
     * 商品分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    public EasyUIResult queryItemListByPage(Integer pageNum, Integer pageSize) {

        //调用baseService分页查询方法
        //PageInfo<Item> itemPageInfo = super.queryPageListByWhere(pageNum, pageSize, null);
        PageHelper.startPage(pageNum, pageSize);
        List<Item> items = itemMapper.queryItemListAndCname();

        PageInfo<Item> pageInfo = new PageInfo<>(items);
        //封装easy...

        return new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
    }

    //修改商品信息
    public void updateItemAndDesc(Item item, String desc, String itemParams) {
        //修改商品的基本信息
        item.setCreated(null);
        item.setStatus(null);//始终不修改
        super.updateSelective(item);


        //修改商品描述
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(item.getId());
        itemDesc.setCreated(null);
        itemDescService.updateSelective(itemDesc);//字段有值才会修改
        //修改规格参数
        ItemParamItem record = new ItemParamItem();
        record.setItemId(item.getId());
        record.setParamData(itemParams);
        this.itemParamItemService.updateSelective(record);

        /*//调用前台系统提供的接口通知前台系统商品被修改
        try {
            String url = "http://www.taotao.com/item/cache/" + item.getId() + ".html";
            this.apiService.doGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //通过rabbitMQ发送消息到前台系统
        //发送什么消息取决于前台需要什么
        //发送的内容包含商品id,路由key,当前时间
        //由于需要通知多个系统,所以写一个公共方法
        sendMsg(item.getId(), "update");
    }

    /**
     * 发送消息
     * @param itemId 商品Id
     * @param type 路由key
     */
    private void sendMsg(Long itemId, String type){

        try {
            Map<String, Object> content = new HashMap<>();
            content.put("itemId", itemId);
            content.put("type", type);
            content.put("date", System.currentTimeMillis());

            //调用rabbitTemplate模板发送
            rabbitTemplate.convertAndSend("item." + type, MAPPER.writeValueAsString(content));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }




    //删除商品相关的所有信息
//    public void deleteItem(Long[] ids, Class<Item> itemClass) {
//        //删除基本信息
//
//        super.deleteByIds(itemClass, "id", ids);
//
//        //删除描述
//        this.itemDescService.deleteByIds();
//
//        //删除规格参数
//        this.itemParamItemService.deleteByIds();
//    }
}
