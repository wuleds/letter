package cn.wule.letter.file.controller;
//汉江师范学院 数计学院 吴乐创建于2024 4月 06 13:11

import cn.wule.letter.util.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/file")
@Slf4j
public class VideoController
{
    @Value("${file.videoPath}")
    private String VIDEO_PATH;

    private static final Set<String> SUPPORTED_VIDEO_MIME_TYPES = new HashSet<>();

    static {
        SUPPORTED_VIDEO_MIME_TYPES.add("video/mp4");
        SUPPORTED_VIDEO_MIME_TYPES.add("video/avi");
    }

    @PostMapping("/video/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile video) {
        if (video.isEmpty()) {
            return ResponseEntity.badRequest().body("找不到视频");
        }

        String contentType = video.getContentType();
        if (contentType == null || !SUPPORTED_VIDEO_MIME_TYPES.contains(contentType)) {
            return ResponseEntity.badRequest().body("不支持该格式");
        }

        try {
            byte[] videoBytes = video.getBytes();
            String sha256Hex = DigestUtil.calculateSha256(videoBytes);
            String originalFilename = video.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = sha256Hex + extension;
            Path path = Paths.get(VIDEO_PATH + newFilename);
            Files.write(path, videoBytes);

            log.info("视频上传成功，文件名：{}", newFilename);

            return ResponseEntity.ok("上传视频成功 ");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("上传视频失败");
        }
    }
}