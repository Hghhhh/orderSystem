package com.yidong.controller;

import com.yidong.model.OrderForm;
import com.yidong.model.OrderFormForAdd;
import com.yidong.model.ShoppingCarGoodsForUpdate;
import com.yidong.service.OrderFormService;
import com.yidong.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@PreAuthorize("hasRole('USER')")
@RestController
public class OrderFormController {

    @Autowired
    private OrderFormService orderFormService;

    @RequestMapping(value="/selectOrderFormById")
    public OrderForm selectByPrimaryKey(@RequestParam String orderFormId) {
        return orderFormService.selectByPrimaryKey(orderFormId);
    }

    @RequestMapping(value="/selectOrderForm")
    public List<OrderForm> selectByUerId(HttpServletRequest request) {
        return orderFormService.selectByUerId((String)request.getAttribute("account"));
    }

    @RequestMapping(value="/createOrderForm")
    public String insert(@RequestBody String orderFormForAdd,HttpServletRequest request) {
        OrderFormForAdd offa = JsonUtil.parseJsonWithGson(orderFormForAdd,OrderFormForAdd.class);
        return orderFormService.insert(offa.getAddressId(),offa.getNote(),offa.getSum(),(String)request.getAttribute("account"),
                offa.getCityId(),offa.getShoppingCarGoodsForUpdates());
    }

    @RequestMapping(value="/updateOrderForm")
    public boolean update(@RequestParam String orderFormId,@RequestParam int state){
        return orderFormService.update(orderFormId,state);
    }


}
