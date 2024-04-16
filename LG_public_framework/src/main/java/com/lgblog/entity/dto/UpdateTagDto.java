package com.lgblog.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前端编辑标签数据封装类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTagDto {
    private Long id;
    private String name;
    private String remark;
}
