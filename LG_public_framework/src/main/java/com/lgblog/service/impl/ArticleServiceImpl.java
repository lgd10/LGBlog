package com.lgblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lgblog.dao.ArticleDao;
import com.lgblog.entity.Article;
import com.lgblog.entity.ArticleTag;
import com.lgblog.entity.Category;
import com.lgblog.entity.dto.AddArticleDto;
import com.lgblog.entity.dto.ArticleListDto;
import com.lgblog.entity.dto.ArticleUpdateDto;
import com.lgblog.entity.vo.*;
import com.lgblog.entity.vo.adminVo.AdminArticleListVo;
import com.lgblog.entity.vo.adminVo.AdminArticleUpdateVo;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.*;
import com.lgblog.statement.statementVal;
import com.lgblog.util.copyBeanUtil.BeanCopy;
import com.lgblog.util.redisUtil.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author gyd
 * @since 2023-11-22 17:08:40
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleDao, Article> implements ArticleService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private ArticleVoService articleVoService;

    @Autowired
    private TagService tagService;
    /**
     * 实现热门文章查询
     * @return
     */
    @Override
    public ResponseResult topList() {
        LambdaQueryWrapper<Article> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus,statementVal.ARTICLE_STATUS_NORMAL)
               .orderByDesc(Article::getViewCount);
        Page<Article> articlePage=new Page<>(statementVal.ARTICLE_STATUS_CURRENT,statementVal.ARTICLE_STATUS_SIZE);
        page(articlePage,wrapper);
        List<Article> records = articlePage.getRecords();

        //使用数据流的方式为列表的每一个浏览量赋值，使得可以实时更新浏览次数
        List<Article> updatedRecords = records.stream()
                .map(record -> {
                    Integer viewCount = redisCache.getCacheMapValue(statementVal.ARTICLE_VIEWCOUNT_KEY, record.getId().toString());
                    record.setViewCount(viewCount.longValue());
                    System.out.println(record.getTitle() + "浏览量是" + viewCount);
                    return record;
                })
                .collect(Collectors.toList());
        List<topArticleVo> topArticleVoList = BeanCopy.copyListByBean(updatedRecords, topArticleVo.class);
        return ResponseResult.okResult(topArticleVoList);
    }

    /**
     * 实现文章列表查询
     * @param pageNum 分页数量
     * @param pageSize 每一页包含的文章数
     * @param categoryId 传入的文章分类id
     * @return
     */

    @Override
    public ResponseResult categoryList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus,statementVal.ARTICLE_STATUS_NORMAL) //文章正常发布
               .eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId)//如果传入category的id就查询该分类的文章
               .orderByDesc(Article::getIsTop);//按照置顶状态倒序查询，使得文章能置顶
        Page<Article> articlePage=new Page<>(pageNum,pageSize);
        page(articlePage,wrapper);
        List<Article> articles = articlePage.getRecords();
        /**
         * 使用for循环为article的categoryName属性赋值
         * for (Article article : articles) {
         *             Category category = categoryService.getById(article.getCategoryId());
         *             article.setCategoryName(category.getName());
         *         }
         */
        /**
         * 使用stream流方式article的categoryName属性赋值
         * 尖括号内有两个泛型，第一个是传入的参数，第二个是返回的参数
         */
        articles.stream().map(new Function<Article, Article>() {
            @Override
            public Article apply(Article article) {
                //'article.getCategoryId()'表示从article表获取category_id字段，然后作为查询category表的name字段
                Category category = categoryService.getById(article.getCategoryId());
                String name = category.getName();
                //把查询出来的category表的name字段值，也就是article，设置给Article实体类的categoryName成员变量
                article.setCategoryName(name);
                //把查询出来的category表的name字段值，也就是article，设置给Article实体类的categoryName成员变量
                return article;
            }
        }).collect(Collectors.toList());
        List<ArticleListVo> articleListVos = BeanCopy.copyListByBean(articles, ArticleListVo.class);
        //把数据封装到page类里面
        PageVo pageVo = new PageVo(articleListVos,articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }


    /**
     * 文章详情页面查询
     * @param id 根据前端传递的id获取文章详情
     * @return
     */
    @Override
    public ResponseResult articleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取浏览量
        Integer viewCount = redisCache.getCacheMapValue(statementVal.ARTICLE_VIEWCOUNT_KEY, id.toString());
        //赋值浏览量
        article.setViewCount(viewCount.longValue());
        //把最后的查询结果封装成ArticleListVo(我们写的实体类)。BeanCopyUtils是我们写的工具类
        ArticleDetailVo articleDetailVo = BeanCopy.copyByBean(article, ArticleDetailVo.class);

        //根据分类id，来查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        //如果根据分类id查询的到分类名，那么就把查询到的值设置给ArticleDetailVo实体类的categoryName字段
        if(category!=null){
            articleDetailVo.setCategoryName(category.getName());
        }

        //封装
        return ResponseResult.okResult(articleDetailVo);
    }

    /**
     * 将访问的对应的文章的阅读量+1并存放至redis中
     * @param id 文章id
     * @return
     */
    @Override
    public ResponseResult updateViewCount(Long id) {
        String hKey=id.toString();
        //更新redis中对应文章的浏览量
        redisCache.increaseViewCount(statementVal.ARTICLE_VIEWCOUNT_KEY,hKey,1);
        return ResponseResult.okResult();
    }

    /**
     * 写博文接口实现层
     * @param addArticleDto 博文封装类
     * @return
     */
    @Override
    @Transactional
    //由于是对多个数据库表进行操作，因此需要保证事务的一致性 所以需要启用事务注解
    public ResponseResult addArticleForAdmin(AddArticleDto addArticleDto) {
        List<Long> tags = addArticleDto.getTags();
        ArticleVo articleVo = BeanCopy.copyByBean(addArticleDto, ArticleVo.class);
        //这里引入vo保存是因为如果在原本的类上使用自动填充会导致redis获取浏览量空指针异常
        articleVoService.save(articleVo);
        Long articleId = articleVo.getId();
        List<ArticleTag> articleTags = tags
                .stream()
                .map(tagId -> new ArticleTag(articleId, tagId))
                .collect(Collectors.toList());
        //保存list集合内部的所有数据
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    /**
     * 后台文章列表实现层
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param articleListDto 文章标题摘要封装类
     * @return
     */
    @Override
    public ResponseResult getArticleListForAdmin(Integer pageNum, Integer pageSize, ArticleListDto articleListDto) {
        List<Article> articles = list();
        List<AdminArticleListVo> adminArticleListVos = BeanCopy.copyListByBean(articles, AdminArticleListVo.class);
        LambdaQueryWrapper<Article> wrapper=new LambdaQueryWrapper<>();
        //实现对摘要和标题的模糊查询
        wrapper.like(StringUtils.hasText(articleListDto.getTitle()),Article::getTitle,articleListDto.getTitle())
               .like(StringUtils.hasText(articleListDto.getSummary()),Article::getSummary,articleListDto.getSummary());
        Page<Article> articlePage=new Page<>();
        articlePage.setSize(pageSize).setCurrent(pageNum);
        page(articlePage,wrapper);
        PageVo pageVo=new PageVo(articlePage.getRecords(),articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getDetailForAdmin(Long id) {
        LambdaQueryWrapper<ArticleTag> wrapper=new LambdaQueryWrapper<>();
        //先获取文章的信息
        Article article = getById(id);
        //再给文章封装的vo赋值
        AdminArticleUpdateVo adminArticleUpdateVo = BeanCopy.copyByBean(article, AdminArticleUpdateVo.class);
        wrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> articleTags = articleTagService.list(wrapper);
        //使用stream流将里面的tag的集合获取出来
        List<Long> tags = articleTags
                        .stream()
                        .map(articleTag -> articleTag.getTagId())
                        .collect(Collectors.toList());
        //为其中的tags集合赋值
        adminArticleUpdateVo.setTags(tags);
        return ResponseResult.okResult(adminArticleUpdateVo);
    }

    /**
     * 修改文章实现接口
     * @param dto 修改文章前端数据封装类
     * @return
     */
    @Override
    public ResponseResult updateArticleForAdmin(ArticleUpdateDto dto) {
        Long articleId=dto.getId();
        LambdaQueryWrapper<ArticleTag> wrapper=new LambdaQueryWrapper<>();
        List<Long> tags = dto.getTags();
        Article article = BeanCopy.copyByBean(dto, Article.class);
        //先更新文章表
        updateById(article);
        //由于标签可能增加也有可能减少，因此需要删除所有关于原来的标签再插入新的标签
        //先删除原来的标签
        wrapper.eq(ArticleTag::getArticleId,articleId);
        articleTagService.remove(wrapper);
        //删除之后转化一个articleTags的集合然后将该集合保存到数据库中
        List<ArticleTag> articleTags = tags.stream().map(id -> new ArticleTag(articleId, id)).collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    /**
     * 逻辑删除实现层
     * @param id 删除文章的id
     * @return
     */
    @Override
    public ResponseResult deleteArticleForAdmin(Long id) {
        LambdaUpdateWrapper<Article> wrapper=new LambdaUpdateWrapper<>();
        wrapper.eq(Article::getId,id).set(Article::getDelFlag,statementVal.ARTICLE_DELETE);
        update(null,wrapper);
        return ResponseResult.okResult();
    }


}
