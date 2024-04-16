package com.lgblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lgblog.entity.User;
import com.lgblog.entity.dto.UserAndRoleDto;
import com.lgblog.entity.dto.UserDto;
import com.lgblog.entity.dto.UserStatusDto;
import com.lgblog.result.ResponseResult;


/**
 * 用户表(User)表服务接口
 *
 * @author lg-gyd
 * @since 2023-11-30 14:12:15
 */
public interface UserService extends IService<User> {

    ResponseResult login(User user);

    ResponseResult logout();

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    /**
     * 后台用户列表实现
     * @param pageNum
     * @param pageSize
     * @param userName 提供查询的用户名
     * @param phonenumber 提供查询的用户号码
     * @param status 提供查询的用户状态
     * @return
     */
    ResponseResult UserList(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult addUser(UserDto userDto);

    ResponseResult deleteUser(Long id);

    ResponseResult UserDataBack(Long id);

    ResponseResult subUserData(UserAndRoleDto dto);

    ResponseResult changeStatus(UserStatusDto dto);
}
