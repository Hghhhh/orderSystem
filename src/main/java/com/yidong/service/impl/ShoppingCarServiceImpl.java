package com.yidong.service.impl;

import com.yidong.mapper.ShoppingCarMapper;
import com.yidong.model.ShoppingCarGoodsForUpdate;
import com.yidong.model.ShoppingCarToGoods;
import com.yidong.service.ShoppingCarService;
import com.yidong.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class ShoppingCarServiceImpl implements ShoppingCarService{
    @Autowired
    private ShoppingCarMapper shoppingCarMapper;

    @Override
    public List<ShoppingCarToGoods> getShoppingCar(String account) {
        String carId = shoppingCarMapper.selectByUserId(account);
        List<ShoppingCarToGoods> shoppingCarToGoods = shoppingCarMapper.selectShoppingCarToGoods(carId);
        for(ShoppingCarToGoods s: shoppingCarToGoods){
            s.getPrice().setBuyNum(s.getBuyNum());
        }
        /**List<ShoppingCarGoods> shoppingCarGoods = new ArrayList<ShoppingCarGoods>();
        for(ShoppingCarToGoods s : shoppingCarToGoods){
            ShoppingCarGoods ss = shoppingCarMapper.getGoodsList(s);
            ss.setBuyNum(s.getBuyNum());
            ss.setGoodsListId(s.getId());
            shoppingCarGoods.add(ss);
        }*/
        //ShoppingCar shoppingCar = new ShoppingCar();
       // shoppingCar.setGoodsList(shoppingCarGoods);
        return shoppingCarToGoods;
    }


    @Transactional
    @Override
    public boolean  updateShoppingCar(List<ShoppingCarGoodsForUpdate> shoppingCarGoodsForUpdates,String account){
        String carId = shoppingCarMapper.selectByUserId(account);
        shoppingCarMapper.clearShoppingCarGoods(carId);
        if(shoppingCarGoodsForUpdates==null){
            //如果购物车为空
            return true;
        }
        else{
            for(ShoppingCarGoodsForUpdate shoppingCarGoodsForUpdate : shoppingCarGoodsForUpdates){
                shoppingCarGoodsForUpdate.setId(UUIDUtils.getUUID());
                shoppingCarGoodsForUpdate.setCarId(carId);
            }
            return shoppingCarMapper.update(shoppingCarGoodsForUpdates)==0?false:true;
        }
    }

}
