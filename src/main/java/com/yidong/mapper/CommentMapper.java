package com.yidong.mapper;

import com.yidong.model.Comment;

import java.util.List;
import java.util.Map;

public interface CommentMapper {
    int deleteByPrimaryKey(String id);

    int insert(Map map);

    List<Comment> selectByGoodsId(String goodsId);

    List<Comment> selectByUserId(String userId);

}