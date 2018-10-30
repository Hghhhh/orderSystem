package com.yidong.util;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;

import java.io.IOException;
import java.util.Random;

public class TencentSmsSender {

    // 短信应用SDK AppID
    private static final int appid = 1400122024; // 1400开头

    // 短信应用SDK AppKey
    private static final  String appkey = "86dbf88cc0abd417897ea9987eb9cb8f";

    // 签名
    private static final  String smsSign = "东兴隆冻品会";

    private static final int templateId = 171182;
    public static String sendMessage(String  phoneNumber){
        try {
            String[] params = {""};
            params[0] = randomNum();
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            SmsSingleSenderResult result = ssender.sendWithParam("86",phoneNumber,templateId,params,smsSign,"",""); // 签名参数未提供或者为空时，会使用默认签名发送短信
            return params[0];
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }
        return null;
    }

    public static String randomNum(){
        Random random = new Random();
        StringBuffer num = new StringBuffer();
        num.append(random.nextInt(10));
        num.append(random.nextInt(10));
        num.append(random.nextInt(10));
        num.append(random.nextInt(10));
        return num.toString();
    }
}
