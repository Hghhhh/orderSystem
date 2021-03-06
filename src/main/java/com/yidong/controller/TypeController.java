package com.yidong.controller;

import com.yidong.model.SmallType;
import com.yidong.service.SmallTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TypeController {
    @Autowired
    private SmallTypeService smallTypeService;


    @RequestMapping(value="/smallType")
    public List<SmallType> getSmallType(@RequestParam String bigTypeId){
        return smallTypeService.selectSmallType(Integer.parseInt(bigTypeId));
    }
}
