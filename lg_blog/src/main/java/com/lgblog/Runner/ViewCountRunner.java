package com.lgblog.Runner;

import com.lgblog.dao.ArticleDao;
import com.lgblog.entity.Article;
import com.lgblog.util.redisUtil.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.lgblog.statement.statementVal;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 该类实现了CommandLineRunner接口，会在程序初始化的时候执行run方法里面的内容
 */
@Component
public class ViewCountRunner implements CommandLineRunner {
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private RedisCache redisCache;
    @Override
    public void run(String... args) throws Exception {
        //获取文章id和文章的浏览量
        List<Article> articleList = articleDao.selectList(null);
        Map<String , Integer> articleMap = articleList.stream().collect(Collectors.toMap
        (
            new Function<Article, String>() {
            @Override
            public String  apply(Article article) {
                return article.getId().toString();
            }
        }
        ,
            new Function<Article, Integer>() {
            @Override
            public Integer apply(Article article) {
                return article.getViewCount().intValue();
            }
        }
        ));
        //将数据存放到redis中
        redisCache.setCacheMap(statementVal.ARTICLE_VIEWCOUNT_KEY,articleMap);
        System.out.println(articleMap);
    }
}
