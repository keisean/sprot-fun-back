package com.tencent.wxcloudrun.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 分配运动机会请求DTO
 */
@Data
public class AllocateOpportunityRequest {
    @NotNull(message = "团队ID不能为空")
    private Integer teamId;

    @NotEmpty(message = "用户列表不能为空")
    private List<Integer> userIds; // 目标用户ID列表

    @NotNull(message = "分配数量不能为空")
    @Min(value = 1, message = "分配数量至少为1")
    private Integer count; // 每人分配的机会数量

    @NotNull(message = "有效期不能为空")
    private LocalDateTime expireAt; // 有效期
}
