package com.lgblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lgblog.dao.TagDao;
import com.lgblog.entity.Tag;
import com.lgblog.entity.dto.TagDto;
import com.lgblog.entity.dto.UpdateTagDto;
import com.lgblog.entity.vo.PageVo;
import com.lgblog.entity.vo.adminVo.TagVo;
import com.lgblog.entity.vo.adminVo.WriteTagVo;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.TagService;
import com.lgblog.util.copyBeanUtil.BeanCopy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.lgblog.statement.statementVal;

import java.util.List;

/**
 * @author gyd
 * @date 2023/12/26 0002 21:15
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagDao, Tag> implements TagService {
    @Override
    public ResponseResult getTagList(Integer pageSize, Integer pageNum, TagDto tagDto) {
        LambdaQueryWrapper<Tag> wrapper=new LambdaQueryWrapper<>();
        //存在name字段时
        if(StringUtils.hasText(tagDto.getName()))
        {
            //实现模糊查询
            wrapper.like(Tag::getName,tagDto.getName());
        }
        //存在备注字段时
        if(StringUtils.hasText(tagDto.getRemark()))
        {
            wrapper.like(Tag::getRemark,tagDto.getRemark());
        }
        Page<Tag> tagPage=new Page<>();
        tagPage.setCurrent(pageNum);
        tagPage.setSize(pageSize);
        page(tagPage,wrapper);
        PageVo pageVo=new PageVo(tagPage.getRecords(), tagPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(TagDto tagDto) {
        Tag tag = BeanCopy.copyByBean(tagDto, Tag.class);
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult delTag(Long id) {
        LambdaUpdateWrapper<Tag> wrapper=new LambdaUpdateWrapper<>();
        wrapper.eq(Tag::getId,id).set(Tag::getDelFlag,statementVal.TAG_DELETE);
        //应该将该对象作为 update 方法的第一个参数传递。这个对象中包含了你想要更新的字段和它们的新值。
        update(null,wrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTagInfo(Long id) {
        LambdaQueryWrapper<Tag> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getId,id);
        Tag tag = getOne(wrapper);
        TagVo tagVo = BeanCopy.copyByBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    /**
     * 标签更新
     * @param updateTagDto 前端数据封装类
     * @return
     */
    @Override
    public ResponseResult updateTag(UpdateTagDto updateTagDto) {
        Tag tag = BeanCopy.copyByBean(updateTagDto, Tag.class);
        updateById(tag);
        return ResponseResult.okResult();
    }

    /**
     * 写博文所需标签接口(不需要分页操作)
     * @return
     */
    @Override
    public ResponseResult allTagForAdmin() {
        List<Tag> tagList = list();
        List<WriteTagVo> writeTagVos = BeanCopy.copyListByBean(tagList, WriteTagVo.class);
        return ResponseResult.okResult(writeTagVos);
    }
}