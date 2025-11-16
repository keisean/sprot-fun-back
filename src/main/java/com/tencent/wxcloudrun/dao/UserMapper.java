package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    User findByOpenid(@Param("openid") String openid);

    User findById(@Param("id") Integer id);

    void insert(User user);

    void update(User user);

    List<User> findByIds(@Param("ids") List<Integer> ids);
}

