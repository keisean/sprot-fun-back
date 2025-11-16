package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
public class User implements Serializable {

    private Integer id;

    private String openid;

    private String nickname;

    private String avatarUrl;

    private String role; // ADMIN-管理员, MEMBER-成员

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

