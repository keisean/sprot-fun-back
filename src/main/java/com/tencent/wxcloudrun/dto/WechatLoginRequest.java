package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * 微信登录请求DTO
 */
@Data
public class WechatLoginRequest {
    private String code; // 微信登录code
    private String nickname;
    private String avatarUrl;
}

