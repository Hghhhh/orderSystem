package com.yidong.service.impl;

import com.yidong.mapper.SmallTypeMapper;
import com.yidong.model.SmallType;
import com.yidong.service.SmallTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SmallTypeServiceImpl implements SmallTypeService {

    @Autowired
    private SmallTypeMapper smallTypeMapper;
    @Override
    public List<SmallType> selectSmallType(String bigTypeId) {
        return smallTypeMapper.selectSmallType(bigTypeId);
    }
}
