package com.lgblog.service.impl;

import com.google.gson.Gson;
import com.lgblog.MyException.SystemException;
import com.lgblog.result.ResponseResult;
import com.lgblog.service.uploadService;
import com.lgblog.util.pathUtil.PathUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.lgblog.result.AppHttpCodeEnum;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
@Data
@ConfigurationProperties(prefix = "oss")
@Service
public class uploadHeaderImpl implements uploadService {
    private String accessKey;
    private String secretKey;
    private String bucket;

    @Override
    public ResponseResult uploadHeader(MultipartFile img) {
        String originalFilename = img.getOriginalFilename();

        //对原始文件名进行判断大小。只能上传png或jpg文件
        if(!originalFilename.endsWith(".png")&&!originalFilename.endsWith(".jpg"))
        {
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        String fileName = PathUtils.generateFilePath(originalFilename);
        String url = saveHeader(img, fileName);
        return ResponseResult.okResult(url);
    }
    public String saveHeader(MultipartFile img,String fileName)
    {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());//选择区域，之前选择的是华南
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        try {
            InputStream inputStream= img.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println("上传成功,生成的key是:"+putRet.key);
                System.out.println("上传成功,生成的hash是:"+putRet.hash);
                return "http://s5j8849ut.hn-bkt.clouddn.com/"+key;
            } catch (QiniuException ex) {
                ex.printStackTrace();
                if (ex.response != null) {
                    System.err.println(ex.response);

                    try {
                        String body = ex.response.toString();
                        System.err.println(body);
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "出现错误了";

    }
}
