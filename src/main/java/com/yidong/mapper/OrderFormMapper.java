package com.yidong.mapper;

import com.yidong.model.OrderForm;
import com.yidong.model.Price;
import com.yidong.model.ShoppingCarGoodsForUpdate;

import java.util.Map;
import java.util.List;

public interface OrderFormMapper {

    OrderForm selectByPrimaryKey(String id);
    List<OrderForm> selectByUerId(String id);
    Map selectShoppingCarGoods(String shoppingCarGoodsId);
    int insert(Map map);
    int insertOrderFormGoods(Map map);
    int update(Map map);
    int deleteShoppingCarGoods(String id);
    int updateGoodsNum(Map map);
    Price getPrice(String priceId);
    String getUserId(String account);
    int insertOrderFormGoods(List<ShoppingCarGoodsForUpdate> shoppingCarGoodsForUpdates);
}