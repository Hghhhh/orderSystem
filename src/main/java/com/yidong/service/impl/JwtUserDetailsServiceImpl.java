package com.yidong.service.impl;
/***
 *用于根据用户名加载对应的UserDetails
 */

import com.yidong.security.JwtUserFactory;
import com.yidong.mapper.UserMapper;
import com.yidong.model.User;
import com.yidong.security.JwtUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("JwtUserDetailsServiceImpl")
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;//这里会报错，但是并不会影响

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        User user = userMapper.selectByAccount(account);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", account));
        } else {
            return  JwtUserFactory.create(user);
        }

    }
}
