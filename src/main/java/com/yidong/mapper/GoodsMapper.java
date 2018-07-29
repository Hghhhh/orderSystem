package com.yidong.mapper;

import com.yidong.model.Goods;

import java.util.List;
import java.util.Map;

public interface GoodsMapper {

    Goods selectByPrimaryKey(String id);

    List<Goods> selectByName(Map map);

    List<Goods> selectByState(Map map);

    List<Goods> selectByType(Map map);

}