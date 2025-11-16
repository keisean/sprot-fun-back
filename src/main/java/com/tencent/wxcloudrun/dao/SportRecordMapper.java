package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.SportRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface SportRecordMapper {

    SportRecord findById(@Param("id") Integer id);

    List<SportRecord> findByUserId(@Param("userId") Integer userId);

    List<SportRecord> findByUserIdAndTeamId(@Param("userId") Integer userId, @Param("teamId") Integer teamId);

    List<SportRecord> findByTeamId(@Param("teamId") Integer teamId);

    List<SportRecord> findByUserIdWithFilter(@Param("userId") Integer userId,
                                             @Param("teamId") Integer teamId,
                                             @Param("sportType") String sportType,
                                             @Param("year") Integer year,
                                             @Param("month") Integer month,
                                             @Param("offset") Integer offset,
                                             @Param("pageSize") Integer pageSize);

    Integer countByUserIdWithFilter(@Param("userId") Integer userId,
                                    @Param("teamId") Integer teamId,
                                    @Param("sportType") String sportType,
                                    @Param("year") Integer year,
                                    @Param("month") Integer month);

    void insert(SportRecord record);

    void update(SportRecord record);

    void delete(@Param("id") Integer id);

    Integer sumDurationByUserIdAndDateRange(@Param("userId") Integer userId,
                                            @Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate);

    Integer countByUserIdAndDateRange(@Param("userId") Integer userId,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);
}

