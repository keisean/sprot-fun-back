package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.NotificationMapper;
import com.tencent.wxcloudrun.model.Notification;
import com.tencent.wxcloudrun.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    final NotificationMapper notificationMapper;

    public NotificationServiceImpl(@Autowired NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    @Override
    public List<Notification> findByUserId(Integer userId) {
        return notificationMapper.findByUserId(userId);
    }

    @Override
    public List<Notification> findUnreadByUserId(Integer userId) {
        return notificationMapper.findByUserIdAndIsRead(userId, false);
    }

    @Override
    public Integer countUnread(Integer userId) {
        return notificationMapper.countUnread(userId);
    }

    @Override
    public void createNotification(Integer userId, String type, String title, String content) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setIsRead(false);
        notificationMapper.insert(notification);
    }

    @Override
    public void batchCreateNotifications(List<Notification> notifications) {
        if (!notifications.isEmpty()) {
            notificationMapper.batchInsert(notifications);
        }
    }

    @Override
    public void markAsRead(Integer id) {
        notificationMapper.markAsRead(id);
    }

    @Override
    public void markAllAsRead(Integer userId) {
        notificationMapper.markAllAsRead(userId);
    }
}

