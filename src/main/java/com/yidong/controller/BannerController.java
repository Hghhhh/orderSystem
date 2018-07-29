package com.yidong.controller;

import com.yidong.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @RequestMapping(value="/getBannerPicture")
    public List<String> getBannerPicture(String cityId){
        return bannerService.selectByState(cityId);
    }
}
