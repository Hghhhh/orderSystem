package com.yidong.service.impl;

import com.yidong.mapper.*;
import com.yidong.model.*;
import com.yidong.service.OrderFormService;
import com.yidong.util.OrderFormId;
import com.yidong.util.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@Slf4j
public class OrderFormServiceImpl implements OrderFormService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private OrderFormMapper orderFormMapper;
    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private AreaMapper areaMapper;

    @Autowired
    private ShoppingCarMapper shoppingCarMapper;

    @Override
    public OrderForm selectByPrimaryKey(String id) {
        OrderForm orderForm =  orderFormMapper.selectByPrimaryKey(id);
        List<ShoppingCarToGoods> shoppingCarToGoods = orderForm.getGoodsList();
        for(int i=0;i<shoppingCarToGoods.size();i++){
            shoppingCarToGoods.get(i).getPrice().setBuyNum(shoppingCarToGoods.get(i).getBuyNum());
        }
        return orderForm;
    }

    @Override
    public List<OrderForm> selectByUerId(String account) {
        List<OrderForm> orderForms = orderFormMapper.selectByUerId(account);
        List<ShoppingCarToGoods> shoppingCarToGoods = null;
        for(int i=0;i<orderForms.size();i++){
          shoppingCarToGoods = orderForms.get(i).getGoodsList();
           for(int j=0;j<shoppingCarToGoods.size(); j++){
               Price price = shoppingCarToGoods.get(j).getPrice();
               Price newPrice = new Price();
               //这里必须new一个新的price对象，因为相同的priceId拿出来的price对象是同一个，
               // 也就是说假如订单A和订单B的包含同一件商品、同一个priceId，那么拿出来的price对象是同一个
               //这时候先修改订单A中price的buyNum，订单B后面也修改了相同price的buyNum，最后订单A中显示的是订单B的buyNum
               newPrice.setId(price.getId());
               newPrice.setNum(price.getNum());
               newPrice.setPrice(price.getPrice());
               newPrice.setUnit(price.getUnit());
               newPrice.setWeight(price.getWeight());
               newPrice.setBuyNum(shoppingCarToGoods.get(j).getBuyNum());
               shoppingCarToGoods.get(j).setPrice(newPrice);
           }
       }
        return orderForms;

    }

    @Override
    public String insert(String addressId, String note,
        Float sum,String account,String cityId,List<ShoppingCarGoodsForUpdate> shoppingCarGoodsForUpdates) {
        for(ShoppingCarGoodsForUpdate s : shoppingCarGoodsForUpdates){
            Goods goods = goodsMapper.selectByPrimaryKey(s.getGoodsId());
            if(goods.getState()==0){
                return "goods is out";
                //订单中有商品已经下架
            }
        }
        Address address = addressMapper.selectByPrimaryKey(addressId);
        ///判断城市和订单收获地址是否匹配
        if(!address.getAddress().contains(areaMapper.selectCityName(cityId))){
            return null;
        }
        //判断订单里面有无商品
        if(shoppingCarGoodsForUpdates==null||shoppingCarGoodsForUpdates.size()==0){
            return "no goods";
        }

        String id = OrderFormId.gens(account);//生成订单id
        Map map = new HashMap();
        map.put("state",1);
        map.put("addressId",addressId);
        map.put("name",addressMapper.selectByPrimaryKey(addressId).getUserName());
        map.put("note",note);
        map.put("sum",sum);
        String userId = orderFormMapper.getUserId(account);
        map.put("userId",userId);
        map.put("cityId",cityId);
        map.put("id",id);
        for(ShoppingCarGoodsForUpdate s : shoppingCarGoodsForUpdates){
            s.setCarId(shoppingCarMapper.selectByUserId(account));
            //判断库存等操作
            Price price = orderFormMapper.getPrice(s.getPriceId());
            int num = price.getNum()-s.getBuyNum();//库存减去购买数量
            if(num<0){
                //库存不够
               return "not enough goods";
            }
            else{
                Map updateMap = new HashMap();
                updateMap.put("num",num);
                updateMap.put("id",s.getPriceId());
                orderFormMapper.updateGoodsNum(updateMap);
                //减掉订单的库存
                shoppingCarMapper.deleteGoodsInOrderForm(s);
                //减掉购物车中的商品
            }
           s.setId(UUIDUtils.getUUID());
           s.setCarId(id);
        }

        orderFormMapper.insert(map);
        orderFormMapper.insertOrderFormGoods(shoppingCarGoodsForUpdates);
        log.info("account:"+account+" 创建了新订单:"+map.get("id"));
        return id;
    }

    @Override
    public boolean update(String id,int state) {
        if(state==5){
            //订单失效
            OrderForm orderForm = orderFormMapper.selectByPrimaryKey(id);
            List<ShoppingCarToGoods> shoppingCarGoodsList = orderForm.getGoodsList();
            for(ShoppingCarToGoods s : shoppingCarGoodsList){
                String priceId = s.getPrice().getId();
                Price price = orderFormMapper.getPrice(priceId);
                int num = s.getBuyNum()+price.getNum();
                Map updateMap = new HashMap();
                updateMap.put("num",num);
                updateMap.put("id",priceId);
                orderFormMapper.updateGoodsNum(updateMap);
                //加上失效订单的货物
            }
        }
        Map map = new HashMap();
        map.put("state",state);
        map.put("id",id);
        switch (state){
            case 5 :
                log.info("订单被取消:"+id);break;
            case 6 :
                log.info("订单需要退款："+id);break;
            case 7 :
                log.info("订单完结："+id);break;

        }
        return orderFormMapper.update(map)==0?false:true;
    }
}
