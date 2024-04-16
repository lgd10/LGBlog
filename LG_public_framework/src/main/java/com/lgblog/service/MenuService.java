package com.lgblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lgblog.entity.Menu;
import com.lgblog.entity.dto.MenuDto;
import com.lgblog.result.ResponseResult;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-12-25 12:20:38
 */
public interface MenuService extends IService<Menu> {

    List<String> getPermsForAdminUser(Long id);

    List<Menu> getRoutersForAdminUser(Long id);

    ResponseResult getAllMenuForAdmin(MenuDto dto);

    ResponseResult addMenuForAdmin(Menu menu);

    ResponseResult updateBackMenuData(Long id);

    ResponseResult updateMenuDataSub(Menu menu);

    ResponseResult deleteMenuAdmin(Long menuId);

}
