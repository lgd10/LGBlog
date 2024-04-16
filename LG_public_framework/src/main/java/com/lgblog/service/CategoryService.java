package com.lgblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lgblog.entity.Category;
import com.lgblog.entity.dto.CategoryStatusDto;
import com.lgblog.entity.vo.adminVo.CategoryListVo;
import com.lgblog.result.ResponseResult;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-11-24 15:40:06
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult AllCategoryForAdmin();

    ResponseResult getList(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addCategory(CategoryListVo category);

    ResponseResult changeStatus(CategoryStatusDto dto);

    ResponseResult categoryDataBack(Long id);

    ResponseResult subCategoryData(CategoryListVo vo);
}
