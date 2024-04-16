package com.lgblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lgblog.entity.User;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-11-30 14:12:15
 */
@Mapper
public interface UserDao extends BaseMapper<User> {

}
