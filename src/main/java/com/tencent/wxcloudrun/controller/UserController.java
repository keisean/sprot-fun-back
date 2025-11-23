package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.UpdateUserRequest;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.service.FileService;
import com.tencent.wxcloudrun.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 用户控制器
 */
@RestController
public class UserController {

    final UserService userService;
    final FileService fileService;
    final Logger logger;

    public UserController(@Autowired UserService userService,
                         @Autowired FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
        this.logger = LoggerFactory.getLogger(UserController.class);
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/api/user/update")
    public ApiResponse updateUserInfo(@RequestBody UpdateUserRequest request) {
        logger.info("/api/user/update post request, userId: {}, nickname: {}, avatarUrl: {}", 
                request.getUserId(), request.getNickname(), request.getAvatarUrl());

        if (request.getUserId() == null) {
            return ApiResponse.error("userId不能为空");
        }
        
        // nickname 和 avatarUrl 至少有一个不为空
        boolean hasNickname = request.getNickname() != null && !request.getNickname().trim().isEmpty();
        boolean hasAvatarUrl = request.getAvatarUrl() != null && !request.getAvatarUrl().trim().isEmpty();
        
        if (!hasNickname && !hasAvatarUrl) {
            return ApiResponse.error("nickname 和 avatarUrl 至少需要提供一个");
        }

        Optional<User> userOptional = userService.findById(request.getUserId());
        if (!userOptional.isPresent()) {
            return ApiResponse.error("用户不存在");
        }

        User user = userOptional.get();
        if (hasNickname) {
            user.setNickname(request.getNickname().trim());
        }
        if (hasAvatarUrl) {
            user.setAvatarUrl(request.getAvatarUrl().trim());
        }
        userService.updateUser(user);

        return ApiResponse.ok(user);
    }

    /**
     * 上传用户头像
     */
    @PostMapping("/api/user/avatar")
    public ApiResponse uploadAvatar(@RequestParam("avatar") MultipartFile file,
                                   @RequestHeader("X-User-Id") Integer userId) {
        logger.info("/api/user/avatar post request, userId: {}", userId);

        if (userId == null) {
            return ApiResponse.error("userId不能为空");
        }

        if (file == null || file.isEmpty()) {
            return ApiResponse.error("文件不能为空");
        }

        try {
            // 上传文件
            String avatarUrl = fileService.uploadAvatar(file, userId);

            // 更新用户头像URL
            Optional<User> userOptional = userService.findById(userId);
            if (!userOptional.isPresent()) {
                return ApiResponse.error("用户不存在");
            }

            User user = userOptional.get();
            user.setAvatarUrl(avatarUrl);
            userService.updateUser(user);

            Map<String, Object> result = new HashMap<>();
            result.put("avatarUrl", avatarUrl);
            result.put("user", user);

            return ApiResponse.ok(result);
        } catch (IllegalArgumentException e) {
            logger.error("上传头像失败: {}", e.getMessage());
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            logger.error("上传头像失败", e);
            return ApiResponse.error("上传头像失败: " + e.getMessage());
        }
    }
}
