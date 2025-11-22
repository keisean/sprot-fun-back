package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.dto.UpdateUserRequest;
import com.tencent.wxcloudrun.model.User;
import com.tencent.wxcloudrun.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * 用户控制器
 */
@RestController
public class UserController {

    final UserService userService;
    final Logger logger;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
        this.logger = LoggerFactory.getLogger(UserController.class);
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/api/user/update")
    public ApiResponse updateUserInfo(@RequestBody UpdateUserRequest request) {
        logger.info("/api/user/update post request, userId: {}, nickname: {}", request.getUserId(),
                request.getNickname());

        if (request.getUserId() == null) {
            return ApiResponse.error("userId不能为空");
        }
        if (request.getNickname() == null || request.getNickname().isEmpty()) {
            return ApiResponse.error("nickname不能为空");
        }

        Optional<User> userOptional = userService.findById(request.getUserId());
        if (!userOptional.isPresent()) {
            return ApiResponse.error("用户不存在");
        }

        User user = userOptional.get();
        user.setNickname(request.getNickname());
        userService.updateUser(user);

        return ApiResponse.ok(user);
    }
}
