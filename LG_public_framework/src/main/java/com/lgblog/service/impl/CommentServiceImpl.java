package com.lgblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lgblog.dao.CommentDao;
import com.lgblog.entity.Comment;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.CommentService;
import com.lgblog.service.UserService;
import com.lgblog.util.copyBeanUtil.BeanCopy;
import com.lgblog.entity.vo.CommentVo;
import com.lgblog.entity.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lgblog.statement.statementVal;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author gyd
 * @since 2023-12-04 10:32:50
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {
    @Autowired
    private UserService userService;
    @Override
    public ResponseResult getCommentList(String commentType,Long articleId, Integer pageNum, Integer pageSize) {
        //先不考虑子评论
        //查询文章对应的根评论
        //root评论的rootId为-1
        LambdaQueryWrapper<Comment> wrapper=new LambdaQueryWrapper<>();
        //说明是友链评论请求(0代表文章评论，1代表友链评论)
        // if (statementVal.LINK_COMMENT.equals(commentType)){//常量写在前面，避免出现空指针异常
        //     wrapper.eq(Comment::getRootId,statementVal.PID);
        // }else{
        //     wrapper.eq(Comment::getArticleId,articleId)
        //             .eq(Comment::getRootId,statementVal.PID);
        // }
        wrapper.eq(statementVal.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId);
        wrapper.eq(Comment::getRootId,statementVal.PID);
        wrapper.eq(Comment::getType,commentType);
        Page<Comment> commentPage=new Page<>(pageNum,pageSize);
        page(commentPage,wrapper);
        List<Comment> comments = commentPage.getRecords();
        // System.out.println(comments);
        List<CommentVo> commentVos = transportComment(comments);//使用工具方法实现转换
        //查询子评论
        /**
         * 子评论查询逻辑
         * 先将评论遍历并将所有id依次传入获取子评论的方法中(getChildrenList)
         * 然后该方法将传入的id进行getRootId进行条件查询即可
         */
        // commentVos.stream()
        //         .map(commentVo -> commentVo.setChildren(getChildrenList(commentVo.getId())));
         for (CommentVo commentVo : commentVos) {
             commentVo.setChildren(getChildrenList(commentVo.getId()));
         }
        return ResponseResult.okResult(new PageVo(commentVos, commentPage.getTotal()));
    }

    //发表评论
    @Override
    public ResponseResult addComment(Comment comment) {
        save(comment);
        return ResponseResult.okResult();
    }

    /**
     * 把一些用户名相关的字段赋值到commentVo中
     */
    private List<CommentVo> transportComment(List<Comment> comments)
    {
        List<CommentVo> commentVos = BeanCopy.copyListByBean(comments, CommentVo.class);
        for (CommentVo commentVo : commentVos) {
            //需要根据commentVo类里面的createBy字段，然后用createBy字段去查询user表的nickname字段(子评论的用户昵称)
            // System.out.println("aaaaaaaaa"+commentVo);
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            // System.out.println(nickName);
            //然后把nickname字段(发这条子评论的用户昵称)的数据赋值给commentVo类的username字段
            commentVo.setUsername(nickName);

            //获取头像的url路径
            String imgUrl=userService.getById(commentVo.getCreateBy()).getAvatar();
            commentVo.setAvatar(imgUrl);

            //查询根评论的用户昵称。怎么判断是根评论的用户呢，判断toCommentId为1，就表示这条评论是根评论
            if(commentVo.getToCommentUserId() != -1){
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                //然后把nickname字段(发这条根评论的用户昵称)的数据赋值给commentVo类的toCommentUserName字段
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVos;
    }

    /**
     *
     * @param pid 传入评论自身的id，是子评论的父id(根评论的id)
     * @return
     */
    private List<CommentVo> getChildrenList(Long pid)
    {
        LambdaQueryWrapper<Comment> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getRootId,pid)
               .orderByAsc(Comment::getCreateTime);
        List<Comment> list = list(wrapper);
        List<CommentVo> commentVos = transportComment(list);//去找到评论的人
        return commentVos;
    }


}
