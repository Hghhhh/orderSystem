package com.yidong.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yidong.model.ShoppingCarGoodsForUpdate;
import com.yidong.model.ShoppingCarToGoods;
import com.yidong.service.ShoppingCarService;

import com.yidong.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@PreAuthorize("hasRole('USER')")
@RestController
public class ShoppingCarController {
    @Autowired
    private ShoppingCarService shoppingCarService;

    @RequestMapping(value="/shoppingCar")
    public List<ShoppingCarToGoods> getShoppingCar(HttpServletRequest request){
        return shoppingCarService.getShoppingCar((String)request.getAttribute("account"));
    }

    @RequestMapping(value="/updateShoppingCar")
    public ResponseEntity<Boolean> updateShoppingCar(@RequestBody String shoppingCarGoodsForUpdates, HttpServletRequest request) {
        List<ShoppingCarGoodsForUpdate> scgfus = null;
        if(shoppingCarGoodsForUpdates!=null ){
            //解决购物车为空的情况
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(shoppingCarGoodsForUpdates);
            String s = m.replaceAll("");
            if(s.equals("[]")){

            }
            else{
                Gson gson = new Gson();
                scgfus  = gson.fromJson(shoppingCarGoodsForUpdates, new TypeToken<List<ShoppingCarGoodsForUpdate>>() {}.getType());
            }
        }
     //   List<ShoppingCarGoodsForUpdate> scgfus  = JsonUtil.parseJsonArrayWithGson(shoppingCarGoodsForUpdates, ShoppingCarGoodsForUpdate.class);
        //这里用泛型传会出错java.lang.ClassCastException: com.google.gson.internal.LinkedTreeMap cannot be cast to xxx，原因未知？
        //解析json。拿到list数组
        String account = (String) request.getAttribute("account");
        return shoppingCarService.updateShoppingCar(scgfus,account)==true?
              new ResponseEntity<Boolean>(true, HttpStatus.OK):
               new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
