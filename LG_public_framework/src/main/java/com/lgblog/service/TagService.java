package com.lgblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lgblog.entity.Tag;
import com.lgblog.entity.dto.TagDto;
import com.lgblog.entity.dto.UpdateTagDto;
import com.lgblog.result.ResponseResult;

/**
 * @author gyd
 * @date 2023/8/2 0002 21:14
 */

public interface TagService extends IService<Tag> {

    ResponseResult getTagList(Integer pageSize, Integer pageNum, TagDto tagDto);

    ResponseResult addTag(TagDto tagDto);

    ResponseResult delTag(Long id);

    ResponseResult getTagInfo(Long id);

    ResponseResult updateTag(UpdateTagDto updateTagDto);

    ResponseResult allTagForAdmin();
}