package cn.wule.letter.file.controller;
//汉江师范学院 数计学院 吴乐创建于2024 4月 08 20:48

import cn.wule.letter.util.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

/**
 * 文件存储控制层
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController
{
    @Value("${file.filePath}")
    private String FILE_PATH;

    @PostMapping("/file/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("找不到文件");
        }

        try {
            byte[] fileBytes = file.getBytes();
            String sha256Hex = DigestUtil.calculateSha256(fileBytes);
            String originalFilename = file.getOriginalFilename();
            String extension = null;
            if (originalFilename != null) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = sha256Hex + extension;
            Path path = Paths.get(FILE_PATH + newFilename);
            Files.createDirectories(path.getParent()); // 确保目录存在
            Files.write(path, fileBytes);

            log.info("文件上传成功，文件名：{}", newFilename);

            return ResponseEntity.ok("文件上传成功");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("文件上传失败");
        }
    }
}