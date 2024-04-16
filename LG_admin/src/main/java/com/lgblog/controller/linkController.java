package com.lgblog.controller;

import com.lgblog.entity.vo.adminVo.LinkList;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.lgblog.annotation.mySystemlog;

@RestController
@RequestMapping("/content/link")
public class linkController {
    @Autowired
    private LinkService linkService;
    @GetMapping("/list")
    @mySystemlog(businessName = "后台友链列表分页查询")
    public ResponseResult getList(Integer pageNum,Integer pageSize,String name,String status)
    {
        return linkService.getList(pageNum,pageSize,name,status);
    }
    @PostMapping
    @mySystemlog(businessName = "新增友链接口")
    public ResponseResult addLink(@RequestBody LinkList dto)
    {
        return linkService.addLink(dto);
    }
    @GetMapping("/{id}")
    @mySystemlog(businessName = "修改数据回显")
    public ResponseResult LinkDataBack(@PathVariable Long id)
    {
        return linkService.LinkDataBack(id);
    }
    @PutMapping
    @mySystemlog(businessName = "修改数据提交")
    public ResponseResult subLinkData(@RequestBody LinkList link)
    {
        return linkService.subLinkData(link);
    }

    @DeleteMapping("/{id}")
    @mySystemlog(businessName = "逻辑删除友链实现")
    public ResponseResult deleteLink(@PathVariable Long id)
    {
        return linkService.deleteLink(id);
    }

}
