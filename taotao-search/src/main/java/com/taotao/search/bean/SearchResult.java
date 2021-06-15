package com.taotao.search.bean;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {

    private Long total;

    private List<?> data = new ArrayList<>();

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public SearchResult() {
    }

    public SearchResult(Long total, List<?> data) {
        this.total = total;
        this.data = data;
    }
}
