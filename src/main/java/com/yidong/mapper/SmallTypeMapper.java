package com.yidong.mapper;

import com.yidong.model.SmallType;

import java.util.List;

public interface SmallTypeMapper {

    List<SmallType> selectSmallType(String bigTypeId);

}