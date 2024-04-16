package com.lgblog.entity.vo.adminVo;

import com.lgblog.entity.Role;
import com.lgblog.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDataBackVo {
    private List<Long> roleIds;
    private List<Role> roles;
    private UserVo user;
}
