package com.tencent.wxcloudrun.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.wxcloudrun.dao.SportRecordMapper;
import com.tencent.wxcloudrun.model.SportRecord;
import com.tencent.wxcloudrun.service.SportRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SportRecordServiceImpl implements SportRecordService {

    final SportRecordMapper sportRecordMapper;
    final ObjectMapper objectMapper;

    public SportRecordServiceImpl(@Autowired SportRecordMapper sportRecordMapper) {
        this.sportRecordMapper = sportRecordMapper;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public SportRecord findById(Integer id) {
        return sportRecordMapper.findById(id);
    }

    @Override
    public List<SportRecord> findByUserId(Integer userId) {
        return sportRecordMapper.findByUserId(userId);
    }

    @Override
    public List<SportRecord> findByUserIdAndTeamId(Integer userId, Integer teamId) {
        return sportRecordMapper.findByUserIdAndTeamId(userId, teamId);
    }

    @Override
    public List<SportRecord> findByUserIdWithFilter(Integer userId, Integer teamId, String sportType, Integer year, Integer month, Integer page, Integer pageSize) {
        Integer offset = (page - 1) * pageSize;
        return sportRecordMapper.findByUserIdWithFilter(userId, teamId, sportType, year, month, offset, pageSize);
    }

    @Override
    public Integer countByUserIdWithFilter(Integer userId, Integer teamId, String sportType, Integer year, Integer month) {
        return sportRecordMapper.countByUserIdWithFilter(userId, teamId, sportType, year, month);
    }

    @Override
    public SportRecord createRecord(Integer userId, Integer teamId, String sportType, Integer duration, LocalDate sportDate, String location, String description, String photos) {
        SportRecord record = new SportRecord();
        record.setUserId(userId);
        record.setTeamId(teamId);
        record.setSportType(sportType);
        record.setDuration(duration);
        record.setSportDate(sportDate);
        record.setLocation(location);
        record.setDescription(description);
        record.setPhotos(photos);
        sportRecordMapper.insert(record);
        return record;
    }

    @Override
    public void updateRecord(SportRecord record) {
        sportRecordMapper.update(record);
    }

    @Override
    public void deleteRecord(Integer id) {
        sportRecordMapper.delete(id);
    }

    @Override
    public Integer sumDurationByUserIdAndDateRange(Integer userId, LocalDate startDate, LocalDate endDate) {
        return sportRecordMapper.sumDurationByUserIdAndDateRange(userId, startDate, endDate);
    }

    @Override
    public Integer countByUserIdAndDateRange(Integer userId, LocalDate startDate, LocalDate endDate) {
        return sportRecordMapper.countByUserIdAndDateRange(userId, startDate, endDate);
    }
}

