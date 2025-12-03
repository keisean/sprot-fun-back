package com.tencent.wxcloudrun.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件服务接口
 */
public interface FileService {

    /**
     * 上传头像文件
     * @param file 文件
     * @param userId 用户ID
     * @return 文件访问URL
     * @throws IOException 文件操作异常
     */
    String uploadAvatar(MultipartFile file, Integer userId) throws IOException;

    /**
     * 通用文件上传
     * @param file 文件
     * @param category 文件分类（如：avatar, document, image等）
     * @param userId 用户ID（可选，用于组织文件目录）
     * @return 文件访问URL
     * @throws IOException 文件操作异常
     */
    String uploadFile(MultipartFile file, String category, Integer userId) throws IOException;

    /**
     * 获取文件访问URL
     * @param fileName 文件名
     * @return 文件访问URL
     */
    String getFileUrl(String fileName);
}

