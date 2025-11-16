package com.tencent.wxcloudrun.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.SportRecordFilterRequest;
import com.tencent.wxcloudrun.dto.UpdateSportRecordRequest;
import com.tencent.wxcloudrun.dto.UseOpportunityRequest;
import com.tencent.wxcloudrun.model.SportOpportunity;
import com.tencent.wxcloudrun.model.SportRecord;
import com.tencent.wxcloudrun.service.SportOpportunityService;
import com.tencent.wxcloudrun.service.SportRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运动记录控制器
 */
@RestController
public class SportRecordController {

    final SportRecordService sportRecordService;
    final SportOpportunityService sportOpportunityService;
    final ObjectMapper objectMapper;
    final Logger logger;

    public SportRecordController(@Autowired SportRecordService sportRecordService,
                                 @Autowired SportOpportunityService sportOpportunityService) {
        this.sportRecordService = sportRecordService;
        this.sportOpportunityService = sportOpportunityService;
        this.objectMapper = new ObjectMapper();
        this.logger = LoggerFactory.getLogger(SportRecordController.class);
    }

    /**
     * 使用运动机会并创建记录
     */
    @PostMapping("/api/sport-record/use-opportunity")
    public ApiResponse useOpportunity(@RequestBody UseOpportunityRequest request,
                                      @RequestHeader("X-User-Id") Integer userId) {
        logger.info("/api/sport-record/use-opportunity post request, userId: {}", userId);

        try {
            SportOpportunity opportunity = sportOpportunityService.findById(request.getOpportunityId());
            if (opportunity == null) {
                return ApiResponse.error("运动机会不存在");
            }
            if (!opportunity.getUserId().equals(userId)) {
                return ApiResponse.error("无权使用此机会");
            }
            if (!"UNUSED".equals(opportunity.getStatus())) {
                return ApiResponse.error("该机会已被使用");
            }
            if (opportunity.getExpireAt().isBefore(java.time.LocalDateTime.now())) {
                return ApiResponse.error("该机会已过期");
            }

            String photosJson = null;
            if (request.getPhotos() != null && !request.getPhotos().isEmpty()) {
                photosJson = objectMapper.writeValueAsString(request.getPhotos());
            }

            SportRecord record = sportRecordService.createRecord(
                    userId,
                    opportunity.getTeamId(),
                    request.getSportType(),
                    request.getDuration(),
                    request.getSportDate(),
                    request.getLocation(),
                    request.getDescription(),
                    photosJson
            );

            sportOpportunityService.useOpportunity(request.getOpportunityId(), record.getId());

            return ApiResponse.ok(record);
        } catch (JsonProcessingException e) {
            logger.error("JSON处理失败", e);
            return ApiResponse.error("处理照片数据失败");
        } catch (Exception e) {
            logger.error("使用运动机会失败", e);
            return ApiResponse.error("使用运动机会失败: " + e.getMessage());
        }
    }

    /**
     * 获取我的运动记录列表
     */
    @GetMapping("/api/sport-record/my-records")
    public ApiResponse getMyRecords(@RequestHeader("X-User-Id") Integer userId,
                                    @RequestParam(required = false) Integer teamId,
                                    @RequestParam(required = false) String sportType,
                                    @RequestParam(required = false) Integer year,
                                    @RequestParam(required = false) Integer month,
                                    @RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "20") Integer pageSize) {
        logger.info("/api/sport-record/my-records get request, userId: {}", userId);

        try {
            List<SportRecord> records = sportRecordService.findByUserIdWithFilter(
                    userId, teamId, sportType, year, month, page, pageSize);
            Integer total = sportRecordService.countByUserIdWithFilter(
                    userId, teamId, sportType, year, month);

            Map<String, Object> result = new HashMap<>();
            result.put("records", records);
            result.put("total", total);
            result.put("page", page);
            result.put("pageSize", pageSize);

            return ApiResponse.ok(result);
        } catch (Exception e) {
            logger.error("获取运动记录列表失败", e);
            return ApiResponse.error("获取运动记录列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取运动记录详情
     */
    @GetMapping("/api/sport-record/{recordId}")
    public ApiResponse getRecordDetail(@PathVariable Integer recordId) {
        logger.info("/api/sport-record/{} get request", recordId);

        try {
            SportRecord record = sportRecordService.findById(recordId);
            if (record == null) {
                return ApiResponse.error("运动记录不存在");
            }
            return ApiResponse.ok(record);
        } catch (Exception e) {
            logger.error("获取运动记录详情失败", e);
            return ApiResponse.error("获取运动记录详情失败: " + e.getMessage());
        }
    }

    /**
     * 更新运动记录
     */
    @PutMapping("/api/sport-record/{recordId}")
    public ApiResponse updateRecord(@PathVariable Integer recordId,
                                    @RequestBody UpdateSportRecordRequest request,
                                    @RequestHeader("X-User-Id") Integer userId) {
        logger.info("/api/sport-record/{} put request, userId: {}", recordId, userId);

        try {
            SportRecord record = sportRecordService.findById(recordId);
            if (record == null) {
                return ApiResponse.error("运动记录不存在");
            }
            if (!record.getUserId().equals(userId)) {
                return ApiResponse.error("无权修改此记录");
            }

            record.setSportType(request.getSportType());
            record.setDuration(request.getDuration());
            record.setSportDate(request.getSportDate());
            record.setLocation(request.getLocation());
            record.setDescription(request.getDescription());

            if (request.getPhotos() != null && !request.getPhotos().isEmpty()) {
                String photosJson = objectMapper.writeValueAsString(request.getPhotos());
                record.setPhotos(photosJson);
            }

            sportRecordService.updateRecord(record);
            return ApiResponse.ok(record);
        } catch (JsonProcessingException e) {
            logger.error("JSON处理失败", e);
            return ApiResponse.error("处理照片数据失败");
        } catch (Exception e) {
            logger.error("更新运动记录失败", e);
            return ApiResponse.error("更新运动记录失败: " + e.getMessage());
        }
    }

    /**
     * 删除运动记录
     */
    @DeleteMapping("/api/sport-record/{recordId}")
    public ApiResponse deleteRecord(@PathVariable Integer recordId,
                                    @RequestHeader("X-User-Id") Integer userId) {
        logger.info("/api/sport-record/{} delete request, userId: {}", recordId, userId);

        try {
            SportRecord record = sportRecordService.findById(recordId);
            if (record == null) {
                return ApiResponse.error("运动记录不存在");
            }
            if (!record.getUserId().equals(userId)) {
                return ApiResponse.error("无权删除此记录");
            }

            sportRecordService.deleteRecord(recordId);
            return ApiResponse.ok();
        } catch (Exception e) {
            logger.error("删除运动记录失败", e);
            return ApiResponse.error("删除运动记录失败: " + e.getMessage());
        }
    }
}

