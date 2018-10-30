package com.yidong.service;

import com.yidong.model.ShoppingCar;
import com.yidong.model.ShoppingCarGoodsForUpdate;
import com.yidong.model.ShoppingCarToGoods;

import java.util.List;
import java.util.Map;

public interface ShoppingCarService {

  List<ShoppingCarToGoods> getShoppingCar(String account);

  boolean updateShoppingCar(List<ShoppingCarGoodsForUpdate> shoppingCarGoodsForUpdates,String account);

  void clearShoppingCarGoods(String account);
}
