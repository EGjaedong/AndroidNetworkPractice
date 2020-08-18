package com.hezhiheng.networkpractice.domain;

import java.util.List;

public class DataList {
    private List<Data> dataList;

    public DataList() {
    }

    public DataList(List<Data> dataList) {
        this.dataList = dataList;
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
    }
}
