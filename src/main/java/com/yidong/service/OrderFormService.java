package com.yidong.service;

import com.yidong.model.OrderForm;
import com.yidong.model.ShoppingCarGoodsForUpdate;

import java.util.List;
import java.util.Map;

public interface OrderFormService {
    OrderForm selectByPrimaryKey(String id);
    List<OrderForm> selectByUerId(String id);
    String insert(String addressId, String note,
                  Float sum,String userId,String cityId,List<ShoppingCarGoodsForUpdate> shoppingCarGoodsForUpdates);
    boolean update(String id,int state);
}
