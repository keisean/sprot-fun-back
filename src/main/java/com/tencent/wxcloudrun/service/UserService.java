package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByOpenid(String openid);

    Optional<User> findById(Integer id);

    User createOrUpdate(String openid, String nickname, String avatarUrl);

    void updateUser(User user);
}

