package com.yidong.config;

public class WxPayConfig {
    //小程序appid
    public static final String appid = "";
    //微信小程序密钥
    public static final String secret = "12312332";
    //微信支付的商户id
    public static final String mch_id = "";
    //微信支付的商户密钥
    public static final String key = "";

    //支付成功后的服务器回调url
    public static final String notify_url = "https://guohanghuang.cn:8443/wxNotify";
    //签名方式
    public static final String SIGNTYPE = "MD5";
    //交易类型
    public static final String TRADETYPE = "JSAPI";
    //微信统一下单接口地址
    public static final String pay_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //微信统一下单接口的参数
    public static final String grant_type = "authorization_code";


}
