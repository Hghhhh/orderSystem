package com.yidong.mapper;

import com.yidong.model.OrderFormGoods;

public interface OrderFormGoodsMapper {
    int deleteByPrimaryKey(String id);

    int insert(OrderFormGoods record);

    int insertSelective(OrderFormGoods record);

    OrderFormGoods selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrderFormGoods record);

    int updateByPrimaryKey(OrderFormGoods record);
}