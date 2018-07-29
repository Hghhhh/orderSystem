package com.yidong.mapper;

import com.yidong.model.Banner;

import java.util.List;

public interface BannerMapper {

    List<String> selectByState(String cityId);

}