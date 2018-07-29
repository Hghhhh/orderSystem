package com.yidong.controller;

import com.yidong.model.Goods;

import com.yidong.service.GoodsService;
import com.yidong.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value="/selectGoods")
    public List<Goods> selectGoodsByName(@RequestParam String message, String cityId, HttpServletRequest request){
        return goodsService.selectByName(message.trim(),cityId,(String) request.getAttribute("account"));
    }

    @RequestMapping(value="/selectOneGoods")
    public Goods selectByPrimaryKey(@RequestParam String goodsId, HttpServletRequest request){
        return goodsService.selectByPrimaryKey(goodsId,(String) request.getAttribute("account"));
    }

    @RequestMapping(value="/selectStateGoods")
    public List<Goods> selectByState(@RequestParam int state,@RequestParam int num,String cityId, HttpServletRequest request){
        return goodsService.selectByState(num,state,cityId,(String) request.getAttribute("account"));
    }

    @RequestMapping(value="/selectTypeGoods")
    public List<Goods> selectByType(@RequestParam String typeId,@RequestParam int num,String cityId, HttpServletRequest request){
        return goodsService.selectByType(num,typeId,cityId,(String) request.getAttribute("account"));
    }


}
