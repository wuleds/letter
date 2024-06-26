package cn.wule.letter.file.controller;
//汉江师范学院 数计学院 吴乐创建于2024 4月 09 00:56

import cn.wule.letter.file.service.CosService;
import cn.wule.letter.util.DigestUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * 录音功能控制层*/
@RestController
@RequestMapping("/file")
@Slf4j
public class AudioController
{
    @Value("${file.audioPath}")
    private String AUDIO_PATH;
    @Resource
    private CosService cosService;

    @PostMapping("/audio/upload")
    public ResponseEntity<String> uploadAudio(@RequestParam("audio") MultipartFile audioFile) {
        if (audioFile.isEmpty()) {
            log.error("找不到录音文件");
            return ResponseEntity.badRequest().body("找不到录音文件");
        }
        try {
            byte[] bytes = audioFile.getBytes();

            //摘要
            String sha256Hex = DigestUtil.calculateSha256(bytes);
            String originalFilename = audioFile.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //用摘要为录音文件命名
            String newFilename = sha256Hex + extension;

            //设置存储路径
            Path path = Paths.get(AUDIO_PATH + newFilename);
            Files.createDirectories(path.getParent());
            Files.write(path, bytes);

            cosService.uploadFile(path.toFile(),newFilename,"audio");

            log.info("录音上传成功，文件名：{}", newFilename);
            return ResponseEntity.ok().body("上传录音成功");
        } catch (IOException e) {
            log.error("上传录音失败");
            return ResponseEntity.internalServerError().body("上传录音失败");
        }
    }
}