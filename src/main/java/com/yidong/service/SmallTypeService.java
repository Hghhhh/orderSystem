package com.yidong.service;

import com.yidong.model.SmallType;

import java.util.List;

public interface SmallTypeService {
    List<SmallType> selectSmallType(int bigTypeId);
}
