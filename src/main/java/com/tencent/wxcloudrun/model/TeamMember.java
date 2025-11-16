package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 团队成员关系实体类
 */
@Data
public class TeamMember implements Serializable {

    private Integer id;

    private Integer teamId;

    private Integer userId;

    private String role; // ADMIN-管理员, MEMBER-成员

    private LocalDateTime joinedAt;
}

