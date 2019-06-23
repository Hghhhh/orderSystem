package com.yidong;

import java.util.List;
import java.util.Map;

import com.yidong.config.WxPayConfig;
import com.yidong.mapper.GoodsMapper;
import com.yidong.util.OrderFormId;
import com.yidong.util.PayUtil;
import com.yidong.util.UUIDUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrdersystemApplicationTests {

	@Autowired
	GoodsMapper goodsMapper;

	@Test
	public void contextLoads() throws Exception {

	}

}
