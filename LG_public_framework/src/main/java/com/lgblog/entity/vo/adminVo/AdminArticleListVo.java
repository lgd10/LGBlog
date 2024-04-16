package com.lgblog.entity.vo.adminVo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 后台文章列表所用的数据封装类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminArticleListVo {
    @TableId
    private Long id;
    //标题
    private String title;
    //文章内容
    private String content;
    //文章摘要
    private String summary;
    //所属分类id
    private Long categoryId;
    //缩略图
    private String thumbnail;
    //是否置顶（0否，1是）
    private String isTop;
    //状态（0已发布，1草稿）
    private String status;
    //访问量
    private Long viewCount;
    //是否允许评论 1是，0否
    private String isComment;

    //新增博客文章-使用mybatisplus的字段自增
    private Date createTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;


}
