package com.lgblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lgblog.dao.MenuTreeDao;
import com.lgblog.entity.RoleMenu;
import com.lgblog.entity.vo.adminVo.MenuAddTreeVo;
import com.lgblog.entity.vo.adminVo.RoleUpdateVo;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.MenuTreeForAdd;
import com.lgblog.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MenuTreeForAddImpl extends ServiceImpl<MenuTreeDao, MenuAddTreeVo> implements MenuTreeForAdd{
    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    public ResponseResult getMenuTree() {
        List<MenuAddTreeVo> menus = baseMapper.getMenuTrees();
        List<MenuAddTreeVo> menusTree=getNext(menus,0L);
        return ResponseResult.okResult(menusTree);
    }

    @Override
    public ResponseResult roleMenuTreeSelect(Long id) {
        List<MenuAddTreeVo> menus = baseMapper.getMenuTrees();
        List<MenuAddTreeVo> menusTree=getNext(menus,0L);//先获取菜单列表树
        RoleUpdateVo roleUpdateVo=new RoleUpdateVo();
        roleUpdateVo.setMenus(menusTree);//将菜单列表填充到返回给前端的vo数据中
        LambdaUpdateWrapper<RoleMenu> wrapper=new LambdaUpdateWrapper<>();
        wrapper.eq(RoleMenu::getRoleId,id);
        //获取与该角色有关的菜单权限，使得在前端能够显示该角色拥有的权限以方便修改
        List<Long> checkedKeys = roleMenuService.list(wrapper)
                .stream()
                .map(roleMenu -> roleMenu.getMenuId())
                .collect(Collectors.toList());
        roleUpdateVo.setCheckedKeys(checkedKeys);
        return ResponseResult.okResult(roleUpdateVo);
    }


    //下面两个方法采用的是之前获取子列表的方法
    private List<MenuAddTreeVo> getNext(List<MenuAddTreeVo> menus, long pid) {
        List<MenuAddTreeVo> baseMenus = menus.stream()
                .filter(menu->menu.getParentId().equals(pid))
                .map(menuAddTreeVo -> menuAddTreeVo.setChildren(getChildMenu(menuAddTreeVo,menus)))
                .collect(Collectors.toList());
        return baseMenus;
    }

    private List<MenuAddTreeVo> getChildMenu(MenuAddTreeVo menu,List<MenuAddTreeVo> menus) {
        List<MenuAddTreeVo> leafMenus = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                //如果有三层菜单的话，也就是子菜单的子菜单，我们就用下面那行递归(自己调用自己)来处理
                .map(m -> m.setChildren(getChildMenu(m, menus)))
                .collect(Collectors.toList());
        return leafMenus;
    }
}
