package com.lgblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lgblog.entity.Menu;
import com.lgblog.entity.vo.adminVo.MenuAddTreeVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuTreeDao extends BaseMapper<MenuAddTreeVo> {
    List<MenuAddTreeVo> getMenuTrees();
}
