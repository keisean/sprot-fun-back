package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.WechatLoginRequest;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 用户认证控制器
 */
@RestController
public class AuthController {

    final UserService userService;
    final Logger logger;

    @Value("${wechat.app-id}")
    private String appId;

    @Value("${wechat.app-secret}")
    private String appSecret;

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

        if (request.getCode() == null || request.getCode().isEmpty()) {
            return ApiResponse.error("code不能为空");
        }

        try {
            // 调用微信API获取openid
            String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId +
                    "&secret=" + appSecret + "&js_code=" + request.getCode() + "&grant_type=authorization_code";

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            logger.info("wechat api response: {}", response);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);

            if (jsonNode.has("errcode") && jsonNode.get("errcode").asInt() != 0) {
                return ApiResponse.error("微信登录失败: " + jsonNode.get("errmsg").asText());
            }

            String openid = jsonNode.get("openid").asText();
            // session_key 也可以获取，如果需要的话

            User user = userService.createOrUpdate(openid, request.getNickname(), request.getAvatarUrl());
            return ApiResponse.ok(user);
        } catch (Exception e) {
            logger.error("微信登录失败", e);
            return ApiResponse.error("登录失败: " + e.getMessage());
        }
    }
}
