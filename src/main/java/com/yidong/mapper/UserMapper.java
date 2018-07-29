package com.yidong.mapper;

import com.yidong.model.User;

import java.util.Map;

public interface UserMapper {
    int login(Map<String,String> map);

    User selectByAccount(String account);

    int insert(User user);

    User selectByPrimaryKey(String id);

    int updatePasswordByAccount(Map<String,String> map);

    int insertShoppingCar(Map<String,String> map);

}