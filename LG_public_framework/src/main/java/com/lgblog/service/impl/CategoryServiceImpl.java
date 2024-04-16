package com.lgblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lgblog.dao.CategoryDao;
import com.lgblog.entity.Article;
import com.lgblog.entity.dto.CategoryStatusDto;
import com.lgblog.entity.vo.PageVo;
import com.lgblog.entity.vo.adminVo.AdminCategoryVo;
import com.lgblog.entity.vo.adminVo.CategoryListVo;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.ArticleService;
import com.lgblog.service.CategoryService;
import com.lgblog.statement.statementVal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lgblog.entity.Category;
import com.lgblog.entity.vo.categoryVo;
import com.lgblog.util.copyBeanUtil.BeanCopy;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 分类表(Category)表服务实现类
 *
 * @author gyd
 * @since 2023-11-24 15:40:06
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;
    /**
     * 合格的要求：没有被删除，不能是草稿
     * @return
     */
    @Override
    public ResponseResult getCategoryList() {
        //首先查询所有合格的文章
        LambdaQueryWrapper<Article> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, statementVal.ARTICLE_STATUS_NORMAL);
        List<Article> list = articleService.list(wrapper);
        //将查询出来的文章去重
        Set<Long> idSet = list.stream()
                .map(((article) -> article.getCategoryId()))
                .collect(Collectors.toSet());
        //查询分类表
        List<Category> categoryList = listByIds(idSet)
                .stream()
                .filter((category) -> category.getStatus().equals(statementVal.ARTICLE_CATEGORY_STATUS))
                .collect(Collectors.toList());

        //vo封装
        categoryVo vo = new categoryVo();
        List<categoryVo> categoryVos = BeanCopy.copyListByBean(categoryList, categoryVo.class);
        System.out.println(categoryVos);
        return ResponseResult.okResult(categoryVos);
    }

    /**
     * 给后台系统的接口，查询所有分类
     * @return
     */
    @Override
    public ResponseResult AllCategoryForAdmin() {
        List<Category> categoryList = list();
        List<AdminCategoryVo> adminCategoryVos = BeanCopy.copyListByBean(categoryList, AdminCategoryVo.class);
        return ResponseResult.okResult(adminCategoryVos);
    }

    /**
     * 后台管理界面获取分类集合
     * @param pageNum 页码
     * @param pageSize 单页大小
     * @param name 分类名
     * @param status 分类状态
     * @return
     */
    @Override
    public ResponseResult getList(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Category> wrapper= new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(name),Category::getName,name);
        wrapper.eq(StringUtils.hasText(status),Category::getStatus,status);
        List<Category> categories = list(wrapper);
        List<CategoryListVo> categoryListVos = BeanCopy.copyListByBean(categories, CategoryListVo.class);
        Page<Category> categoryPage=new Page<>(pageNum,pageSize);
        page(categoryPage,wrapper);
        PageVo pageVo=new PageVo(categoryListVos,categoryPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addCategory(CategoryListVo category) {
        Category category1 = BeanCopy.copyByBean(category, Category.class);
        save(category1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeStatus(CategoryStatusDto dto) {
        LambdaUpdateWrapper<Category> wrapper=new LambdaUpdateWrapper<>();
        wrapper.eq(Category::getId,dto.getCategoryId());
        wrapper.set(Category::getStatus,dto.getStatus());
        update(wrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult categoryDataBack(Long id) {
        LambdaQueryWrapper<Category> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Category::getId,id);
        Category category = getOne(wrapper);
        CategoryListVo categoryListVo = BeanCopy.copyByBean(category, CategoryListVo.class);
        return ResponseResult.okResult(categoryListVo);
    }

    @Override
    public ResponseResult subCategoryData(CategoryListVo vo) {
        Category category = BeanCopy.copyByBean(vo, Category.class);
        updateById(category);
        return ResponseResult.okResult();
    }
}
