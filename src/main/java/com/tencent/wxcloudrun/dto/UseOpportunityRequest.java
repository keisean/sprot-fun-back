package com.tencent.wxcloudrun.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 使用运动机会请求DTO
 */
@Data
public class UseOpportunityRequest {
    private Integer opportunityId; // 运动机会ID
    private String sportType; // 运动类型
    private Integer duration; // 运动时长（分钟）
    private LocalDate sportDate; // 运动日期
    private String location; // 运动地点（可选）
    private String description; // 运动描述（可选）
    private List<String> photos; // 照片URL列表（可选）
}

