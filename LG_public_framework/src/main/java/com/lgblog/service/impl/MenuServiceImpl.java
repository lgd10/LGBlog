package com.lgblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lgblog.dao.MenuDao;
import com.lgblog.entity.Menu;
import com.lgblog.entity.SysUserRole;
import com.lgblog.entity.dto.MenuDto;
import com.lgblog.entity.vo.MenuVo;
import com.lgblog.entity.vo.adminVo.MenuDataBackVo;
import com.lgblog.result.AppHttpCodeEnum;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.MenuService;
import com.lgblog.service.MenuTreeForAdd;
import com.lgblog.service.SysUserRoleService;
import com.lgblog.util.copyBeanUtil.BeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lgblog.statement.statementVal;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author gyd
 * @since 2023-12-25 12:20:38
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuDao, Menu> implements MenuService {
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private MenuTreeForAdd add;
    @Override
    public List<String> getPermsForAdminUser(Long id) {
        Long roleId = null;
        LambdaQueryWrapper<SysUserRole> sysUserRoleLambdaQueryWrapper=new LambdaQueryWrapper<>();
        sysUserRoleLambdaQueryWrapper.eq(SysUserRole::getUserId,id);
        SysUserRole userRole=sysUserRoleService.getOne(sysUserRoleLambdaQueryWrapper);
        roleId=userRole.getRoleId();
        // System.out.println("用户类型"+roleId);
        //如果是超级管理员
        if(roleId.equals(1L))
        {
            // System.out.println("进入超级管理员判定");
            LambdaQueryWrapper<Menu> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(Menu::getStatus,statementVal.MENU_NORMAL);
            wrapper.in(Menu::getMenuType,statementVal.TYPE_MENU,statementVal.TYPE_BUTTON);
            List<String> perms=list(wrapper).stream().map(menu->menu.getPerms()).collect(Collectors.toList());
            // System.out.println(perms);
            return perms;
        }
        //否则根据id查询用户权限
        return getBaseMapper().getPermsByUserType(id);

    }

    @Override
    public List<Menu> getRoutersForAdminUser(Long id) {
        //检测用户是否是超级管理员
        List<Menu> menus=new ArrayList<>();
        Long roleId = null;
        LambdaQueryWrapper<SysUserRole> sysUserRoleLambdaQueryWrapper=new LambdaQueryWrapper<>();
        sysUserRoleLambdaQueryWrapper.eq(SysUserRole::getUserId,id);
        SysUserRole userRole=sysUserRoleService.getOne(sysUserRoleLambdaQueryWrapper);
        roleId=userRole.getRoleId();
        //先获取第一层级的控件
        if(roleId.equals(1L)){
            menus= getBaseMapper().getRoutersTrees();
        }
        else {
            menus= getBaseMapper().getRoutersByUserType(id);
        }
        //先查询最初的控件拥有哪些子控件
        List<Menu> menusTree=getNext(menus,0L);
        return menusTree;
    }

    @Override
    public ResponseResult getAllMenuForAdmin(MenuDto dto) {
        LambdaQueryWrapper<Menu> wrapper=new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(dto.getMenuName()),Menu::getMenuName,dto.getMenuName());
        wrapper.eq(StringUtils.hasText(dto.getStatus()),Menu::getStatus,dto.getStatus());
        wrapper.orderByAsc(Menu::getOrderNum);
        List<Menu> menuList = list(wrapper);
        List<MenuVo> menuVos = BeanCopy.copyListByBean(menuList, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    @Override
    public ResponseResult addMenuForAdmin(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateBackMenuData(Long id) {
        Menu menu = getById(id);
        MenuDataBackVo menuDataBackVo = BeanCopy.copyByBean(menu, MenuDataBackVo.class);
        return ResponseResult.okResult(menuDataBackVo);
    }

    /**
     * 提交修改
     * @param menu 前端传递的菜单数据
     * @return
     */
    @Override
    public ResponseResult updateMenuDataSub(Menu menu) {
        //判断修改的的名字是否跟自己的名字重合
        if (menu.getMenuName().equals(getById(menu.getParentId()).getMenuName()))
        {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"父级菜单名与该控件名重合");
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenuAdmin(Long menuId) {
        //判断是否含有子菜单
        LambdaQueryWrapper<Menu> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId,menuId);
        int count = count(wrapper);
        if (count>=1)
        {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"该级菜单下有子菜单存在,无法删除");
        }
        removeById(menuId);
        return ResponseResult.okResult();
    }


    private List<Menu> getNext(List<Menu> menus, long pid) {
        List<Menu> baseMenus = menus.stream()
                //先获取最基础的控件
                .filter(menu -> menu.getParentId().equals(pid))
                .map(menu -> menu.setChildren(getChildRouter(menu, menus)))
                .collect(Collectors.toList());
        return baseMenus;
    }

    private List<Menu> getChildRouter(Menu menu,List<Menu> menus) {
        List<Menu> leafMenus = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                //如果有三层菜单的话，也就是子菜单的子菜单，我们就用下面那行递归(自己调用自己)来处理
                .map(m -> m.setChildren(getChildRouter(m, menus)))
                .collect(Collectors.toList());
        return leafMenus;
    }
}
