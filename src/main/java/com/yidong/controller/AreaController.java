package com.yidong.controller;

import com.yidong.model.Area;
import com.yidong.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AreaController {

    @Autowired
    private AreaService areaService;

    @RequestMapping(value="/getCity")
    public List<Area> getArea(){
        return areaService.selectArea();
    }
}
