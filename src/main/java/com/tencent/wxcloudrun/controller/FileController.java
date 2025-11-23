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
     * 获取上传目录的绝对路径
     */
    private Path getUploadBasePath() {
        Path basePath;
        if (Paths.get(uploadDir).isAbsolute()) {
            // 如果配置的是绝对路径，直接使用
            basePath = Paths.get(uploadDir);
        } else {
            // 如果是相对路径，使用用户目录或系统临时目录作为基础
            String userDir = System.getProperty("user.dir");
            if (userDir != null && !userDir.isEmpty()) {
                basePath = Paths.get(userDir, uploadDir);
            } else {
                // 如果无法获取用户目录，使用系统临时目录
                String tempDir = System.getProperty("java.io.tmpdir");
                basePath = Paths.get(tempDir, uploadDir);
            }
        }
        return basePath.toAbsolutePath().normalize();
    }

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
            
            // 获取上传基础路径（绝对路径）
            Path basePath = getUploadBasePath();
            
            // 构建文件路径
            Path filePath = basePath.resolve(requestPath);
            File file = filePath.toFile();

            if (!file.exists() || !file.isFile()) {
                logger.warn("文件不存在: {} (basePath: {}, requestPath: {})", filePath, basePath, requestPath);
                return ResponseEntity.notFound().build();
            }

            // 检查文件是否在上传目录内（安全验证）
            Path resolvedPath = filePath.toAbsolutePath().normalize();
            if (!resolvedPath.startsWith(basePath)) {
                logger.warn("非法文件访问: {} (basePath: {})", resolvedPath, basePath);
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

