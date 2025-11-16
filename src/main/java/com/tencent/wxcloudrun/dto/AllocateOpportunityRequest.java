package com.tencent.wxcloudrun.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 分配运动机会请求DTO
 */
@Data
public class AllocateOpportunityRequest {
    private Integer teamId;
    private List<Integer> userIds; // 目标用户ID列表
    private Integer count; // 每人分配的机会数量
    private LocalDateTime expireAt; // 有效期
}

