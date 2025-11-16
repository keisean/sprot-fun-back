package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 运动机会实体类
 */
@Data
public class SportOpportunity implements Serializable {

    private Integer id;

    private Integer teamId;

    private Integer userId;

    private LocalDateTime allocatedAt;

    private LocalDateTime expireAt;

    private String status; // UNUSED-未使用, USED-已使用

    private Integer sportRecordId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

