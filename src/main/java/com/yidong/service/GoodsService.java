package com.yidong.service;

import com.yidong.model.Goods;

import java.util.List;
import java.util.Map;

public interface GoodsService {

    Goods selectByPrimaryKey(String id,String account);

    List<Goods> selectByName(String name,String cityId,String account);

    List<Goods> selectByState(int num, int state,String cityId,String account);

    List<Goods> selectByType(int num, String typeId,String cityId,String account);

}
