package com.taotao.manage.service;

import com.taotao.manage.mapper.ItemDescMapper;
import com.taotao.manage.pojo.ItemDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemDescService extends BaseService<ItemDesc> {

    @Autowired
    private ItemDescMapper itemDescMapper;
}
