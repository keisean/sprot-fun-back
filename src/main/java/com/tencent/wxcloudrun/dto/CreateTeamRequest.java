package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * 创建团队请求DTO
 */
@Data
public class CreateTeamRequest {
    private String teamName;
    private String description;
}

