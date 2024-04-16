package com.lgblog.service;

import com.lgblog.result.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface uploadService {
    ResponseResult uploadHeader(MultipartFile img);
}
