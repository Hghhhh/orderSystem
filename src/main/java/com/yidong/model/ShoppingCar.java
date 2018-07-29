package com.yidong.model;

import java.util.List;

public class ShoppingCar {

    private List<ShoppingCarGoods> goodsList;

    public List<ShoppingCarGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<ShoppingCarGoods> goodsList) {
        this.goodsList = goodsList;
    }
}