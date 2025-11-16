package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.StatisticsResponse;

import java.time.LocalDate;

public interface StatisticsService {

    StatisticsResponse getPersonalStatistics(Integer userId, Integer teamId, LocalDate startDate, LocalDate endDate);

    StatisticsResponse getTeamStatistics(Integer teamId, LocalDate startDate, LocalDate endDate);
}

