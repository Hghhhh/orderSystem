package com.yidong.service.impl;

import com.yidong.mapper.UserMapper;
import com.yidong.model.JwtUser;
import com.yidong.model.User;
import com.yidong.service.UserService;
import com.yidong.util.JwtTokenUtil;
import com.yidong.util.MyPasswordEncoder;
import com.yidong.util.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtTokenUtil jwtTokenUtil;
    private UserMapper userMapper;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    public UserServiceImpl(
         @Qualifier("JwtUserDetailsServiceImpl") UserDetailsService userDetailsService,
            JwtTokenUtil jwtTokenUtil,
            UserMapper userMapper,
         AuthenticationManager authenticationManager
            ) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
    }

    /**
     * 登录的方法
     * @param account
     * @param password
     * @return 一个jwt的token
     */
    @Override
    public Map<String,String> login(String account, String password) {
        //这一部分生成根据账号密码authentication用来验证账号密码
        UsernamePasswordAuthenticationToken unAuthentication = new UsernamePasswordAuthenticationToken(account, password);//还未验证的认证
        final Authentication authentication = authenticationManager.authenticate(unAuthentication);//验证认证
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final JwtUser userDetails =(JwtUser)userDetailsService.loadUserByUsername(account);

        final String token = jwtTokenUtil.generateToken(userDetails);
        //返回用户的信息
        Map<String,String> userMap  = new HashMap<String ,String>();
        userMap.put("id",userDetails.getId());
        userMap.put("account",userDetails.getUsername());
        userMap.put("token",token);
        return userMap;
    }

    /**
     * 刷新过期token
     * @param oldToken
     * @return newToken
     */
   /** @Override
    public String refresh(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());//获得“bearer”后面的部分
        String account = jwtTokenUtil.getAccountFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(account);//验证数据库中有无此用户
        if (jwtTokenUtil.canTokenBeRefreshed(token)&&user!=null){
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }
    */

    @Override
    public boolean selectByAccount(String account) {
        return userMapper.selectByAccount(account)==null?false:true;
    }


    @Override
    public boolean insert(String account,String password,String name) {
        PasswordEncoder passwordEncoder = new MyPasswordEncoder();
        User user = new User();
        String userId = UUIDUtils.getUUID();
        user.setId(userId);
        user.setAccount(account);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        log.info("新注册用户：account:"+account);
        Map<String,String> map = new HashMap<String,String>();
        map.put("id",UUIDUtils.getUUID());
        map.put("userId",userId);
        userMapper.insertShoppingCar(map);
        return userMapper.insert(user)==1?true:false;
    }

    @Override
    public User selectByPrimaryKey(String id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updatePasswordByAccount(String account, String password) {
        Map<String,String> map = new HashMap<String,String>();
        map.put("account",account);
        PasswordEncoder passwordEncoder = new MyPasswordEncoder();
        map.put("password",passwordEncoder.encode(password));
        return userMapper.updatePasswordByAccount(map)==1?true:false;
    }
}
