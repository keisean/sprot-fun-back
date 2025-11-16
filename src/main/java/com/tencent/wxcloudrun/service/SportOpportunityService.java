package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.SportOpportunity;

import java.time.LocalDateTime;
import java.util.List;

public interface SportOpportunityService {

    SportOpportunity findById(Integer id);

    List<SportOpportunity> findByUserId(Integer userId);

    List<SportOpportunity> findByUserIdAndStatus(Integer userId, String status);

    List<SportOpportunity> findByUserIdAndTeamId(Integer userId, Integer teamId);

    void allocateOpportunities(Integer teamId, List<Integer> userIds, Integer count, LocalDateTime expireAt);

    void useOpportunity(Integer opportunityId, Integer sportRecordId);

    List<SportOpportunity> findExpiredOpportunities();
}

