package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.SportOpportunityMapper;
import com.tencent.wxcloudrun.model.SportOpportunity;
import com.tencent.wxcloudrun.service.SportOpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SportOpportunityServiceImpl implements SportOpportunityService {

    final SportOpportunityMapper sportOpportunityMapper;

    public SportOpportunityServiceImpl(@Autowired SportOpportunityMapper sportOpportunityMapper) {
        this.sportOpportunityMapper = sportOpportunityMapper;
    }

    @Override
    public SportOpportunity findById(Integer id) {
        return sportOpportunityMapper.findById(id);
    }

    @Override
    public List<SportOpportunity> findByUserId(Integer userId) {
        return sportOpportunityMapper.findByUserId(userId);
    }

    @Override
    public List<SportOpportunity> findByUserIdAndStatus(Integer userId, String status) {
        return sportOpportunityMapper.findByUserIdAndStatus(userId, status);
    }

    @Override
    public List<SportOpportunity> findByUserIdAndTeamId(Integer userId, Integer teamId) {
        return sportOpportunityMapper.findByUserIdAndTeamId(userId, teamId);
    }

    @Override
    @Transactional
    public void allocateOpportunities(Integer teamId, List<Integer> userIds, Integer count, LocalDateTime expireAt) {
        List<SportOpportunity> opportunities = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Integer userId : userIds) {
            for (int i = 0; i < count; i++) {
                SportOpportunity opportunity = new SportOpportunity();
                opportunity.setTeamId(teamId);
                opportunity.setUserId(userId);
                opportunity.setAllocatedAt(now);
                opportunity.setExpireAt(expireAt);
                opportunity.setStatus("UNUSED");
                opportunities.add(opportunity);
            }
        }

        if (!opportunities.isEmpty()) {
            sportOpportunityMapper.batchInsert(opportunities);
        }
    }

    @Override
    public void useOpportunity(Integer opportunityId, Integer sportRecordId) {
        sportOpportunityMapper.updateStatus(opportunityId, "USED", sportRecordId);
    }

    @Override
    public List<SportOpportunity> findExpiredOpportunities() {
        return sportOpportunityMapper.findExpired(LocalDateTime.now());
    }
}

