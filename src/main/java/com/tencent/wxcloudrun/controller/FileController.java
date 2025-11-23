package com.tencent.wxcloudrun.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件访问控制器
 */
@RestController
@RequestMapping("/api/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Value("${file.upload.dir:uploads}")
    private String uploadDir;

    /**
     * 获取文件
     * 支持路径如: /api/files/avatar/1/filename.jpg
     */
    @GetMapping("/**")
    public ResponseEntity<Resource> getFile(HttpServletRequest request) {
        try {
            // 获取请求路径，去掉 /api/files 前缀
            String requestUri = request.getRequestURI();
            String requestPath = requestUri.replaceFirst("^/api/files", "");
            if (requestPath.startsWith("/")) {
                requestPath = requestPath.substring(1);
            }
            
            // 构建文件路径
            Path filePath = Paths.get(uploadDir, requestPath);
            File file = filePath.toFile();

            if (!file.exists() || !file.isFile()) {
                logger.warn("文件不存在: {}", filePath);
                return ResponseEntity.notFound().build();
            }

            // 检查文件是否在上传目录内（安全验证）
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path resolvedPath = filePath.toAbsolutePath().normalize();
            if (!resolvedPath.startsWith(uploadPath)) {
                logger.warn("非法文件访问: {}", resolvedPath);
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(file);

            // 根据文件扩展名设置Content-Type
            String contentType = getContentType(file.getName());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            logger.error("获取文件失败", e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 根据文件扩展名获取Content-Type
     */
    private String getContentType(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex < 0 || lastDotIndex >= filename.length() - 1) {
            return "application/octet-stream";
        }
        
        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "webp":
                return "image/webp";
            default:
                return "application/octet-stream";
        }
    }
}

