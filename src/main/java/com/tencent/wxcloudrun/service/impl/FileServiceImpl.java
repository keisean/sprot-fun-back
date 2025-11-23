package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件服务实现类
 */
@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${file.upload.dir:uploads}")
    private String uploadDir;

    @Value("${file.upload.avatar.path:avatar}")
    private String avatarPath;

    @Value("${file.access.base-url:}")
    private String baseUrl;

    @Override
    public String uploadAvatar(MultipartFile file, Integer userId) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        // 验证文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        String extension = getFileExtension(originalFilename);
        if (!isImageFile(extension)) {
            throw new IllegalArgumentException("只支持图片文件格式：jpg, jpeg, png, gif, webp");
        }

        // 验证文件大小（限制为5MB）
        long maxSize = 5 * 1024 * 1024; // 5MB
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("文件大小不能超过5MB");
        }

        // 创建上传目录
        String relativePath = avatarPath + File.separator + userId;
        Path uploadPath = Paths.get(uploadDir, relativePath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名
        String fileName = UUID.randomUUID().toString() + extension;
        Path filePath = uploadPath.resolve(fileName);

        // 保存文件
        file.transferTo(filePath.toFile());

        logger.info("头像上传成功: userId={}, fileName={}", userId, fileName);

        // 返回文件访问路径
        String relativeFilePath = relativePath.replace(File.separator, "/") + "/" + fileName;
        return getFileUrl(relativeFilePath);
    }

    @Override
    public String getFileUrl(String fileName) {
        if (baseUrl != null && !baseUrl.isEmpty()) {
            // 如果配置了基础URL，使用配置的URL
            return baseUrl + "/" + fileName.replace("\\", "/");
        } else {
            // 否则使用相对路径
            return "/api/files/" + fileName.replace("\\", "/");
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex).toLowerCase();
        }
        return "";
    }

    /**
     * 判断是否为图片文件
     */
    private boolean isImageFile(String extension) {
        return extension.equals(".jpg") || extension.equals(".jpeg") 
                || extension.equals(".png") || extension.equals(".gif") 
                || extension.equals(".webp");
    }
}

