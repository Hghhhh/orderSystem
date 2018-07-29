package com.yidong.util;

import java.net.ServerSocket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderFormId {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    //年月日小时分钟秒毫秒

    /**
     * 生成订单号
     *
     * @param account 订单号前缀
     * @return
     */
    public static String gens(String account) {
        String orderNo = sdf.format(new Date()) + (1 + (int) (Math.random() * 10000)) + account;
        return orderNo;
    }


    /**
     * 根据订单id中的时间判断订单是否过期失效
     * @param orderFormId
     * @return
     * @throws ParseException
     */
    public static Boolean isExprexpired(String orderFormId) {
        long formTime = 0;
        try {
            formTime = sdf.parse(orderFormId.substring(0, 17)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timePass = (System.currentTimeMillis() - formTime) / (1000 * 60);
        //获得订单生成时间到目前经过了多少分钟
        if (timePass > 15) return true;
        else return false;
    }
}