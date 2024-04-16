package com.lgblog.controller;

import com.lgblog.entity.Comment;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.CommentService;
import com.lgblog.util.copyBeanUtil.BeanCopy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.lgblog.statement.statementVal;
import com.lgblog.entity.dto.AddCommentDto;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论控制层",description = "评论相关接口")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 该方法是对文章评论的查询
     * @param articleId 文章id
     * @param pageNum 分页数量
     * @param pageSize 单页大小
     * @return
     */
    @GetMapping("/commentList")
    public ResponseResult getCommentList(Long articleId,Integer pageNum,Integer pageSize){
        return commentService.getCommentList(statementVal.ARTICLE_COMMENT,articleId,pageNum,pageSize);
    }
    /**
     * 实现友链评论查询，友链不需要文章id
     */
    @ApiOperation(value = "友链评论列表")
    @ApiImplicitParams//swagger参数说明
    ({
            @ApiImplicitParam(name = "pageNum",value = "页码"),
            @ApiImplicitParam(name = "pageSize",value = "分页大小")
    })
    @GetMapping("/linkCommentList")
    public ResponseResult commentLinkList(Integer pageNum,Integer pageSize)
    {
        return commentService.getCommentList(statementVal.LINK_COMMENT,null,pageNum,pageSize);
    }
    //实现添加评论(即发送评论)
    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto)
    {
        Comment comment = BeanCopy.copyByBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);
    }
}
