package com.yidong.model;

import java.util.Date;
import java.util.List;

public class OrderForm {
    private String id;

    private String note;

    private Float sum;

    private Integer state;

    private String createTime;

    private String userId;

    private Address address;

    private List<ShoppingCarToGoods> goodsList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Float getSum() {
        return sum;
    }

    public void setSum(Float sum) {
        this.sum = sum;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<ShoppingCarToGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<ShoppingCarToGoods> goodsList) {
        this.goodsList = goodsList;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}