package cn.wule.letter.file.service;
//汉江师范学院 数计学院 吴乐创建于2024 4月 16 02:55

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class CosService
{
    @Resource
    private COSClient cosClient;
    @Value("${cos.bucketName}")
    private String bucketName;

    public void uploadFile(File file, String name,String path){
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, name, file);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);

        log.info("上传文件结果：{}", putObjectResult);
    }
}