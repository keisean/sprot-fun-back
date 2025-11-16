package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 团队实体类
 */
@Data
public class Team implements Serializable {

    private Integer id;

    private String teamName;

    private String description;

    private String inviteCode;

    private Integer creatorId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

