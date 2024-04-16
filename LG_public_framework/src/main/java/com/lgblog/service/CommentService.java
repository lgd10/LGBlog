package com.lgblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lgblog.entity.Comment;
import com.lgblog.result.ResponseResult;


/**
 * 评论表(Comment)表服务接口
 *
 * @author gyd
 * @since 2023-12-04 10:32:50
 */
public interface CommentService extends IService<Comment> {

    ResponseResult getCommentList(String commentType,Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
