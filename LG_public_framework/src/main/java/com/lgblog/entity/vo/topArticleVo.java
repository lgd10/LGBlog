package com.lgblog.entity.vo;

import lombok.Data;

/**
 * 有时候前端不需要过多的数据，比如我现在只需要文章的id标题还有浏览量做一个排行榜，这个时候文章的内容显得不是那么重要
 */
@Data
public class topArticleVo {
    private Long id;
    //标题
    private String title;
    //访问量
    private Long viewCount;
}
