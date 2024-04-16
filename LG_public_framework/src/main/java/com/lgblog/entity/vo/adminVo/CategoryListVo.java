package com.lgblog.entity.vo.adminVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 *后台查询分类列表以及请求体数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryListVo {
    private Long id;

    //分类名
    private String name;
    //父分类id，如果没有父分类为-1
    //描述
    private String description;
    //状态0:正常,1禁用
    private String status;

    private Integer delFlag;
}
