package cn.wule.letter.file.controller;
//汉江师范学院 数计学院 吴乐创建于2024 4月 06 09:10

import cn.wule.letter.file.model.FileGetter;
import cn.wule.letter.util.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
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
public class ImageController
{
    @Value("${file.imagePath}")
    private String IMAGE_PATH;

    private static final Set<String> SUPPORTED_IMAGE_MIME_TYPES = new HashSet<>();

    static {
        SUPPORTED_IMAGE_MIME_TYPES.add("image/png");
        SUPPORTED_IMAGE_MIME_TYPES.add("image/jpeg");
        SUPPORTED_IMAGE_MIME_TYPES.add("image/jpg");
        SUPPORTED_IMAGE_MIME_TYPES.add("image/gif");
    }

    @GetMapping("/image/get")
    public ResponseEntity<Resource> getImage(@RequestBody FileGetter getter){
        Path imagePath = Paths.get("images", getter.getFileName());
        if (Files.exists(imagePath) && Files.isReadable(imagePath)) {
            // 动态获取图片的MIME类型
            String contentType = null;
            try {
                contentType = Files.probeContentType(imagePath);
            } catch (IOException e) {
                return ResponseEntity.badRequest().build();
            }
            if (contentType == null) {
                // 设置默认值
                contentType = "application/octet-stream";
            }

            Resource resource = null;
            try {
                resource = new InputStreamResource(Files.newInputStream(imagePath));
            } catch (IOException e) {
                return ResponseEntity.badRequest().build();
            }

            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imagePath.getFileName().toString() + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } else {
            // 如果找不到图片，返回404
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/image/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile image) {
        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body("找不到图片");
        }

        String contentType = image.getContentType();
        if (contentType == null || !SUPPORTED_IMAGE_MIME_TYPES.contains(contentType)) {
            return ResponseEntity.badRequest().body("不支持该格式");
        }

        try {
            //获取比特数组
            byte[] bytes = image.getBytes();

            //摘要
            String sha256Hex = DigestUtil.calculateSha256(bytes);

            //提取后缀
            String originalFilename = image.getOriginalFilename();
            String extension = null;
            if (originalFilename != null) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // 设置文件名
            String newFilename = sha256Hex + extension;

            //保存文件
            Path path = Paths.get(IMAGE_PATH + newFilename);
            log.info("文件保存路径: {}", path);
            Files.write(path, bytes);

            return ResponseEntity.ok("图片上传成功");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("图片上传失败");
        }
    }
}