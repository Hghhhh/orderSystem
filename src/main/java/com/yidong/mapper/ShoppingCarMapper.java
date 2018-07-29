package com.yidong.mapper;

import com.yidong.model.ShoppingCar;
import com.yidong.model.ShoppingCarGoods;
import com.yidong.model.ShoppingCarGoodsForUpdate;
import com.yidong.model.ShoppingCarToGoods;

import java.util.Map;
import java.util.List;

public interface ShoppingCarMapper {

     String selectByUserId(String account);

     //ShoppingCarGoods getGoodsList(ShoppingCarToGoods shoppingCarToGoods);

     List<ShoppingCarToGoods> selectShoppingCarToGoods(String carId);

     Map<String,String> selectGoodsInCar(String id);

     int deleteGoodsInOrderForm(ShoppingCarGoodsForUpdate shoppingCarGoodsForUpdate);

     int deleteGoodsInCar(String id);

     int addGoodsToCar(Map map);

     int update(List<ShoppingCarGoodsForUpdate> shoppingCarGoodsForUpdates);

     String selectWhenAddGoodsToCar(Map map);

     List<Map> getPriceBuyNum(Map map);

      int clearShoppingCarGoods(String carId);


}