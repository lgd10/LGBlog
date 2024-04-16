package com.lgblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lgblog.entity.Article;
import org.apache.ibatis.annotations.Mapper;


/**
 * 文章表(Article)表数据库访问层
 *
 * @author gyd
 * @since 2023-11-22 17:08:23
 */
@Mapper
public interface ArticleDao extends BaseMapper<Article> {
}
