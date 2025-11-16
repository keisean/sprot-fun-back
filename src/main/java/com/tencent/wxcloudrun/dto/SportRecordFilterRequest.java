package com.tencent.wxcloudrun.dto;

import lombok.Data;

/**
 * 运动记录筛选请求DTO
 */
@Data
public class SportRecordFilterRequest {
    private Integer teamId;
    private String sportType; // 运动类型筛选
    private Integer year; // 年份
    private Integer month; // 月份
    private Integer page = 1; // 页码
    private Integer pageSize = 20; // 每页数量
}

