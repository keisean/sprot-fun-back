package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.model.Notification;
import com.tencent.wxcloudrun.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息通知控制器
 */
@RestController
public class NotificationController {

    final NotificationService notificationService;
    final Logger logger;

    public NotificationController(@Autowired NotificationService notificationService) {
        this.notificationService = notificationService;
        this.logger = LoggerFactory.getLogger(NotificationController.class);
    }

    /**
     * 获取我的通知列表
     */
    @GetMapping("/api/notification/my-notifications")
    public ApiResponse getMyNotifications(@RequestHeader("X-User-Id") Integer userId,
                                           @RequestParam(required = false) Boolean unreadOnly) {
        logger.info("/api/notification/my-notifications get request, userId: {}", userId);

        try {
            List<Notification> notifications;
            if (Boolean.TRUE.equals(unreadOnly)) {
                notifications = notificationService.findUnreadByUserId(userId);
            } else {
                notifications = notificationService.findByUserId(userId);
            }

            Integer unreadCount = notificationService.countUnread(userId);

            Map<String, Object> result = new HashMap<>();
            result.put("notifications", notifications);
            result.put("unreadCount", unreadCount);

            return ApiResponse.ok(result);
        } catch (Exception e) {
            logger.error("获取通知列表失败", e);
            return ApiResponse.error("获取通知列表失败: " + e.getMessage());
        }
    }

    /**
     * 标记通知为已读
     */
    @PutMapping("/api/notification/{notificationId}/read")
    public ApiResponse markAsRead(@PathVariable Integer notificationId) {
        logger.info("/api/notification/{}/read put request", notificationId);

        try {
            notificationService.markAsRead(notificationId);
            return ApiResponse.ok();
        } catch (Exception e) {
            logger.error("标记通知已读失败", e);
            return ApiResponse.error("标记通知已读失败: " + e.getMessage());
        }
    }

    /**
     * 标记所有通知为已读
     */
    @PutMapping("/api/notification/read-all")
    public ApiResponse markAllAsRead(@RequestHeader("X-User-Id") Integer userId) {
        logger.info("/api/notification/read-all put request, userId: {}", userId);

        try {
            notificationService.markAllAsRead(userId);
            return ApiResponse.ok();
        } catch (Exception e) {
            logger.error("标记所有通知已读失败", e);
            return ApiResponse.error("标记所有通知已读失败: " + e.getMessage());
        }
    }
}

