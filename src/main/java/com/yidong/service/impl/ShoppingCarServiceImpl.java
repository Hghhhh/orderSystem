package com.yidong.service.impl;

import com.yidong.mapper.ShoppingCarMapper;
import com.yidong.model.ShoppingCarGoodsForUpdate;
import com.yidong.model.ShoppingCarToGoods;
import com.yidong.service.ShoppingCarService;
import com.yidong.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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


    @Override
    public boolean  updateShoppingCar(List<ShoppingCarGoodsForUpdate> shoppingCarGoodsForUpdates,String account){
        //用商品的goodsId+priceId来标识商品列表中的商品的index
        Map<String,Integer> goodsMap = new HashMap<String,Integer>();
       //列表中商品的索引
        int index = 0;
        String carId = shoppingCarMapper.selectByUserId(account);
        if(shoppingCarGoodsForUpdates==null){
            //如果购物车为空
            return true;
        }
        else{
            for(ShoppingCarGoodsForUpdate shoppingCarGoodsForUpdate : shoppingCarGoodsForUpdates){
                //拿到此商品的goodsId+priceId
                String message = shoppingCarGoodsForUpdate.getGoodsId()+shoppingCarGoodsForUpdate.getPriceId();
                Integer goodsIndex = goodsMap.get(message);
                //判断goodsMap中是否有此商品
                if(goodsIndex != null){
                    //如果有则这两件商品是一样的商品，合并商品的buyNum
                    shoppingCarGoodsForUpdates.get(goodsIndex).setBuyNum(shoppingCarGoodsForUpdates.get(goodsIndex)
                            .getBuyNum()+shoppingCarGoodsForUpdate.getBuyNum());
                }
                else{
                    //如果goodsMap中没有此商品，则添加到goodsMap中
                    goodsMap.put(shoppingCarGoodsForUpdate.getGoodsId()+shoppingCarGoodsForUpdate.getPriceId(),index);
                    shoppingCarGoodsForUpdate.setId(UUIDUtils.getUUID());
                    shoppingCarGoodsForUpdate.setCarId(carId);
                }
                index++;
            }
            //收集合并后的商品列表
            List<ShoppingCarGoodsForUpdate> shoppingCarGoodsForUpdatesAfterBuyNumMerged = new ArrayList<ShoppingCarGoodsForUpdate>();
            Collection<Integer> indexs = goodsMap.values();
            for(Integer i : indexs){
                shoppingCarGoodsForUpdatesAfterBuyNumMerged.add(shoppingCarGoodsForUpdates.get(i));
            }
            return shoppingCarMapper.update(shoppingCarGoodsForUpdatesAfterBuyNumMerged)==0?false:true;
        }
    }

    @Override
    public void clearShoppingCarGoods(String account){
        String carId = shoppingCarMapper.selectByUserId(account);
        shoppingCarMapper.clearShoppingCarGoods(carId);
    }

}
