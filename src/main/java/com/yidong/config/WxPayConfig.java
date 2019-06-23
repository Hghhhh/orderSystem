package com.yidong.config;

public class WxPayConfig {
    //小程序appid
    public static final String appid = "wx86afcb7df200ad91";
    //微信小程序密钥
    public static final String secret = "a71fb1f0643b7399768b335a6882a96a";
    //微信支付的商户id
    public static final String mch_id = "1510906771";
    //微信支付的商户密钥
    public static final String key = "4878413d676a4bac9f6e45811081618e";

    //支付成功后的服务器回调url
    public static final String notify_url = "https://www.dongpinhui.xyz:8443/wxNotify";
    //签名方式
    public static final String SIGNTYPE = "MD5";
    //交易类型
    public static final String TRADETYPE = "JSAPI";
    //微信统一下单接口地址
    public static final String pay_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //微信统一下单接口的参数
    public static final String grant_type = "authorization_code";


}
