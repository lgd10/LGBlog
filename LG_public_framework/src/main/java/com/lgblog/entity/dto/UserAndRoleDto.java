package com.lgblog.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAndRoleDto {
    private Long id;
    //用户名
    private String userName;
    //昵称
    private String nickName;

    //账号状态（0正常 1停用）
    private String status;
    //邮箱
    private String email;
    //手机号
    private String sex;

    //电话号码
    private String phonenumber;
    //权限id
    List<Long> roleIds;
}
