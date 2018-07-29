package com.yidong.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.yidong.model.User;

import java.util.HashMap;
import java.util.Map;

public interface UserService {
    Map<String,String> login(String account, String password);

    boolean selectByAccount(String account);

    boolean insert(String account,String password,String  name);

    User selectByPrimaryKey(String id);

    boolean updatePasswordByAccount(String account, String password);

}
