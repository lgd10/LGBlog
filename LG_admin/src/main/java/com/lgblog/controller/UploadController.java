package com.lgblog.controller;

import com.lgblog.result.ResponseResult;
import com.lgblog.service.uploadService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class UploadController {
    @Autowired
    private uploadService service;
    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img){
        return service.uploadHeader(img);
    }
}
