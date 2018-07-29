package com.yidong.service;

import com.yidong.model.Comment;

import java.util.List;
import java.util.Map;

public interface CommentService {
    boolean deleteByPrimaryKey(String id);

    boolean insert(int star,String comment,
                        String  userId,String goodsId);

    List<Comment> selectByGoodsId(String goodsId);

    List<Comment> selectByUserId(String userId);

}
