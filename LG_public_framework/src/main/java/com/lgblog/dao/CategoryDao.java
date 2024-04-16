package com.lgblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lgblog.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分类表(Category)表数据库访问层
 *
 * @author gyd
 * @since 2023-11-24 15:40:06
 */
@Mapper
public interface CategoryDao extends BaseMapper<Category> {

}
