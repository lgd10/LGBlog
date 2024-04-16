package com.lgblog.controller;

import com.lgblog.result.ResponseResult;
import com.lgblog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/getCategoryList")
    public ResponseResult getCategoryList(){
        System.out.println("=============================");
        return categoryService.getCategoryList();
    }
}
