package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.TeamMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeamMemberMapper {

    TeamMember findByTeamIdAndUserId(@Param("teamId") Integer teamId, @Param("userId") Integer userId);

    List<TeamMember> findByTeamId(@Param("teamId") Integer teamId);

    List<TeamMember> findByUserId(@Param("userId") Integer userId);

    void insert(TeamMember teamMember);

    void update(TeamMember teamMember);

    void delete(@Param("teamId") Integer teamId, @Param("userId") Integer userId);

    void deleteByTeamId(@Param("teamId") Integer teamId);
}

