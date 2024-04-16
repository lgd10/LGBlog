package com.lgblog.controller;

import com.lgblog.annotation.mySystemlog;
import com.lgblog.entity.dto.AddArticleDto;
import com.lgblog.entity.dto.ArticleListDto;
import com.lgblog.entity.dto.ArticleUpdateDto;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleAdminController {
    @Autowired
    private ArticleService articleService;

    /**
     * 写博文控制层
     * @param addArticleDto 前端传入参数
     * @return
     */
    @PostMapping
    @mySystemlog(businessName = "写博文接口")
    public ResponseResult addArticleAdmin(@RequestBody AddArticleDto addArticleDto)
    {
        return articleService.addArticleForAdmin(addArticleDto);
    }

    @GetMapping("/list")
    @mySystemlog(businessName = "文章列表查询")
    public ResponseResult ArticleList(Integer pageNum, Integer pageSize, ArticleListDto articleListDto)
    {
        return articleService.getArticleListForAdmin(pageNum,pageSize,articleListDto);
    }
    @GetMapping("/{id}")
    @mySystemlog(businessName = "后台修改文章界面文章详情")
    public ResponseResult getDetailForAdmin(@PathVariable Long id)
    {
        return articleService.getDetailForAdmin(id);
    }

    @PutMapping
    @mySystemlog(businessName = "修改文章接口")
    public ResponseResult updateArticleForAdmin(@RequestBody ArticleUpdateDto dto)
    {
        return articleService.updateArticleForAdmin(dto);
    }

    @DeleteMapping("/{id}")
    @mySystemlog(businessName = "逻辑删除接口")
    public ResponseResult deleteArticleForAdmin(@PathVariable Long id)
    {
        return articleService.deleteArticleForAdmin(id);
    }

}
