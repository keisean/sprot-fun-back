package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * 加入团队请求DTO
 */
@Data
public class JoinTeamRequest {
    private String inviteCode; // 团队邀请码
}

