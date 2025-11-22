package com.tencent.wxcloudrun.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 加入团队请求DTO
 */
@Data
public class JoinTeamRequest {
    @NotBlank(message = "邀请码不能为空")
    private String inviteCode; // 团队邀请码
}
