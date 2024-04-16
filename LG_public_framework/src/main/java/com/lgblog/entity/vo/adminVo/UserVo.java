package com.lgblog.entity.vo.adminVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装到响应体内部类的部分数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    private Long id;
    //用户名
    private String userName;
    //昵称
    private String nickName;

    //账号状态（0正常 1停用）
    private String status;
    private String sex;
    private String phonenumber;

    private String email;

}
