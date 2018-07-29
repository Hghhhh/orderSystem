package com.yidong.service;

import com.yidong.model.BigType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface BigTypeService {

     List<BigType> selectBigType();
}
