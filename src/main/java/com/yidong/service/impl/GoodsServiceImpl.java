package com.yidong.service.impl;

import com.yidong.mapper.GoodsMapper;
import com.yidong.mapper.ShoppingCarMapper;
import com.yidong.model.Goods;
import com.yidong.model.Price;
import com.yidong.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;//这里会报错，但是并不会影响

    @Autowired
    private ShoppingCarMapper shoppingCarMapper;

    /**
     * 给商品的price中加入购物车的buyNum
     * @param account
     * @param goods
     * @return
     */
    private Goods getBuyNum(String account,Goods goods){
        if(account!=null){
            //拿出商品在购物车里面的购买数量
            String carId = shoppingCarMapper.selectByUserId(account);
            Map<String,String> map = new HashMap<String,String>();
            map.put("carId",carId);
            map.put("goodsId",goods.getId());
            List<Map> priceBuyNums = shoppingCarMapper.getPriceBuyNum(map);
            List<Price> prices = goods.getPrice();
            for(Price price : prices){
                for(Map priceBuyNum : priceBuyNums){
                    if(price.getId().equals((String)priceBuyNum.get("priceId"))){
                        price.setBuyNum((Integer) priceBuyNum.get("num"));
                    }
                }
            }
            goods.setPrice(prices);
        }
        return goods;
    }

    @Override
    public Goods selectByPrimaryKey(String id,String account) {
        Goods goods =  goodsMapper.selectByPrimaryKey(id);
        return  getBuyNum(account,goods);
    }

    @Override
    public List<Goods> selectByName(String name, String cityId,String account) {
        Map<String,String> map = new HashMap();
        map.put("name",name);
        map.put("cityId",cityId);
        List<Goods> goods = goodsMapper.selectByName(map);
        for(Goods good: goods){
            good = getBuyNum(account,good);
        }
        return goods;
    }

    @Override
    public List<Goods> selectByState(int num, int state, String cityId,String account) {
        Map map = new HashMap();
        map.put("num",num);
        map.put("state",state);
        map.put("cityId",cityId);
        List<Goods> goods = goodsMapper.selectByState(map);
        for(Goods good: goods){
            good = getBuyNum(account,good);
        }
        return goods;
    }

    @Override
    public List<Goods> selectByType(int num, int typeId, String cityId,String account) {
        Map map = new HashMap();
        map.put("num",num);
        map.put("typeId",typeId);
        map.put("cityId",cityId);
        List<Goods> goods = goodsMapper.selectByType(map);
        for(Goods good: goods){
            good = getBuyNum(account,good);
        }
        return goods;
    }
}
