package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.Notification;

import java.util.List;

public interface NotificationService {

    List<Notification> findByUserId(Integer userId);

    List<Notification> findUnreadByUserId(Integer userId);

    Integer countUnread(Integer userId);

    void createNotification(Integer userId, String type, String title, String content);

    void batchCreateNotifications(List<Notification> notifications);

    void markAsRead(Integer id);

    void markAllAsRead(Integer userId);
}

