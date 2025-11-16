package com.tencent.wxcloudrun.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 统计报表响应DTO
 */
@Data
public class StatisticsResponse {
    // 个人统计
    private Integer totalCount; // 总运动次数
    private Integer totalDuration; // 总运动时长（分钟）
    private Map<String, Integer> sportTypeDistribution; // 各运动类型分布
    private List<MonthlyStat> monthlyStats; // 月度统计

    // 团队统计
    private Integer teamTotalCount; // 团队总运动次数
    private List<RankingItem> rankings; // 排行榜
    private Map<String, Integer> teamSportTypeDistribution; // 团队运动类型分布

    @Data
    public static class MonthlyStat {
        private String month; // 月份（格式：YYYY-MM）
        private Integer count; // 运动次数
        private Integer duration; // 运动时长
    }

    @Data
    public static class RankingItem {
        private Integer userId;
        private String nickname;
        private String avatarUrl;
        private Integer count; // 运动次数
        private Integer duration; // 总时长
    }
}

