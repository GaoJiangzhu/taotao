package com.taotao.commen.bean;

import java.util.ArrayList;
import java.util.List;

public class ItemCatResult {

    private List<ItemCatData> data = new ArrayList<>();

    public List<ItemCatData> getData() {
        return data;
    }

    public void setData(List<ItemCatData> data) {
        this.data = data;
    }
}
