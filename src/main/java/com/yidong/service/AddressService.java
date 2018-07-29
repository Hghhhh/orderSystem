package com.yidong.service;

import com.yidong.model.Address;

import java.util.List;

public interface AddressService {
    boolean deleteByPrimaryKey(String id);

    boolean insert(String account, String userName, String userPhone, String address);

    Address selectByPrimaryKey(String id);

    List<Address> selectByUserId(String account);

    boolean updateByPrimaryKey(String id,String account, String userName, String userPhone, String address);

    boolean setDefaultState(String account,String id);

    Address selectDefaultAddress(String account);
}
