# Git 代码提交指南

## 一、初始化Git仓库（如果还没有初始化）

### 1. 检查是否已初始化
```bash
git status
```

如果提示 "not a git repository"，需要先初始化。

### 2. 初始化Git仓库
```bash
git init
```

## 二、配置Git用户信息（首次使用需要）

```bash
git config --global user.name "你的名字"
git config --global user.email "你的邮箱"
```

## 三、提交代码步骤

### 步骤1：查看当前状态
```bash
git status
```
这会显示哪些文件被修改、新增或删除。

### 步骤2：添加文件到暂存区

**方式一：添加所有文件**
```bash
git add .
```

**方式二：选择性添加文件**
```bash
git add src/main/java/com/tencent/wxcloudrun/controller/
git add src/main/resources/db.sql
```

**方式三：添加特定文件**
```bash
git add 文件名
```

### 步骤3：提交代码
```bash
git commit -m "提交说明信息"
```

**提交信息示例：**
```bash
git commit -m "feat: 完成团队运动机会管理系统后端代码"
git commit -m "feat: 添加用户认证、团队管理、运动机会等模块"
git commit -m "docs: 添加API文档和配置说明"
```

**提交信息规范（推荐）：**
- `feat:` 新功能
- `fix:` 修复bug
- `docs:` 文档更新
- `style:` 代码格式调整
- `refactor:` 代码重构
- `test:` 测试相关
- `chore:` 构建/工具相关

### 步骤4：查看提交历史
```bash
git log
```

## 四、推送到远程仓库

### 1. 添加远程仓库（如果还没有）
```bash
git remote add origin <远程仓库地址>
```

**示例：**
```bash
git remote add origin https://github.com/username/sport-fun-back.git
# 或
git remote add origin git@github.com:username/sport-fun-back.git
```

### 2. 查看远程仓库
```bash
git remote -v
```

### 3. 推送到远程仓库

**首次推送：**
```bash
git push -u origin main
# 或
git push -u origin master
```

**后续推送：**
```bash
git push
```

## 五、完整提交流程示例

```bash
# 1. 查看状态
git status

# 2. 添加所有文件
git add .

# 3. 提交
git commit -m "feat: 完成团队运动机会管理系统后端代码

- 添加用户认证模块
- 添加团队管理模块
- 添加运动机会管理模块
- 添加运动记录模块
- 添加统计报表模块
- 添加消息通知模块
- 完成数据库设计和Mapper配置
- 添加API文档和配置说明"

# 4. 推送到远程
git push
```

## 六、常用Git命令

```bash
# 查看差异
git diff

# 查看提交历史（简洁版）
git log --oneline

# 撤销暂存区的文件
git reset HEAD <文件名>

# 撤销工作区的修改
git checkout -- <文件名>

# 创建新分支
git branch <分支名>

# 切换分支
git checkout <分支名>

# 查看所有分支
git branch -a

# 合并分支
git merge <分支名>
```

## 七、注意事项

1. **不要提交敏感信息**
   - 数据库密码
   - API密钥
   - 个人配置文件（如 `application-local.yml` 已在.gitignore中）

2. **提交前检查**
   - 确保代码可以编译通过
   - 确保没有提交临时文件

3. **提交信息要清晰**
   - 用中文或英文清晰描述本次提交的内容
   - 遵循提交信息规范

4. **定期提交**
   - 完成一个功能模块就提交一次
   - 不要积累太多改动再提交

## 八、如果使用Git图形化工具

- **VS Code**: 内置Git支持，可以直接在界面中操作
- **GitHub Desktop**: 图形化Git客户端
- **SourceTree**: Atlassian的Git图形化工具
- **TortoiseGit**: Windows下的Git图形化工具

