# 团队每月运动机会管理系统 - API文档

## 基础信息

- 基础URL: `/api`
- 请求头: `X-User-Id` (用户ID，除登录接口外都需要)

## 1. 用户认证模块

### 1.1 微信登录
- **接口**: `POST /api/auth/wechat-login`
- **请求体**:
```json
{
  "code": "微信登录code",
  "nickname": "用户昵称",
  "avatarUrl": "头像URL"
}
```
- **响应**: 返回用户信息

## 2. 团队管理模块

### 2.1 创建团队
- **接口**: `POST /api/team/create`
- **请求体**:
```json
{
  "teamName": "团队名称",
  "description": "团队描述"
}
```

### 2.2 获取我的团队列表
- **接口**: `GET /api/team/my-teams`

### 2.3 获取团队详情
- **接口**: `GET /api/team/{teamId}`

### 2.4 加入团队
- **接口**: `POST /api/team/join`
- **请求体**:
```json
{
  "inviteCode": "邀请码"
}
```

### 2.5 获取团队成员列表
- **接口**: `GET /api/team/{teamId}/members`

### 2.6 移除团队成员
- **接口**: `DELETE /api/team/{teamId}/members/{memberId}`

### 2.7 删除团队
- **接口**: `DELETE /api/team/{teamId}`

## 3. 运动机会管理模块

### 3.1 分配运动机会
- **接口**: `POST /api/opportunity/allocate`
- **请求体**:
```json
{
  "teamId": 1,
  "userIds": [1, 2, 3],
  "count": 5,
  "expireAt": "2024-02-29T23:59:59"
}
```

### 3.2 获取我的运动机会列表
- **接口**: `GET /api/opportunity/my-opportunities`
- **查询参数**:
  - `teamId` (可选): 团队ID
  - `status` (可选): 状态 (UNUSED/USED)

### 3.3 获取运动机会详情
- **接口**: `GET /api/opportunity/{opportunityId}`

## 4. 运动记录模块

### 4.1 使用运动机会并创建记录
- **接口**: `POST /api/sport-record/use-opportunity`
- **请求体**:
```json
{
  "opportunityId": 1,
  "sportType": "跑步",
  "duration": 60,
  "sportDate": "2024-01-15",
  "location": "公园",
  "description": "晨跑",
  "photos": ["url1", "url2"]
}
```

### 4.2 获取我的运动记录列表
- **接口**: `GET /api/sport-record/my-records`
- **查询参数**:
  - `teamId` (可选): 团队ID
  - `sportType` (可选): 运动类型
  - `year` (可选): 年份
  - `month` (可选): 月份
  - `page` (默认1): 页码
  - `pageSize` (默认20): 每页数量

### 4.3 获取运动记录详情
- **接口**: `GET /api/sport-record/{recordId}`

### 4.4 更新运动记录
- **接口**: `PUT /api/sport-record/{recordId}`
- **请求体**:
```json
{
  "sportType": "跑步",
  "duration": 60,
  "sportDate": "2024-01-15",
  "location": "公园",
  "description": "晨跑",
  "photos": ["url1", "url2"]
}
```

### 4.5 删除运动记录
- **接口**: `DELETE /api/sport-record/{recordId}`

## 5. 统计报表模块

### 5.1 获取个人统计
- **接口**: `GET /api/statistics/personal`
- **查询参数**:
  - `teamId` (可选): 团队ID
  - `startDate` (可选): 开始日期 (格式: yyyy-MM-dd)
  - `endDate` (可选): 结束日期 (格式: yyyy-MM-dd)

### 5.2 获取团队统计
- **接口**: `GET /api/statistics/team/{teamId}`
- **查询参数**:
  - `startDate` (可选): 开始日期
  - `endDate` (可选): 结束日期

## 6. 消息通知模块

### 6.1 获取我的通知列表
- **接口**: `GET /api/notification/my-notifications`
- **查询参数**:
  - `unreadOnly` (可选): 是否只获取未读通知

### 6.2 标记通知为已读
- **接口**: `PUT /api/notification/{notificationId}/read`

### 6.3 标记所有通知为已读
- **接口**: `PUT /api/notification/read-all`

## 响应格式

### 成功响应
```json
{
  "code": 0,
  "errorMsg": "",
  "data": {}
}
```

### 错误响应
```json
{
  "code": 0,
  "errorMsg": "错误信息",
  "data": {}
}
```

