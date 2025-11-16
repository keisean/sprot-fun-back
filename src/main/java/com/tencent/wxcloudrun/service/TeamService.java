package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.Team;
import com.tencent.wxcloudrun.model.TeamMember;

import java.util.List;
import java.util.Optional;

public interface TeamService {

    Optional<Team> findById(Integer id);

    Optional<Team> findByInviteCode(String inviteCode);

    List<Team> findByUserId(Integer userId);

    Team createTeam(Integer creatorId, String teamName, String description);

    void updateTeam(Team team);

    void deleteTeam(Integer teamId);

    void joinTeam(Integer userId, String inviteCode);

    void removeMember(Integer teamId, Integer userId);

    List<TeamMember> getTeamMembers(Integer teamId);

    boolean isTeamAdmin(Integer teamId, Integer userId);
}

