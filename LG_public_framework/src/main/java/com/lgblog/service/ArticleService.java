package com.lgblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lgblog.entity.Article;
import com.lgblog.entity.dto.AddArticleDto;
import com.lgblog.entity.dto.ArticleListDto;
import com.lgblog.entity.dto.ArticleUpdateDto;
import com.lgblog.result.ResponseResult;


/**
 * 文章表(Article)表服务接口
 *
 * @author LG
 * @since 2023-11-22 17:08:34
 */
public interface ArticleService extends IService<Article> {
    ResponseResult topList();

    ResponseResult categoryList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult articleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addArticleForAdmin(AddArticleDto addArticleDto);

    ResponseResult getArticleListForAdmin(Integer pageNum, Integer pageSize, ArticleListDto articleListDto);

    ResponseResult getDetailForAdmin(Long id);

    ResponseResult updateArticleForAdmin(ArticleUpdateDto dto);

    ResponseResult deleteArticleForAdmin(Long id);
}
