package com.lgblog.entity.vo.adminVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 由于前后端数据交互的封装类实在太多，用该类代替所有响应体跟请求体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkList {
    private Long id;


    private String name;

    private String logo;

    private String description;
    //网站地址
    private String address;
    private String status;
}
