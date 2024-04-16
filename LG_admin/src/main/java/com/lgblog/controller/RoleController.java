package com.lgblog.controller;

import com.lgblog.entity.dto.AddRoleDto;
import com.lgblog.entity.dto.RoleDto;
import com.lgblog.entity.dto.RoleUpdateDto;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.lgblog.annotation.mySystemlog;

@RestController
@RequestMapping("system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    /**
     * 角色列表查询
     * @param pageNum 页码
     * @param pageSize 单页大小
     * @param roleName 角色名
     * @param status 角色状态
     * @return
     */
    @GetMapping("/list")
    @mySystemlog(businessName = "角色列表查询")
    public ResponseResult getRoleList(Integer pageNum,Integer pageSize,String roleName,String status)
    {
        return roleService.getRoleList(pageNum,pageSize,roleName,status);
    }

    @PutMapping("/changeStatus")
    @mySystemlog(businessName = "修改角色状态")
    public ResponseResult changeStatus(@RequestBody RoleDto roleDto)
    {
        return roleService.changeStatus(roleDto);
    }

    @PostMapping
    @mySystemlog(businessName = "添加新角色")
    public ResponseResult addNewRole(@RequestBody AddRoleDto dto)
    {
        return roleService.addNewRole(dto);
    }
    @GetMapping("/{roleId}")
    @mySystemlog(businessName = "角色信息回显")
    public ResponseResult RoleDataBack(@PathVariable Long roleId)
    {
        return roleService.RoleDataBack(roleId);
    }

    @PutMapping
    @mySystemlog(businessName = "更新数据提交")
    public ResponseResult RoleUpdate(@RequestBody RoleUpdateDto dto)
    {
        return roleService.RoleUpdate(dto);
    }

    @DeleteMapping("/{id}")
    @mySystemlog(businessName = "删除角色数据")
    public ResponseResult deleteRole(@PathVariable Long id)
    {
        return roleService.deleteRole(id);
    }

    @GetMapping("/listAllRole")
    @mySystemlog(businessName = "查询所有的角色名")
    public ResponseResult getAllRole()
    {
        return roleService.getAllRole();
    }

}
