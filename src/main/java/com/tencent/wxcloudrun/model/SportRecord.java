package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 运动记录实体类
 */
@Data
public class SportRecord implements Serializable {

    private Integer id;

    private Integer userId;

    private Integer teamId;

    private String sportType;

    private Integer duration; // 运动时长（分钟）

    private LocalDate sportDate;

    private String location;

    private String description;

    private String photos; // JSON格式存储照片URL列表

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

