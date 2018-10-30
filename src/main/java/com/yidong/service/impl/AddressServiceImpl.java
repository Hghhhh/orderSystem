package com.yidong.service.impl;

import com.yidong.mapper.AddressMapper;
import com.yidong.model.Address;
import com.yidong.service.AddressService;
import com.yidong.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;

    @Override
    public boolean deleteByPrimaryKey(String id) {
        Address address = addressMapper.selectByPrimaryKey(id);
        boolean b = true;
        String userId = address.getUserId();
        boolean a = addressMapper.deleteByPrimaryKey(id)==0?false:true;
        if(address.getState()==0){
            //如果删除了默认地址
            String defalutAddressId = addressMapper.selectOneByUserId(userId);
            if(defalutAddressId!=null){
                //如果还有其他地址，设为默认地址
                String account = addressMapper.getAccount(userId);
                b = setDefaultState(account, defalutAddressId);
            }
        }
        return a&&b;
    }

    /**
     * 通过参数生成Address
     * @param account
     * @param userName
     * @param userPhone
     * @param address
     * @return
     */
    public Address getRecord(String account, String userName, String userPhone, String address){
        String userId = addressMapper.getUserId(account);
        Address record = new Address();
        record.setId(UUIDUtils.getUUID());
        record.setUserName(userName);
        record.setUserPhone(userPhone);
        record.setAddress(address);
        record.setUserId(userId);
        return record;
    }

    @Override
    public boolean insert(String account, String userName, String userPhone, String address) {
        Address record = getRecord(account,userName,userPhone,address);
        String id= addressMapper.selectOneByUserId((record.getUserId()));
        if(id==null){
            record.setState(0);
        }
        else {record.setState(1);}
        record.setId(UUIDUtils.getUUID());
        return addressMapper.insert(record)==0?false:true;
    }

    @Override
    public Address selectByPrimaryKey(String id) {
        return addressMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Address> selectByUserId(String account) {
        return addressMapper.selectByUserId(account);
    }


    @Override
    public boolean updateByPrimaryKey(String addressId,String account, String userName, String userPhone, String address) {
        Address record = getRecord(account,userName,userPhone,address);
        record.setId(addressId);
        return addressMapper.updateByPrimaryKeySelective(record)==1?true:false;
    }

    @Override
    public boolean setDefaultState(String account, String addressId) {
        addressMapper.updateState(account);//把所有地址的state设为1
        return addressMapper.setState(addressId)==0?false:true;
    }

    @Override
    public Address selectDefaultAddress(String account) {
        return addressMapper.selectDefaultAddress(account);
    }

}
