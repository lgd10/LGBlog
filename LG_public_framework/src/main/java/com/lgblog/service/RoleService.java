package com.lgblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lgblog.entity.Role;
import com.lgblog.entity.dto.AddRoleDto;
import com.lgblog.entity.dto.RoleDto;
import com.lgblog.entity.dto.RoleUpdateDto;
import com.lgblog.result.ResponseResult;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-12-25 12:31:41
 */
public interface RoleService extends IService<Role> {

    List<String> getRoleKeyForAdminUser(Long id);

    ResponseResult getRoleList(Integer pageNum, Integer pageSize, String roleName, String status);

    ResponseResult changeStatus(RoleDto roleDto);

    ResponseResult addNewRole(AddRoleDto dto);

    ResponseResult RoleDataBack(Long roleId);

    ResponseResult RoleUpdate(RoleUpdateDto dto);

    ResponseResult deleteRole(Long id);

    /***
     * 添加用户需要用到的获取所有的角色信息
     * @return
     */
    ResponseResult getAllRole();
}
