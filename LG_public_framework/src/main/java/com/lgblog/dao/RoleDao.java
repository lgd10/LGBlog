package com.lgblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lgblog.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author gyd
 * @since 2023-12-25 12:31:41
 */
public interface RoleDao extends BaseMapper<Role> {
    List<String> getRoleKeyByUserType(Long userId);
}
