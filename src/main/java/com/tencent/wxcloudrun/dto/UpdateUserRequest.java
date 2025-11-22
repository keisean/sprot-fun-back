package com.tencent.wxcloudrun.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private Integer userId;
    private String nickname;
}
