package com.tencent.wxcloudrun.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 更新运动记录请求DTO
 */
@Data
public class UpdateSportRecordRequest {
    private String sportType;
    private Integer duration;
    private LocalDate sportDate;
    private String location;
    private String description;
    private List<String> photos;
}

