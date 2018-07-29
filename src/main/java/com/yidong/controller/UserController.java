package com.yidong.controller;

import com.yidong.service.UserService;
import com.yidong.util.TencentSmsSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private UserService userService;

    /**
     * 接收账号密码登录成功后返回一个token
     * @param account
     * @param password
     * @return token
     * @throws AuthenticationException
     */
    @RequestMapping(value = "/login")
    public ResponseEntity<Map<String,String>> login(
            @RequestParam String account, @RequestParam String password) {
        ResponseEntity<Map<String,String>> responseEntity ;
        if((userService.selectByAccount(account))){
            //判断用户是否存在
            Map<String,String> userMap;
            userMap = userService.login(account, password);
            responseEntity = new ResponseEntity<Map<String,String>>(userMap,HttpStatus.OK);
        }
        else{
            responseEntity = new ResponseEntity<Map<String,String>>(new HashMap<String,String>(),HttpStatus.BAD_REQUEST);
        }
        // Return the token
        return responseEntity;
    }

    /**
     * 获取验证码
     * @param phoneNum
     * @return 验证码
     */
    @RequestMapping(value="/getSms")
    public String getSms(@RequestParam String phoneNum){
        return TencentSmsSender.sendMessage(phoneNum);
    }

    @RequestMapping(value = "/register")
    public ResponseEntity<Boolean> register(
            @RequestParam String account, @RequestParam String password,@RequestParam String name) {
        if(userService.selectByAccount(account)) return new ResponseEntity(false,HttpStatus.CONFLICT);
        //账号已经存在，返回409状态码
        if(userService.insert(account,password,name)){
            return new ResponseEntity(true,HttpStatus.OK);
        }
        //注册成功返回200
        else return new ResponseEntity(false,HttpStatus.INTERNAL_SERVER_ERROR);
        //失败返回500
    }

    @RequestMapping(value = "/updatePassword")
    public ResponseEntity updatePassword(
            @RequestParam String account, @RequestParam String password){
        if(!userService.selectByAccount(account)) return new ResponseEntity(HttpStatus.CONFLICT);
        //账号不存在，返回409状态码
        if(userService.updatePasswordByAccount(account,password)){
            return new ResponseEntity(HttpStatus.OK);
        }
        else return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
   /**
     * 用于更新token
     * @param request
     * @return
     * @throws AuthenticationException
     */
   /**
    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<String> refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws AuthenticationException{
        String token = request.getHeader(tokenHeader);
        String refreshedToken = userService.refresh(token);
        if(refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(refreshedToken);
        }
    }
*/


}
