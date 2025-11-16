package com.tencent.wxcloudrun.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息通知实体类
 */
@Data
public class Notification implements Serializable {

    private Integer id;

    private Integer userId;

    private String type; // OPPORTUNITY_ALLOCATED-机会分配, OPPORTUNITY_EXPIRING-机会到期, SPORT_RECORDED-运动记录

    private String title;

    private String content;

    private Boolean isRead; // 0-未读, 1-已读

    private LocalDateTime createdAt;
}

