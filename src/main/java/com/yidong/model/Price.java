package com.yidong.model;

public class Price {

    private String id;

    private Float price;

    private String unit;

    private int num;//库存

    private int buyNum;//购物车中购买数量


    private String weight;

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

   public int getBuyNum() {
        return buyNum;
    }

   public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}