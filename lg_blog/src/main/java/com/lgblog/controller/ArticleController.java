package com.lgblog.controller;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章表(Article)表控制层
 *
 * @author makejava
 * @since 2023-11-22 17:10:12
 */
@RestController
@RequestMapping("article")
public class ArticleController {
    /**
     * 服务对象
     */
    @Autowired
    private ArticleService articleService;
    // @GetMapping("/list")
    // public List<Article> test(){
    //     return articleService.list();
    // }
    @GetMapping("/topList")
    public ResponseResult topList()
    {
        return articleService.topList();
    }
    @GetMapping("/list")
    public ResponseResult categoryList(Integer pageNum,Integer pageSize, Long categoryId){
        return articleService.categoryList(pageNum,pageSize,categoryId);
    }
    @GetMapping("/{id}")
    public ResponseResult articleDetail(@PathVariable Long id)
    {
        // System.out.println("==========执行文章详情接口==========");
        return articleService.articleDetail(id);
    }
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable Long id)
    {
        return articleService.updateViewCount(id);
    }




}

