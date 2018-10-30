package com.yidong.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.alibaba.fastjson.JSONObject;
import com.yidong.config.WxPayConfig;
import com.yidong.model.Json;
import com.yidong.model.OAuthJsToken;
import com.yidong.model.OrderForm;
import com.yidong.service.OrderFormService;
import com.yidong.util.IpUtils;
import com.yidong.util.OrderFormId;
import com.yidong.util.PayUtil;
import com.yidong.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.weixin4j.WeixinException;
import org.weixin4j.WeixinSupport;
import org.weixin4j.http.HttpsClient;
import org.weixin4j.http.Response;

import javax.net.ssl.SSLContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class WeChatController extends WeixinSupport {


    @Autowired
    private OrderFormService orderFormService;

    /**
     * 小程序后台登录，向微信平台发送获取access_token请求，并返回openId
     * @param code
     * @return openid
     * @throws WeixinException
     * @throws IOException
     * @since Weixin4J 0.1.0
     */
    @RequestMapping("/wxLogin")
    public Map<String, Object> login(@RequestParam String code) throws WeixinException, IOException {
        if (code == null || code.equals("")) {
            throw new WeixinException("invalid null, code is null.");
        }

        Map<String, Object> ret = new HashMap<String, Object>();
        //拼接参数
        String param = "?grant_type=" + WxPayConfig.grant_type + "&appid=" + WxPayConfig.appid + "&secret=" + WxPayConfig.secret + "&js_code=" + code;
        //创建请求对象
        HttpsClient http = new HttpsClient();
        //调用获取access_token接口
        Response res = http.get("https://api.weixin.qq.com/sns/jscode2session" + param);
        //根据请求结果判定，是否验证成功
        JSONObject jsonObj =  res.asJSONObject();
        if (jsonObj != null) {
            Object errcode = jsonObj.get("errcode");
            if (errcode != null) {
                //返回异常信息
                throw new WeixinException(getCause(Integer.parseInt(errcode.toString()))+jsonObj.get("errmsg"));
            }
            ObjectMapper mapper = new ObjectMapper();
            OAuthJsToken oauthJsToken = mapper.readValue(jsonObj.toJSONString(),OAuthJsToken.class);
            log.info("openid=" + oauthJsToken.getOpenid());
            ret.put("openid", oauthJsToken.getOpenid());
        }
        return ret;
    }

    /**
     * @Description: 发起微信支付
     * @param openid
     * @param request
     */
    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/wxPay")
    public ResponseEntity<Json> wxPay(@RequestParam String openid, @RequestParam String orderFormId, HttpServletRequest request){
        Json json = new Json();
        //判断订单是否过期
        if(OrderFormId.isExprexpired(orderFormId)){
            orderFormService.update(orderFormId,5);
            log.error("该订单已经过期失效！"+orderFormId);
            json.setSuccess(false);
            json.setMsg("该订单已经过期失效!");
            return ResponseEntity.badRequest().body(json);
        }
        OrderForm orderForm = orderFormService.selectByPrimaryKey(orderFormId);//拿到支付订单
        //判断是否订单为未支付
        if(orderForm.getState()!=1){
            json.setSuccess(false);
            json.setMsg("该订单已支付或失效!");
            return ResponseEntity.badRequest().body(json);
        }

        try{
            //生成的随机字符串
            String nonce_str = StringUtils.getRandomStringByLength(32);
            //商品名称
            String body = "冻品会商品";
            //获取本机的ip地址
            String spbill_create_ip = IpUtils.getIpAddr(request);
            String money = Math.round((orderForm.getSum()*100))+"";//支付金额，单位：分，这边需要转成字符串类型，否则后面的签名会失败
            Map<String, String> packageParams = new HashMap<String, String>();
            packageParams.put("appid", WxPayConfig.appid);
            packageParams.put("mch_id", WxPayConfig.mch_id);
            packageParams.put("nonce_str", nonce_str);
            packageParams.put("body", body);
            packageParams.put("out_trade_no", orderFormId);//商户订单号
            packageParams.put("total_fee", money);//支付金额，这边需要转成字符串类型，否则后面的签名会失败
            packageParams.put("spbill_create_ip", spbill_create_ip);
            packageParams.put("notify_url", WxPayConfig.notify_url);
            packageParams.put("trade_type", WxPayConfig.TRADETYPE);
            packageParams.put("openid", openid);
            // 除去数组中的空值和签名参数
            packageParams = PayUtil.paraFilter(packageParams);
            String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
            String mysign = PayUtil.sign(prestr, WxPayConfig.key, "utf-8").toUpperCase();

            //拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
            String xml = "<xml>" + "<appid>" + WxPayConfig.appid + "</appid>"
                    + "<body><![CDATA[" + body + "]]></body>"
                    + "<mch_id>" + WxPayConfig.mch_id + "</mch_id>"
                    + "<nonce_str>" + nonce_str + "</nonce_str>"
                    + "<notify_url>" + WxPayConfig.notify_url + "</notify_url>"
                    + "<openid>" + openid + "</openid>"
                    + "<out_trade_no>" + orderFormId + "</out_trade_no>"
                    + "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>"
                    + "<total_fee>" + money + "</total_fee>"
                    + "<trade_type>" + WxPayConfig.TRADETYPE + "</trade_type>"
                    + "<sign>" + mysign + "</sign>"
                    + "</xml>";
            log.info("firstPayxml:\n"+xml);
            //调用统一下单接口，并接受返回的结果
            // 将解析结果存储在HashMap中
            Map map = PayUtil.httpRequest(WxPayConfig.pay_url, "POST", xml);
            String return_code = (String) map.get("return_code");//返回状态码

            //返回给移动端需要的参数
            Map<String, Object> response = new HashMap<String, Object>();
            if(return_code.equals("SUCCESS")){
                //首先判断通信是否成功
                if(map.get("result_code").equals("SUCCESS")) {
                    //接下来判断交易是否成功
                    if (PayUtil.verify(PayUtil.createLinkString(PayUtil.paraFilter(map)), (String)map.get("sign"), WxPayConfig.key, "utf-8")) {
                        //最后判断微信传过来的sign是否正确
                        String prepay_id = (String) map.get("prepay_id");//返回的预付单信息
                        response.put("nonceStr", nonce_str);
                        response.put("package", "prepay_id=" + prepay_id);
                        Long timeStamp = System.currentTimeMillis() / 1000;
                        response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误

                        String stringSignTemp = "appId=" + WxPayConfig.appid + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id + "&signType=" + WxPayConfig.SIGNTYPE + "&timeStamp=" + timeStamp;
                        //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                        String paySign = PayUtil.sign(stringSignTemp, WxPayConfig.key, "utf-8").toUpperCase();
                        response.put("paySign", paySign);
                    }
                    else{
                        throw new WeixinException("签名不正确，数据无效，请重新支付！ "+orderFormId);
                    }
                }
                else{
                    throw new WeixinException((String) map.get("err_code")+(String)map.get("err_code_des"));
                }
            }
            else{
                throw new WeixinException((String) map.get("return_msg"));
            }
            response.put("appid", WxPayConfig.appid);
            json.setSuccess(true);
            json.setData(response);
        }catch(Exception e){
            log.error(e.getMessage());
            json.setSuccess(false);
            json.setMsg(e.getMessage());
            return ResponseEntity.status(500).body(json);
        }
        return ResponseEntity.ok(json);
    }

    /**
     * @Description:微信支付通知路径
     * @return
     * @throws Exception
     * @throws WeixinException
     */
    @RequestMapping(value="/wxNotify")
    public void wxNotify(HttpServletRequest request,HttpServletResponse response) throws Exception{
        String resXml = "";
        Map map = this.getResultMap(new InputStreamReader((ServletInputStream)request.getInputStream()));
        //获得返回的报文，并转为map
        String returnCode = (String) map.get("return_code");
        if("SUCCESS".equals(returnCode)){
            //判断微信支付是否成功
            if("SUCCESS".equals((String) map.get("result_code"))){
                //验证签名是否正确
                if (PayUtil.verify(PayUtil.createLinkString(PayUtil.paraFilter(map)), (String) map.get("sign"), WxPayConfig.key, "utf-8")) {
                    /**此处添加自己的业务逻辑代码start**/
                    String orderFormId = (String) map.get("out_trade_no");
                    OrderForm orderForm = orderFormService.selectByPrimaryKey(orderFormId);
                    int money = Math.round((orderForm.getSum() * 100));
                    //判断订单金额是否与商户侧的订单金额一致
                    if (money == Integer.parseInt((String) map.get("total_fee"))) {
                        if (orderForm.getState() == 1) {
                            //将订单状态变成待发货状态
                            orderFormService.update(orderFormId, 2);
                            //通知微信服务器已经支付成功
                            log.info(orderFormId + " pay success!");
                            resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                                    + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                        }
                    } else {
                        throw new RuntimeException("orderForm：" + orderFormId + "The payment amount is inconsistent with the order amount on the merchant side!\n" +
                                "Please contact customer service!");
                    }
                    /**此处添加自己的业务逻辑代码start**/
                } else {
                    log.error("the return sign is not corret! orderFormId:"+map.get("out_trade_no"));
                }
            } else {
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            }
        }else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
        //如果支付失败，报错
        if(resXml.equals("<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ")){
            throw new RuntimeException("微信支付失败!orderFormID："+map.get("out_trade_no"));
        }
    }


    /**
     * 发起退款
     * @param orderFormId
     * @throws Exception
     */
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value="/wxRefund")
    public ResponseEntity<Boolean> wxRefund(@RequestParam String orderFormId) throws Exception {
        OrderForm orderForm = orderFormService.selectByPrimaryKey(orderFormId);//拿到支付订单
        if(orderForm.getState()!=2){
            return  ResponseEntity.status(400).body(Boolean.FALSE);
        }
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File("C:/Program Files/zapp/zapp/dph.p12"));//读取商户证书
        try {
            keyStore.load(instream, "1510906771".toCharArray());//这里的密码即为商户号
            }catch (Exception e){
                log.error(e.getMessage());
                return ResponseEntity.status(500).body(Boolean.FALSE);
         } finally {
                instream.close();
            }
            // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                        .loadKeyMaterial(keyStore, "1510906771".toCharArray())
                        .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpClient = HttpClients.custom()
                        .setSSLSocketFactory(sslsf)
                        .build();
        try {
            //发起退款请求
            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
            // 构造消息头
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");
            //准备参数
            //生成的随机字符串
            String nonce_str = StringUtils.getRandomStringByLength(32);
            String money = Math.round((orderForm.getSum()*100))+"";//支付金额，单位：分，这边需要转成字符串类型，否则后面的签名会失败
            Map<String, String> packageParams = new HashMap<String, String>();
            packageParams.put("appid", WxPayConfig.appid);
            packageParams.put("mch_id", WxPayConfig.mch_id);
            packageParams.put("nonce_str", nonce_str);
            packageParams.put("out_trade_no", orderFormId);//商户订单号
            packageParams.put("out_refund_no", orderFormId);//商户退款单号
            packageParams.put("total_fee", money);//支付金额，这边需要转成字符串类型，否则后面的签名会失败
            packageParams.put("refund_fee", money);//退款金额
            // 除去数组中的空值和签名参数
            packageParams = PayUtil.paraFilter(packageParams);
            String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            //MD5运算生成签名，用于调用退款接口
            String mysign = PayUtil.sign(prestr, WxPayConfig.key, "utf-8").toUpperCase();
            //拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
            String xml = "<xml>" + "<appid>" + WxPayConfig.appid + "</appid>"
                    + "<mch_id>" + WxPayConfig.mch_id + "</mch_id>"
                    + "<nonce_str>" + nonce_str + "</nonce_str>"
                    + "<out_trade_no>" + orderFormId + "</out_trade_no>"
                    + "<out_refund_no>" + orderFormId + "</out_refund_no>"
                    + "<total_fee>" + money + "</total_fee>"
                    + "<refund_fee>" + money + "</refund_fee>"
                    + "<sign>" + mysign + "</sign>"
                    + "</xml>";
            // 构建消息实体
            StringEntity entity = new StringEntity(xml, Charset.forName("UTF-8"));
            entity.setContentEncoding("UTF-8");
            // 发送Json格式的数据请求
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    Map map = getResultMap(new InputStreamReader(responseEntity.getContent()));
                    String returnCode = (String) map.get("return_code");
                    if ("SUCCESS".equals(returnCode)) {
                        if ("SUCCESS".equals((String) map.get("result_code"))) {
                            //验证签名是否正确
                            String refunrOrderForm = (String) map.get("out_trade_no");//退款订单号
                            if (PayUtil.verify(PayUtil.createLinkString(PayUtil.paraFilter(map)), (String) map.get("sign"), WxPayConfig.key, "utf-8")) {
                                /**此处添加自己的业务逻辑代码start**/
                                orderFormService.update(refunrOrderForm, 5);//订单跳到失效
                                log.info("orderForm："+refunrOrderForm+" refunded successfully!");
                                /**此处添加自己的业务逻辑代码end**/
                            }
                        } else {
                            throw new WeixinException((String) map.get("orderFormId:"+orderFormId+"  err_code:"+(String) map.get("err_code") ));
                        }

                    } else {
                        throw new WeixinException((String) map.get("orderFormId:"+orderFormId+"   return_code") + (String) map.get("return_msg"));
                    }
                    EntityUtils.consume(responseEntity);
                } else {
                    throw new RuntimeException("发起退款请求返回为空!请重试！");
                }
            }catch (Exception e){
                log.error("refund 1:"+e.getMessage());
                return ResponseEntity.status(500).body(Boolean.FALSE);
            } finally {
                    response.close();
                }
        }catch (Exception e){
            log.error("refund 2:"+e.getMessage());
            return ResponseEntity.status(500).body(Boolean.FALSE);
        } finally {
                    httpClient.close();
        }
        return ResponseEntity.ok(Boolean.TRUE);
    }


    private Map getResultMap(InputStreamReader inputStreamReader) throws Exception {
        String resultXml = PayUtil.readFormServer(inputStreamReader);//读取返回的报文
        log.info("wechat xml:" + resultXml);
        //解析接收到的报文
        Map map = PayUtil.doXMLParse(resultXml);

        return map;
    }
}
