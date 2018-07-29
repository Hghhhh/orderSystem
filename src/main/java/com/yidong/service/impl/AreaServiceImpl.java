package com.yidong.service.impl;

import com.yidong.mapper.AreaMapper;
import com.yidong.model.Area;
import com.yidong.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaMapper areaMapper;

    @Override
    public List<Area> selectArea() {
        return areaMapper.selectArea();
    }
}
