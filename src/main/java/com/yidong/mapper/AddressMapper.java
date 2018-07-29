package com.yidong.mapper;

import com.yidong.model.Address;

import java.util.List;

public interface AddressMapper {

    String getUserId(String account);

    int deleteByPrimaryKey(String id);

    int insert(Address record);

    int insertSelective(Address record);

    Address selectByPrimaryKey(String id);

    List<Address> selectByUserId(String account);

    int updateByPrimaryKeySelective(Address record);

    int updateByPrimaryKey(Address record);

    int updateState(String account);

    int setState(String id);

    String selectOneByUserId(String userId);

    String getAccount(String userId);

    Address selectDefaultAddress(String account);
}