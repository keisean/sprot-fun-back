package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationMapper {

    Notification findById(@Param("id") Integer id);

    List<Notification> findByUserId(@Param("userId") Integer userId);

    List<Notification> findByUserIdAndIsRead(@Param("userId") Integer userId, @Param("isRead") Boolean isRead);

    void insert(Notification notification);

    void batchInsert(@Param("list") List<Notification> notifications);

    void update(Notification notification);

    void markAsRead(@Param("id") Integer id);

    void markAllAsRead(@Param("userId") Integer userId);

    Integer countUnread(@Param("userId") Integer userId);
}

