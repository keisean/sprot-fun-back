package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件访问控制器
 */
@RestController
@RequestMapping("/api/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    final FileService fileService;

    @Value("${file.upload.dir:uploads}")
    private String uploadDir;

    public FileController(@Autowired FileService fileService) {
        this.fileService = fileService;
    }

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
     * 通用文件上传接口
     * @param file 文件
     * @param category 文件分类（可选，如：avatar, document, image等）
     * @param userId 用户ID（可选，从请求头获取）
     * @return 文件上传结果
     */
    @PostMapping("/upload")
    public ApiResponse uploadFile(@RequestParam("file") MultipartFile file,
                                 @RequestParam(value = "category", required = false) String category,
                                 @RequestHeader(value = "X-User-Id", required = false) Integer userId) {
        logger.info("/api/files/upload post request, category: {}, userId: {}, fileName: {}", 
                category, userId, file.getOriginalFilename());

        if (file == null || file.isEmpty()) {
            return ApiResponse.error("文件不能为空");
        }

        try {
            // 上传文件
            String fileUrl = fileService.uploadFile(file, category, userId);

            Map<String, Object> result = new HashMap<>();
            result.put("fileUrl", fileUrl);
            result.put("fileName", file.getOriginalFilename());
            result.put("fileSize", file.getSize());
            result.put("contentType", file.getContentType());

            return ApiResponse.ok(result);
        } catch (IllegalArgumentException e) {
            logger.error("文件上传失败: {}", e.getMessage());
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            logger.error("文件上传失败", e);
            return ApiResponse.error("文件上传失败: " + e.getMessage());
        }
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

