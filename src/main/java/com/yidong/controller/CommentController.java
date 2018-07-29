package com.yidong.controller;

import com.yidong.model.Comment;
import com.yidong.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value="/deleteComment")
    public boolean deleteByPrimaryKey(@RequestParam String commentId) {
        return commentService.deleteByPrimaryKey(commentId);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value="/createComment")
    public boolean insert(HttpServletRequest request, @RequestParam int star, @RequestParam String comment,
                          @RequestParam String goodsId) {
        return commentService.insert(star,comment,(String)request.getAttribute("account"),goodsId);
    }

    @RequestMapping(value="/selectCommentByGoodsId")
    public List<Comment> selectByGoodsId(@RequestParam String goodsId) {
        return commentService.selectByGoodsId(goodsId);
    }


    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value="/selectComment")
    public List<Comment> selectByUserId(HttpServletRequest request) {
        return commentService.selectByUserId((String)request.getAttribute("account"));
    }
}
