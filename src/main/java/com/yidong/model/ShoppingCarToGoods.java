package com.yidong.model;

public class ShoppingCarToGoods {
    private String id;
    private ShoppingCarGoods goods;
    private Price price;
    private Integer buyNum;
    private int isCheck;

    public Integer getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }


    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public ShoppingCarGoods getGoods() {
        return goods;
    }

    public void setGoods(ShoppingCarGoods goods) {
        this.goods = goods;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }
}
