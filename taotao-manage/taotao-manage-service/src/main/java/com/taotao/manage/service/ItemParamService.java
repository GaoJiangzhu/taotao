package com.taotao.manage.service;

import com.taotao.manage.pojo.ItemParam;
import org.springframework.stereotype.Service;

@Service
public class ItemParamService extends BaseService<ItemParam> {

    public void saveItemParam(Long itemCatId, String paramData) {
        ItemParam itemParam = new ItemParam();
        itemParam.setItemCatId(itemCatId);
        itemParam.setParamData(paramData);
        super.saveSelective(itemParam);
    }

}
