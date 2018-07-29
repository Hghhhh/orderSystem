package com.yidong.service.impl;

import com.yidong.mapper.BigTypeMapper;
import com.yidong.model.BigType;
import com.yidong.service.BigTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BigTypeServiceImpl implements BigTypeService{

    @Autowired
    private BigTypeMapper bigTypeMapper;

    @Override
    public List<BigType> selectBigType() {
        return bigTypeMapper.selectBigType();
    }
}
