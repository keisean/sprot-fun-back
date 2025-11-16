-- 用户表
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `openid` varchar(100) NOT NULL COMMENT '微信OpenID',
  `nickname` varchar(100) DEFAULT NULL COMMENT '昵称',
  `avatar_url` varchar(500) DEFAULT NULL COMMENT '头像URL',
  `role` varchar(20) NOT NULL DEFAULT 'MEMBER' COMMENT '角色：ADMIN-管理员, MEMBER-成员',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_openid` (`openid`),
  KEY `idx_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 团队表
CREATE TABLE `teams` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `team_name` varchar(100) NOT NULL COMMENT '团队名称',
  `description` varchar(500) DEFAULT NULL COMMENT '团队描述',
  `invite_code` varchar(20) NOT NULL COMMENT '邀请码',
  `creator_id` int(11) NOT NULL COMMENT '创建人ID',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_invite_code` (`invite_code`),
  KEY `idx_creator_id` (`creator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='团队表';

-- 团队成员关系表
CREATE TABLE `team_members` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `team_id` int(11) NOT NULL COMMENT '团队ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role` varchar(20) NOT NULL DEFAULT 'MEMBER' COMMENT '在团队中的角色：ADMIN-管理员, MEMBER-成员',
  `joined_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_team_user` (`team_id`, `user_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_team_id` (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='团队成员关系表';

-- 运动机会表
CREATE TABLE `sport_opportunities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `team_id` int(11) NOT NULL COMMENT '团队ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `allocated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分配时间',
  `expire_at` timestamp NOT NULL COMMENT '有效期',
  `status` varchar(20) NOT NULL DEFAULT 'UNUSED' COMMENT '状态：UNUSED-未使用, USED-已使用',
  `sport_record_id` int(11) DEFAULT NULL COMMENT '关联的运动记录ID',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_team_id` (`team_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_expire_at` (`expire_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运动机会表';

-- 运动记录表
CREATE TABLE `sport_records` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `team_id` int(11) NOT NULL COMMENT '团队ID',
  `sport_type` varchar(50) NOT NULL COMMENT '运动类型',
  `duration` int(11) NOT NULL COMMENT '运动时长（分钟）',
  `sport_date` date NOT NULL COMMENT '运动日期',
  `location` varchar(200) DEFAULT NULL COMMENT '运动地点',
  `description` text COMMENT '运动描述',
  `photos` text COMMENT '照片URL列表（JSON格式）',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_team_id` (`team_id`),
  KEY `idx_sport_date` (`sport_date`),
  KEY `idx_sport_type` (`sport_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运动记录表';

-- 消息通知表
CREATE TABLE `notifications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '接收用户ID',
  `type` varchar(50) NOT NULL COMMENT '通知类型：OPPORTUNITY_ALLOCATED-机会分配, OPPORTUNITY_EXPIRING-机会到期, SPORT_RECORDED-运动记录',
  `title` varchar(200) NOT NULL COMMENT '通知标题',
  `content` text COMMENT '通知内容',
  `is_read` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已读：0-未读, 1-已读',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_read` (`is_read`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息通知表';
