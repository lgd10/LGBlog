package com.lgblog.entity.vo.adminVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleUpdateVo {
    private List<MenuAddTreeVo> menus;
    private List<Long> checkedKeys;
}
