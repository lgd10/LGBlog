package com.lgblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lgblog.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author gyd
 * @date 2023/12/24 0002 21:13
 */

@Mapper
public interface TagDao extends BaseMapper<Tag> {

}