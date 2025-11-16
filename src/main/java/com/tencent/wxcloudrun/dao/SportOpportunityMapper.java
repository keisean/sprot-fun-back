package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.SportOpportunity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SportOpportunityMapper {

    SportOpportunity findById(@Param("id") Integer id);

    List<SportOpportunity> findByUserId(@Param("userId") Integer userId);

    List<SportOpportunity> findByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") String status);

    List<SportOpportunity> findByTeamId(@Param("teamId") Integer teamId);

    List<SportOpportunity> findByUserIdAndTeamId(@Param("userId") Integer userId, @Param("teamId") Integer teamId);

    void insert(SportOpportunity opportunity);

    void batchInsert(@Param("list") List<SportOpportunity> opportunities);

    void update(SportOpportunity opportunity);

    void updateStatus(@Param("id") Integer id, @Param("status") String status, @Param("sportRecordId") Integer sportRecordId);

    List<SportOpportunity> findExpired(@Param("now") LocalDateTime now);
}

