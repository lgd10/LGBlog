package com.lgblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lgblog.dao.RoleDao;
import com.lgblog.entity.Role;
import com.lgblog.entity.RoleMenu;
import com.lgblog.entity.SysUserRole;
import com.lgblog.entity.dto.AddRoleDto;
import com.lgblog.entity.dto.RoleDto;
import com.lgblog.entity.dto.RoleUpdateDto;
import com.lgblog.entity.vo.PageVo;
import com.lgblog.entity.vo.adminVo.RoleDataBackForUserVo;
import com.lgblog.entity.vo.adminVo.RoleDataBackVo;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.RoleMenuService;
import com.lgblog.service.RoleService;
import com.lgblog.service.SysUserRoleService;
import com.lgblog.statement.statementVal;
import com.lgblog.util.copyBeanUtil.BeanCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author gyd
 * @since 2023-12-25 12:31:41
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    public List<String> getRoleKeyForAdminUser(Long id) {
        //判断是否是管理员，如果是，就返回集合中只需要有admin
        //该方式是通过判断roleId来判定是否是管理员
        Long roleId = null;
        LambdaQueryWrapper<SysUserRole> sysUserRoleLambdaQueryWrapper=new LambdaQueryWrapper<>();
        sysUserRoleLambdaQueryWrapper.eq(SysUserRole::getUserId,id);
        SysUserRole userRole=sysUserRoleService.getOne(sysUserRoleLambdaQueryWrapper);
        roleId=userRole.getRoleId();
        if(roleId.equals(1L)){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        return getBaseMapper().getRoleKeyByUserType(id);
    }

    @Override
    public ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> wrapper=new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        wrapper.eq(StringUtils.hasText(status),Role::getStatus,status);
        Page<Role> rolePage=new Page<>(pageNum,pageSize);
        page(rolePage,wrapper);
        PageVo pageVo=new PageVo(rolePage.getRecords(),rolePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeStatus(RoleDto roleDto) {
        LambdaUpdateWrapper<Role> wrapper=new LambdaUpdateWrapper<>();
        wrapper.eq(Role::getId,roleDto.getRoleId());
        wrapper.set(Role::getStatus,roleDto.getStatus());
        update(wrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addNewRole(AddRoleDto dto) {
        Role role = BeanCopy.copyByBean(dto, Role.class);
        save(role);
        Long roleId = role.getId();
        List<Long> menuIds = dto.getMenuIds();
        List<RoleMenu> roleMenus = menuIds
                .stream()
                .map(menuId -> new RoleMenu(roleId, menuId))
                .collect(Collectors.toList());
        /**
         * List<RoleMenu> roleMenus = menuIds.stream().map(new Function<Long, RoleMenu>() {
         *             @Override
         *             public RoleMenu apply(Long menuId) {
         *                 return new RoleMenu(roleId, menuId);
         *             }
         *         }).collect(Collectors.toList());
         */
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult RoleDataBack(Long roleId) {
        Role role = getById(roleId);
        RoleDataBackVo roleDataBackVo = BeanCopy.copyByBean(role, RoleDataBackVo.class);
        return ResponseResult.okResult(roleDataBackVo);
    }

    @Override
    public ResponseResult RoleUpdate(RoleUpdateDto dto) {
        Role role = BeanCopy.copyByBean(dto, Role.class);
        updateById(role);
        //由于角色和他控制的菜单是一对多的关系，所以要先从role_menu表中删除所有跟该角色有关的数据，再重新写入
        LambdaUpdateWrapper<RoleMenu> wrapper=new LambdaUpdateWrapper<>();
        wrapper.eq(RoleMenu::getRoleId,dto.getId());
        roleMenuService.remove(wrapper);
        //写入
        List<RoleMenu> roleMenus = dto.getMenuIds()
                .stream()
                .map(menuId -> new RoleMenu(dto.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRole(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    /***
     * 添加用户需要用到的获取所有的角色信息
     * @return
     */
    @Override
    public ResponseResult getAllRole() {
        List<Role> roles = list();
        List<RoleDataBackForUserVo> roleDataBackForUserVos = BeanCopy.copyListByBean(roles, RoleDataBackForUserVo.class);
        return ResponseResult.okResult(roleDataBackForUserVos);
    }
}
