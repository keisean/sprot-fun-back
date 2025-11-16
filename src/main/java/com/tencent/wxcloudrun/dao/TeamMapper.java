package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Team;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeamMapper {

    Team findById(@Param("id") Integer id);

    Team findByInviteCode(@Param("inviteCode") String inviteCode);

    List<Team> findByUserId(@Param("userId") Integer userId);

    void insert(Team team);

    void update(Team team);

    void delete(@Param("id") Integer id);
}

