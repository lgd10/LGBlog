package com.lgblog.controller;

import com.lgblog.result.ResponseResult;
import com.lgblog.service.uploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class uploadHeaderController {
    @Autowired
    private uploadService service;
    @PostMapping("/upload")
    public ResponseResult uploadHeader(MultipartFile img)
    {
        return service.uploadHeader(img);
    }
}
