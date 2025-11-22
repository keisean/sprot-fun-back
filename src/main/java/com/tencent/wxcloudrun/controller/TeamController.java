package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.CreateTeamRequest;
import com.tencent.wxcloudrun.dto.JoinTeamRequest;
import com.tencent.wxcloudrun.model.Team;
import com.tencent.wxcloudrun.model.TeamMember;
import com.tencent.wxcloudrun.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

/**
 * 团队管理控制器
 */
@RestController
public class TeamController {

    final TeamService teamService;
    final Logger logger;

    public TeamController(@Autowired TeamService teamService) {
        this.teamService = teamService;
        this.logger = LoggerFactory.getLogger(TeamController.class);
    }

    /**
     * 创建团队
     */
    @PostMapping("/api/team/create")
    public ApiResponse createTeam(@RequestBody @Valid CreateTeamRequest request,
            @RequestHeader("X-User-Id") Integer userId) {
        logger.info("/api/team/create post request, userId: {}", userId);

        try {
            Team team = teamService.createTeam(userId, request.getTeamName(), request.getDescription());
            return ApiResponse.ok(team);
        } catch (Exception e) {
            logger.error("创建团队失败", e);
            return ApiResponse.error("创建团队失败: " + e.getMessage());
        }
    }

    /**
     * 获取我的团队列表
     */
    @GetMapping("/api/team/my-teams")
    public ApiResponse getMyTeams(@RequestHeader("X-User-Id") Integer userId) {
        logger.info("/api/team/my-teams get request, userId: {}", userId);

        try {
            List<Team> teams = teamService.findByUserId(userId);
            return ApiResponse.ok(teams);
        } catch (Exception e) {
            logger.error("获取团队列表失败", e);
            return ApiResponse.error("获取团队列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取团队详情
     */
    @GetMapping("/api/team/{teamId}")
    public ApiResponse getTeamDetail(@PathVariable Integer teamId) {
        logger.info("/api/team/{} get request", teamId);

        try {
            return teamService.findById(teamId)
                    .map(ApiResponse::ok)
                    .orElse(ApiResponse.error("团队不存在"));
        } catch (Exception e) {
            logger.error("获取团队详情失败", e);
            return ApiResponse.error("获取团队详情失败: " + e.getMessage());
        }
    }

    /**
     * 加入团队
     */
    @PostMapping("/api/team/join")
    public ApiResponse joinTeam(@RequestBody @Valid JoinTeamRequest request,
            @RequestHeader("X-User-Id") Integer userId) {
        logger.info("/api/team/join post request, userId: {}", userId);

        try {
            teamService.joinTeam(userId, request.getInviteCode());
            return ApiResponse.ok();
        } catch (Exception e) {
            logger.error("加入团队失败", e);
            return ApiResponse.error("加入团队失败: " + e.getMessage());
        }
    }

    /**
     * 获取团队成员列表
     */
    @GetMapping("/api/team/{teamId}/members")
    public ApiResponse getTeamMembers(@PathVariable Integer teamId) {
        logger.info("/api/team/{}/members get request", teamId);

        try {
            List<TeamMember> members = teamService.getTeamMembers(teamId);
            return ApiResponse.ok(members);
        } catch (Exception e) {
            logger.error("获取团队成员失败", e);
            return ApiResponse.error("获取团队成员失败: " + e.getMessage());
        }
    }

    /**
     * 移除团队成员
     */
    @DeleteMapping("/api/team/{teamId}/members/{memberId}")
    public ApiResponse removeMember(@PathVariable Integer teamId,
            @PathVariable Integer memberId,
            @RequestHeader("X-User-Id") Integer userId) {
        logger.info("/api/team/{}/members/{} delete request", teamId, memberId);

        try {
            if (!teamService.isTeamAdmin(teamId, userId)) {
                return ApiResponse.error("无权限操作");
            }
            teamService.removeMember(teamId, memberId);
            return ApiResponse.ok();
        } catch (Exception e) {
            logger.error("移除成员失败", e);
            return ApiResponse.error("移除成员失败: " + e.getMessage());
        }
    }

    /**
     * 删除团队
     */
    @DeleteMapping("/api/team/{teamId}")
    public ApiResponse deleteTeam(@PathVariable Integer teamId,
            @RequestHeader("X-User-Id") Integer userId) {
        logger.info("/api/team/{} delete request", teamId);

        try {
            if (!teamService.isTeamAdmin(teamId, userId)) {
                return ApiResponse.error("无权限操作");
            }
            teamService.deleteTeam(teamId);
            return ApiResponse.ok();
        } catch (Exception e) {
            logger.error("删除团队失败", e);
            return ApiResponse.error("删除团队失败: " + e.getMessage());
        }
    }
}
