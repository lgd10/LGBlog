package com.lgblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lgblog.dao.LinkDao;
import com.lgblog.entity.vo.PageVo;
import com.lgblog.entity.vo.adminVo.LinkList;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.LinkService;
import com.lgblog.util.copyBeanUtil.BeanCopy;
import com.lgblog.entity.vo.LinKVo;
import org.springframework.stereotype.Service;
import com.lgblog.entity.Link;
import com.lgblog.statement.statementVal;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author gyd
 * @since 2023-11-29 17:09:11
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkDao, Link> implements LinkService {
    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Link::getStatus,statementVal.FLINK_STATUS_NORMAL);
        List<Link> linkList = list(wrapper);
        List<LinKVo> linKVos = BeanCopy.copyListByBean(linkList, LinKVo.class);
        return ResponseResult.okResult(linKVos);
    }

    @Override
    public ResponseResult getList(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Link> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(status),Link::getStatus,status )
               .like(StringUtils.hasText(name),Link::getName,name);
        Page<Link> linkPage=new Page<>(pageNum,pageSize);
        List<Link> links = list(wrapper);
        List<LinkList> linkLists = BeanCopy.copyListByBean(links, LinkList.class);
        page(linkPage,wrapper);
        PageVo pageVo=new PageVo(linkLists, linkPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addLink(LinkList dto) {
        Link link = BeanCopy.copyByBean(dto, Link.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult LinkDataBack(Long id) {
        LambdaQueryWrapper<Link> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Link::getId,id);
        Link link = getOne(wrapper);
        LinkList linkList = BeanCopy.copyByBean(link, LinkList.class);
        return ResponseResult.okResult(linkList);
    }

    @Override
    public ResponseResult subLinkData(LinkList link) {
        Link copyLink = BeanCopy.copyByBean(link, Link.class);
        updateById(copyLink);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLink(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }
}
