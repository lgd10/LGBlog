package com.lgblog.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接收前端传递来的数据实现查询标签名或者标签备注查询
 * 以定位到标签
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {
    private String name;
    private String remark;
}
