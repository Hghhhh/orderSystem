package com.yidong.mapper;

import com.yidong.model.Area;

import java.util.List;

public interface AreaMapper {

    List<Area> selectArea();

    String selectCityName(String id);

}