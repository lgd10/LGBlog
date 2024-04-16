package com.lgblog.entity.vo.adminVo;

import com.lgblog.entity.vo.UserInfoVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserVo {
    private List<String> permissions;
    private List<String> roles;
    private UserInfoVo user;
}
