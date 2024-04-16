package com.lgblog.controller;

import com.lgblog.entity.dto.UserAndRoleDto;
import com.lgblog.entity.dto.UserDto;
import com.lgblog.entity.dto.UserStatusDto;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.RoleService;
import com.lgblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.lgblog.annotation.mySystemlog;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/list")
    @mySystemlog(businessName = "用户列表实现")
    public ResponseResult UserList(Integer pageNum,Integer pageSize,String userName,String phonenumber,String status)
    {
        return userService.UserList(pageNum,pageSize,userName,phonenumber,status);
    }

    @PostMapping
    @mySystemlog(businessName = "添加用户")
    public ResponseResult addUser(@RequestBody UserDto userDto)
    {
        return userService.addUser(userDto);
    }
    @DeleteMapping("/{id}")
    @mySystemlog(businessName = "删除用户(逻辑删除)")
    public ResponseResult deleteUser(@PathVariable Long id)
    {
        return userService.deleteUser(id);
    }
    @GetMapping("/{id}")
    @mySystemlog(businessName = "修改用户时的用户数据回显")
    public ResponseResult UserDataBack(@PathVariable Long id)
    {
        return userService.UserDataBack(id);
    }

    @PutMapping
    @mySystemlog(businessName = "更新用户提交")
    public ResponseResult subUserData(@RequestBody UserAndRoleDto dto)
    {
        return userService.subUserData(dto);
    }

    @PutMapping("/changeStatus")
    @mySystemlog(businessName = "用户状态修改")
    public ResponseResult changeStatus(@RequestBody UserStatusDto dto)
    {
        return userService.changeStatus(dto);
    }
}
