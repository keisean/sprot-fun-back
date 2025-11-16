package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.AllocateOpportunityRequest;
import com.tencent.wxcloudrun.model.SportOpportunity;
import com.tencent.wxcloudrun.service.SportOpportunityService;
import com.tencent.wxcloudrun.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运动机会管理控制器
 */
@RestController
public class SportOpportunityController {

    final SportOpportunityService sportOpportunityService;
    final TeamService teamService;
    final Logger logger;

    public SportOpportunityController(@Autowired SportOpportunityService sportOpportunityService,
                                     @Autowired TeamService teamService) {
        this.sportOpportunityService = sportOpportunityService;
        this.teamService = teamService;
        this.logger = LoggerFactory.getLogger(SportOpportunityController.class);
    }

    /**
     * 分配运动机会
     */
    @PostMapping("/api/opportunity/allocate")
    public ApiResponse allocateOpportunities(@RequestBody AllocateOpportunityRequest request,
                                             @RequestHeader("X-User-Id") Integer userId) {
        logger.info("/api/opportunity/allocate post request, userId: {}", userId);

        try {
            if (!teamService.isTeamAdmin(request.getTeamId(), userId)) {
                return ApiResponse.error("无权限操作");
            }

            sportOpportunityService.allocateOpportunities(
                    request.getTeamId(),
                    request.getUserIds(),
                    request.getCount(),
                    request.getExpireAt()
            );
            return ApiResponse.ok();
        } catch (Exception e) {
            logger.error("分配运动机会失败", e);
            return ApiResponse.error("分配运动机会失败: " + e.getMessage());
        }
    }

    /**
     * 获取我的运动机会列表
     */
    @GetMapping("/api/opportunity/my-opportunities")
    public ApiResponse getMyOpportunities(@RequestHeader("X-User-Id") Integer userId,
                                          @RequestParam(required = false) Integer teamId,
                                          @RequestParam(required = false) String status) {
        logger.info("/api/opportunity/my-opportunities get request, userId: {}", userId);

        try {
            List<SportOpportunity> opportunities;
            if (teamId != null) {
                opportunities = sportOpportunityService.findByUserIdAndTeamId(userId, teamId);
            } else if (status != null) {
                opportunities = sportOpportunityService.findByUserIdAndStatus(userId, status);
            } else {
                opportunities = sportOpportunityService.findByUserId(userId);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("opportunities", opportunities);
            result.put("total", opportunities.size());
            result.put("unused", opportunities.stream().filter(o -> "UNUSED".equals(o.getStatus())).count());
            result.put("used", opportunities.stream().filter(o -> "USED".equals(o.getStatus())).count());

            return ApiResponse.ok(result);
        } catch (Exception e) {
            logger.error("获取运动机会列表失败", e);
            return ApiResponse.error("获取运动机会列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取运动机会详情
     */
    @GetMapping("/api/opportunity/{opportunityId}")
    public ApiResponse getOpportunityDetail(@PathVariable Integer opportunityId) {
        logger.info("/api/opportunity/{} get request", opportunityId);

        try {
            SportOpportunity opportunity = sportOpportunityService.findById(opportunityId);
            if (opportunity == null) {
                return ApiResponse.error("运动机会不存在");
            }
            return ApiResponse.ok(opportunity);
        } catch (Exception e) {
            logger.error("获取运动机会详情失败", e);
            return ApiResponse.error("获取运动机会详情失败: " + e.getMessage());
        }
    }
}

