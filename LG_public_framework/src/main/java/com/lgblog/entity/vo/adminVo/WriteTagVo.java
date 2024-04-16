package com.lgblog.entity.vo.adminVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 写博文专用标签响应类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WriteTagVo {
    private Long id;
    private String name;
}
