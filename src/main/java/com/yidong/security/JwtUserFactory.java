package com.yidong.security;

import com.yidong.model.JwtUser;
import com.yidong.model.User;


public final class JwtUserFactory {


    /**
     * 根据user对象生成userDetail对象
     * @param user
     * @return
     */
    public static JwtUser create(User user) {
      return new JwtUser(user.getId(),user.getAccount(),user.getPassword());
    }

}