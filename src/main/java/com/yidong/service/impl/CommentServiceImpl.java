package com.yidong.service.impl;

import com.yidong.mapper.CommentMapper;
import com.yidong.model.Comment;
import com.yidong.service.CommentService;
import com.yidong.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public boolean deleteByPrimaryKey(String id) {
        return commentMapper.deleteByPrimaryKey(id)==0?false:true;
    }

    @Override
    public boolean insert(int star,String comment,
                        String  account,String goodsId) {
        String userId = commentMapper.getUserId(account);
        Map map = new HashMap();
        map.put("star",star);
        map.put("comment",comment);
        map.put("userId",userId);
        map.put("goodsId",goodsId);
        map.put("id", UUIDUtils.getUUID());
        return commentMapper.insert(map)==0?false:true;
    }

    @Override
    public List<Comment> selectByGoodsId(String goodsId) {
        return commentMapper.selectByGoodsId(goodsId);
    }

    @Override
    public List<Comment> selectByUserId(String userId) {
        return commentMapper.selectByUserId(userId);
    }
}
