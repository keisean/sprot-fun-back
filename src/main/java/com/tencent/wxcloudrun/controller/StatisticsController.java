package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.StatisticsResponse;
import com.tencent.wxcloudrun.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 统计报表控制器
 */
@RestController
public class StatisticsController {

    final StatisticsService statisticsService;
    final Logger logger;

    public StatisticsController(@Autowired StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
        this.logger = LoggerFactory.getLogger(StatisticsController.class);
    }

    /**
     * 获取个人统计
     */
    @GetMapping("/api/statistics/personal")
    public ApiResponse getPersonalStatistics(@RequestHeader("X-User-Id") Integer userId,
                                              @RequestParam(required = false) Integer teamId,
                                              @RequestParam(required = false) String startDate,
                                              @RequestParam(required = false) String endDate) {
        logger.info("/api/statistics/personal get request, userId: {}", userId);

        try {
            LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().withDayOfMonth(1);
            LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();

            StatisticsResponse response = statisticsService.getPersonalStatistics(userId, teamId, start, end);
            return ApiResponse.ok(response);
        } catch (Exception e) {
            logger.error("获取个人统计失败", e);
            return ApiResponse.error("获取个人统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取团队统计
     */
    @GetMapping("/api/statistics/team/{teamId}")
    public ApiResponse getTeamStatistics(@PathVariable Integer teamId,
                                         @RequestParam(required = false) String startDate,
                                         @RequestParam(required = false) String endDate) {
        logger.info("/api/statistics/team/{} get request", teamId);

        try {
            LocalDate start = startDate != null ? LocalDate.parse(startDate) : LocalDate.now().withDayOfMonth(1);
            LocalDate end = endDate != null ? LocalDate.parse(endDate) : LocalDate.now();

            StatisticsResponse response = statisticsService.getTeamStatistics(teamId, start, end);
            return ApiResponse.ok(response);
        } catch (Exception e) {
            logger.error("获取团队统计失败", e);
            return ApiResponse.error("获取团队统计失败: " + e.getMessage());
        }
    }
}

