package com.hezhiheng.networkpractice.domain;

import java.util.List;

public class DataList {
    private List<Data> data;

    public DataList() {
    }

    public DataList(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
