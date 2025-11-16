package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.model.SportRecord;

import java.time.LocalDate;
import java.util.List;

public interface SportRecordService {

    SportRecord findById(Integer id);

    List<SportRecord> findByUserId(Integer userId);

    List<SportRecord> findByUserIdAndTeamId(Integer userId, Integer teamId);

    List<SportRecord> findByUserIdWithFilter(Integer userId, Integer teamId, String sportType, Integer year, Integer month, Integer page, Integer pageSize);

    Integer countByUserIdWithFilter(Integer userId, Integer teamId, String sportType, Integer year, Integer month);

    SportRecord createRecord(Integer userId, Integer teamId, String sportType, Integer duration, LocalDate sportDate, String location, String description, String photos);

    void updateRecord(SportRecord record);

    void deleteRecord(Integer id);

    Integer sumDurationByUserIdAndDateRange(Integer userId, LocalDate startDate, LocalDate endDate);

    Integer countByUserIdAndDateRange(Integer userId, LocalDate startDate, LocalDate endDate);
}

