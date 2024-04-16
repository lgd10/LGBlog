package com.lgblog.controller;

import com.lgblog.entity.Menu;
import com.lgblog.entity.dto.MenuDto;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.MenuService;
import com.lgblog.service.MenuTreeForAdd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.lgblog.annotation.mySystemlog;

@RestController
@RequestMapping("system/menu")
public class menuController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuTreeForAdd add;
    @GetMapping("/list")
    @mySystemlog(businessName = "后台菜单列表功能")
    public ResponseResult getAllMenuForAdmin(MenuDto dto)
    {
        return menuService.getAllMenuForAdmin(dto);
    }
    @PostMapping
    @mySystemlog(businessName = "后台添加菜单")
    public ResponseResult addMenuForAdmin(@RequestBody Menu menu)
    {
        return menuService.addMenuForAdmin(menu);
    }
    @GetMapping("/{menuId}")
    @mySystemlog(businessName = "后台修改菜单回显数据")
    public ResponseResult updateBackMenuData(@PathVariable Long menuId)
    {
        return menuService.updateBackMenuData(menuId);
    }
    @PutMapping
    @mySystemlog(businessName = "修改数据提交")
    public ResponseResult updateMenuDataSub(@RequestBody Menu menu)
    {
        return menuService.updateMenuDataSub(menu);
    }

    @DeleteMapping("/{menuId}")
    @mySystemlog(businessName = "菜单删除(逻辑删除)")
    public ResponseResult deleteMenuAdmin(@PathVariable Long menuId)
    {
        return menuService.deleteMenuAdmin(menuId);
    }

    @GetMapping("/treeselect")
    @mySystemlog(businessName = "获取菜单树")
    public ResponseResult getMenuTree()
    {
        return add.getMenuTree();
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    @mySystemlog(businessName = "菜单数据回显(注意：超级管理员的数据是没有回显的)")
    public ResponseResult roleMenuTreeSelect(@PathVariable Long id)
    {
        return add.roleMenuTreeSelect(id);
    }
}
