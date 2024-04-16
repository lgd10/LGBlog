package com.lgblog.entity.vo.adminVo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 后台添加用户的时候需要回显一部分菜单列表，并且要按照层级关系排列
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_menu")
@Accessors(chain = true)
public class MenuAddTreeVo {
    private Long id;
    @TableField(value = "menu_name")
    private String label;
    private Long parentId;
    private List<MenuAddTreeVo> children;
}
