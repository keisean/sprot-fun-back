package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.WechatLoginRequest;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户认证控制器
 */
@RestController
public class AuthController {

    final UserService userService;
    final Logger logger;

    public AuthController(@Autowired UserService userService) {
        this.userService = userService;
        this.logger = LoggerFactory.getLogger(AuthController.class);
    }

    /**
     * 微信登录
     */
    @PostMapping("/api/auth/wechat-login")
    public ApiResponse wechatLogin(@RequestBody WechatLoginRequest request) {
        logger.info("/api/auth/wechat-login post request, code: {}", request.getCode());

        try {
            // TODO: 调用微信API获取openid，这里暂时使用code作为openid
            String openid = request.getCode(); // 实际应该通过code调用微信API获取openid

            User user = userService.createOrUpdate(openid, request.getNickname(), request.getAvatarUrl());
            return ApiResponse.ok(user);
        } catch (Exception e) {
            logger.error("微信登录失败", e);
            return ApiResponse.error("登录失败: " + e.getMessage());
        }
    }
}

