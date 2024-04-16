package com.lgblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lgblog.dao.ArticleVoDao;
import com.lgblog.entity.vo.ArticleVo;
import com.lgblog.service.ArticleVoService;
import org.springframework.stereotype.Service;

@Service
public class ArticleVoServiceImpl extends ServiceImpl<ArticleVoDao,ArticleVo> implements ArticleVoService {

}
