package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.SportRecordMapper;
import com.tencent.wxcloudrun.dao.TeamMemberMapper;
import com.tencent.wxcloudrun.dao.UserMapper;
import com.tencent.wxcloudrun.dto.StatisticsResponse;
import com.tencent.wxcloudrun.model.SportRecord;
import com.tencent.wxcloudrun.model.TeamMember;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    final SportRecordMapper sportRecordMapper;
    final TeamMemberMapper teamMemberMapper;
    final UserMapper userMapper;

    public StatisticsServiceImpl(@Autowired SportRecordMapper sportRecordMapper,
                                 @Autowired TeamMemberMapper teamMemberMapper,
                                 @Autowired UserMapper userMapper) {
        this.sportRecordMapper = sportRecordMapper;
        this.teamMemberMapper = teamMemberMapper;
        this.userMapper = userMapper;
    }

    @Override
    public StatisticsResponse getPersonalStatistics(Integer userId, Integer teamId, LocalDate startDate, LocalDate endDate) {
        StatisticsResponse response = new StatisticsResponse();

        List<SportRecord> records = getRecordsByDateRange(userId, teamId, startDate, endDate);

        response.setTotalCount(records.size());
        response.setTotalDuration(calculateTotalDuration(records));
        response.setSportTypeDistribution(calculateSportTypeDistribution(records));
        response.setMonthlyStats(calculateMonthlyStats(records));

        return response;
    }

    @Override
    public StatisticsResponse getTeamStatistics(Integer teamId, LocalDate startDate, LocalDate endDate) {
        StatisticsResponse response = new StatisticsResponse();

        List<TeamMember> members = teamMemberMapper.findByTeamId(teamId);
        List<Integer> userIds = members.stream()
                .map(TeamMember::getUserId)
                .collect(Collectors.toList());

        List<SportRecord> allRecords = new ArrayList<>();
        for (Integer userId : userIds) {
            List<SportRecord> userRecords = getRecordsByDateRange(userId, teamId, startDate, endDate);
            allRecords.addAll(userRecords);
        }

        response.setTeamTotalCount(allRecords.size());
        response.setTeamSportTypeDistribution(calculateSportTypeDistribution(allRecords));
        response.setRankings(calculateRankings(userIds, teamId, startDate, endDate));

        return response;
    }

    private List<SportRecord> getRecordsByDateRange(Integer userId, Integer teamId, LocalDate startDate, LocalDate endDate) {
        List<SportRecord> allRecords = sportRecordMapper.findByUserIdAndTeamId(userId, teamId);
        return allRecords.stream()
                .filter(record -> !record.getSportDate().isBefore(startDate) && !record.getSportDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    private Integer calculateTotalDuration(List<SportRecord> records) {
        return records.stream()
                .mapToInt(SportRecord::getDuration)
                .sum();
    }

    private Map<String, Integer> calculateSportTypeDistribution(List<SportRecord> records) {
        return records.stream()
                .collect(Collectors.groupingBy(
                        SportRecord::getSportType,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));
    }

    private List<StatisticsResponse.MonthlyStat> calculateMonthlyStats(List<SportRecord> records) {
        Map<String, List<SportRecord>> monthlyRecords = records.stream()
                .collect(Collectors.groupingBy(record ->
                        record.getSportDate().format(DateTimeFormatter.ofPattern("yyyy-MM"))
                ));

        return monthlyRecords.entrySet().stream()
                .map(entry -> {
                    StatisticsResponse.MonthlyStat stat = new StatisticsResponse.MonthlyStat();
                    stat.setMonth(entry.getKey());
                    stat.setCount(entry.getValue().size());
                    stat.setDuration(calculateTotalDuration(entry.getValue()));
                    return stat;
                })
                .sorted(Comparator.comparing(StatisticsResponse.MonthlyStat::getMonth))
                .collect(Collectors.toList());
    }

    private List<StatisticsResponse.RankingItem> calculateRankings(List<Integer> userIds, Integer teamId, LocalDate startDate, LocalDate endDate) {
        List<StatisticsResponse.RankingItem> rankings = new ArrayList<>();
        List<User> users = userMapper.findByIds(userIds);
        Map<Integer, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        for (Integer userId : userIds) {
            List<SportRecord> userRecords = getRecordsByDateRange(userId, teamId, startDate, endDate);
            if (!userRecords.isEmpty()) {
                StatisticsResponse.RankingItem item = new StatisticsResponse.RankingItem();
                item.setUserId(userId);
                User user = userMap.get(userId);
                if (user != null) {
                    item.setNickname(user.getNickname());
                    item.setAvatarUrl(user.getAvatarUrl());
                }
                item.setCount(userRecords.size());
                item.setDuration(calculateTotalDuration(userRecords));
                rankings.add(item);
            }
        }

        rankings.sort((a, b) -> {
            int countCompare = b.getCount().compareTo(a.getCount());
            return countCompare != 0 ? countCompare : b.getDuration().compareTo(a.getDuration());
        });

        return rankings;
    }
}

