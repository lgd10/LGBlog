package com.lgblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lgblog.entity.vo.adminVo.MenuAddTreeVo;
import com.lgblog.result.ResponseResult;

public interface MenuTreeForAdd extends IService<MenuAddTreeVo> {
    ResponseResult getMenuTree();

    ResponseResult roleMenuTreeSelect(Long id);
}
