package com.taotao.manage.mapper;

import com.github.abel533.mapper.Mapper;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemCat;

import java.util.List;

public interface ItemMapper extends Mapper<Item> {

    List<Item> queryItemListAndCname();
}
