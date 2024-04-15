package cn.wule.letter.file.service;
//汉江师范学院 数计学院 吴乐创建于2024 4月 16 02:55

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class CosService
{
    private static final Logger log = LoggerFactory.getLogger(CosService.class);
    @Resource
    private COSClient cosClient;
    @Value("${cos.bucketName}")
    private String bucketName;

    public void uploadFile(File file, String name,String path){
        String key = path + "/" + name;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);

        log.info("上传文件结果：{}", putObjectResult);
    }
}