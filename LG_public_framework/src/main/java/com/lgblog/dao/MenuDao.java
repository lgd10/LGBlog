package com.lgblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lgblog.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author gyd
 * @since 2023-12-25 12:20:37
 */
@Mapper
public interface MenuDao extends BaseMapper<Menu> {

    List<String> getPermsByUserType(Long userId);

    List<Menu> getRoutersTrees();

    List<Menu> getRoutersByUserType(Long userId);
}
