package com.lgblog.Job;

import com.lgblog.entity.Article;
import com.lgblog.service.ArticleService;
import com.lgblog.util.redisUtil.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.lgblog.statement.statementVal;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class updateMySQLJob {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;
    @Scheduled(cron = "2-2 0/10 * * * ? ")//每隔十分钟执行一次
    // @Scheduled(cron = "0/15 * * * * ? ")//(测试)每相隔十五秒执行一次
    public void updateViewCount()
    {
        System.out.println("定时更新文章浏览量开始执行");
        Map<String, Integer> cacheMap = redisCache.getCacheMap(statementVal.ARTICLE_VIEWCOUNT_KEY);
        List<Article> articleList = cacheMap
                .entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        //批量操作修改阅读访问量
        articleService.updateBatchById(articleList);
    }
}
