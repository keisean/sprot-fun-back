package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.TeamMapper;
import com.tencent.wxcloudrun.dao.TeamMemberMapper;
import com.tencent.wxcloudrun.model.Team;
import com.tencent.wxcloudrun.model.TeamMember;
import com.tencent.wxcloudrun.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeamServiceImpl implements TeamService {

    final TeamMapper teamMapper;
    final TeamMemberMapper teamMemberMapper;

    public TeamServiceImpl(@Autowired TeamMapper teamMapper,
                           @Autowired TeamMemberMapper teamMemberMapper) {
        this.teamMapper = teamMapper;
        this.teamMemberMapper = teamMemberMapper;
    }

    @Override
    public Optional<Team> findById(Integer id) {
        Team team = teamMapper.findById(id);
        return Optional.ofNullable(team);
    }

    @Override
    public Optional<Team> findByInviteCode(String inviteCode) {
        Team team = teamMapper.findByInviteCode(inviteCode);
        return Optional.ofNullable(team);
    }

    @Override
    public List<Team> findByUserId(Integer userId) {
        return teamMapper.findByUserId(userId);
    }

    @Override
    @Transactional
    public Team createTeam(Integer creatorId, String teamName, String description) {
        Team team = new Team();
        team.setTeamName(teamName);
        team.setDescription(description);
        team.setCreatorId(creatorId);
        team.setInviteCode(generateInviteCode());
        teamMapper.insert(team);

        TeamMember member = new TeamMember();
        member.setTeamId(team.getId());
        member.setUserId(creatorId);
        member.setRole("ADMIN");
        teamMemberMapper.insert(member);

        return team;
    }

    @Override
    public void updateTeam(Team team) {
        teamMapper.update(team);
    }

    @Override
    @Transactional
    public void deleteTeam(Integer teamId) {
        teamMemberMapper.deleteByTeamId(teamId);
        teamMapper.delete(teamId);
    }

    @Override
    @Transactional
    public void joinTeam(Integer userId, String inviteCode) {
        Team team = teamMapper.findByInviteCode(inviteCode);
        if (team == null) {
            throw new RuntimeException("邀请码无效");
        }

        TeamMember existing = teamMemberMapper.findByTeamIdAndUserId(team.getId(), userId);
        if (existing != null) {
            throw new RuntimeException("您已经是该团队成员");
        }

        TeamMember member = new TeamMember();
        member.setTeamId(team.getId());
        member.setUserId(userId);
        member.setRole("MEMBER");
        teamMemberMapper.insert(member);
    }

    @Override
    public void removeMember(Integer teamId, Integer userId) {
        teamMemberMapper.delete(teamId, userId);
    }

    @Override
    public List<TeamMember> getTeamMembers(Integer teamId) {
        return teamMemberMapper.findByTeamId(teamId);
    }

    @Override
    public boolean isTeamAdmin(Integer teamId, Integer userId) {
        TeamMember member = teamMemberMapper.findByTeamIdAndUserId(teamId, userId);
        return member != null && "ADMIN".equals(member.getRole());
    }

    private String generateInviteCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

