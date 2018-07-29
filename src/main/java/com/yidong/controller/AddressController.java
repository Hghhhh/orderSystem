package com.yidong.controller;

import com.yidong.model.Address;
import com.yidong.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
@PreAuthorize("hasRole('USER')")
@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @RequestMapping(value="/insertAddress")
    public boolean insertAddress(HttpServletRequest request,@RequestParam String userName,@RequestParam String userPhone,@RequestParam String address){
        return addressService.insert((String)request.getAttribute("account"),userName,userPhone,address);
    }

    @RequestMapping(value="/updateAddress")
    public boolean updateAddress(HttpServletRequest request,@RequestParam String addressId,@RequestParam String userName,@RequestParam String userPhone,@RequestParam String address){
        return addressService.updateByPrimaryKey(addressId,(String)request.getAttribute("account"),userName,userPhone,address);
    }

    @RequestMapping(value="/selectAddress")
    public List<Address>  selectAddressByUserId(HttpServletRequest request){
        return addressService.selectByUserId((String)request.getAttribute("account"));
    }

    @RequestMapping(value="/deleteAddress")
    public boolean  deleteAddress(@RequestParam String addressId){
        return addressService.deleteByPrimaryKey(addressId);
    }

    @RequestMapping(value="/selectAddressByPrimaryKey")
    public Address  selectAddressByPrimaryKey(@RequestParam String addressId){
        return addressService.selectByPrimaryKey(addressId);
    }

    @RequestMapping(value="/setDefaultAddress")
    public boolean setDefaultAddress(HttpServletRequest request,@RequestParam String addressId){
        return addressService.setDefaultState((String)request.getAttribute("account"),addressId);
    }

    @RequestMapping(value="/selectDefaultAddress")
    public Address selectDefaultAddress(HttpServletRequest request){
        return addressService.selectDefaultAddress((String)request.getAttribute("account"));
    }


}
