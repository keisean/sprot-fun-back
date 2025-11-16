package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.UserMapper;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    final UserMapper userMapper;

    public UserServiceImpl(@Autowired UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findByOpenid(String openid) {
        User user = userMapper.findByOpenid(openid);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findById(Integer id) {
        User user = userMapper.findById(id);
        return Optional.ofNullable(user);
    }

    @Override
    public User createOrUpdate(String openid, String nickname, String avatarUrl) {
        User existingUser = userMapper.findByOpenid(openid);
        if (existingUser != null) {
            existingUser.setNickname(nickname);
            existingUser.setAvatarUrl(avatarUrl);
            userMapper.update(existingUser);
            return existingUser;
        } else {
            User newUser = new User();
            newUser.setOpenid(openid);
            newUser.setNickname(nickname);
            newUser.setAvatarUrl(avatarUrl);
            newUser.setRole("MEMBER");
            userMapper.insert(newUser);
            return newUser;
        }
    }

    @Override
    public void updateUser(User user) {
        userMapper.update(user);
    }
}

