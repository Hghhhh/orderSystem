package com.yidong.model;

import java.util.List;

public class OrderFormForAdd {

    private String addressId;
    private String note;
    private Float sum;
    private String cityId;
    private List<ShoppingCarGoodsForUpdate> shoppingCarGoodsForUpdates;

    public List<ShoppingCarGoodsForUpdate> getShoppingCarGoodsForUpdates() {
        return shoppingCarGoodsForUpdates;
    }

    public void setShoppingCarGoodsForUpdates(List<ShoppingCarGoodsForUpdate> shoppingCarGoodsForUpdates) {
        this.shoppingCarGoodsForUpdates = shoppingCarGoodsForUpdates;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public Float getSum() {
        return sum;
    }

    public void setSum(Float sum) {
        this.sum = sum;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
}
