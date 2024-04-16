package com.lgblog.controller;

import com.lgblog.entity.dto.UpdateTagDto;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.TagService;
import com.lgblog.entity.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class tagController {
    @Autowired
    private TagService tagService;

    /**
     * 标签测试接口
     * @return
     */
    @GetMapping("/content/tag/listAllTag")
    public ResponseResult allTag()
    {
        return tagService.allTagForAdmin();
    }

    /**
     * 标签列表控制层接口
     * @param pageSize 每页数据量
     * @param pageNum 页码
     * @param tagDto 前端传递的搜索关键字用作模糊查询
     * @return
     */
    @GetMapping("/content/tag/list")
    public ResponseResult tagList(Integer pageSize,Integer pageNum,TagDto tagDto)
    {
        return tagService.getTagList(pageSize,pageNum,tagDto);
    }

    /**
     * 后台标签添加接口控制层
     * @param tagDto 添加数据封装类
     * @return
     */
    @PostMapping("/content/tag")
    public ResponseResult addTag(@RequestBody TagDto tagDto)
    {
        return tagService.addTag(tagDto);
    }

    /**
     * 逻辑删除接口
     * @param id 逻辑删除的标签的id
     * @return
     */
    @DeleteMapping("/content/tag/{id}")
    public ResponseResult delTag(@PathVariable Long id)
    {
        return tagService.delTag(id);
    }

    @GetMapping("/content/tag/{id}")
    public ResponseResult getTagInfo(@PathVariable Long id)
    {
        return tagService.getTagInfo(id);
    }

    @PutMapping("/content/tag")
    public ResponseResult updateTag(@RequestBody UpdateTagDto updateTagDto)
    {
        return tagService.updateTag(updateTagDto);
    }



}
