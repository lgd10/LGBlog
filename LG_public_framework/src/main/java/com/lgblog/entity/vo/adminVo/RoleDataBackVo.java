package com.lgblog.entity.vo.adminVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 修改角色数据回显
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDataBackVo {
    //角色表id
    private Long id;
    //角色名称
    private String roleName;
    //角色权限字符串
    private String roleKey;
    //显示顺序
    private Integer roleSort;
    //角色状态（0正常 1停用）
    private String status;
    //备注
    private String remark;
}
