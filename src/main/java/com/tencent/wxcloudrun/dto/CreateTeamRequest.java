package com.tencent.wxcloudrun.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 创建团队请求DTO
 */
@Data
public class CreateTeamRequest {
    @NotBlank(message = "团队名称不能为空")
    private String teamName;
    private String description;
}
