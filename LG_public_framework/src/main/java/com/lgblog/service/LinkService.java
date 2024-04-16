package com.lgblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lgblog.entity.Link;
import com.lgblog.entity.vo.adminVo.LinkList;
import com.lgblog.result.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

/**
 * 友链(Link)表服务接口
 *
 * @author gyd
 * @since 2023-11-29 17:09:11
 */

public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult getList(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addLink(LinkList dto);

    ResponseResult LinkDataBack(Long id);

    ResponseResult subLinkData(LinkList link);

    ResponseResult deleteLink(Long id);
}
