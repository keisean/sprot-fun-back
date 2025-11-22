# Git 推送问题解决指南

## 问题：Connection was reset / Recv failure

这是网络连接问题，常见于访问GitHub时。以下是几种解决方案：

## 解决方案

### 方案一：使用SSH方式（推荐）

**1. 检查是否已有SSH密钥**
```bash
ls ~/.ssh
```

**2. 如果没有，生成SSH密钥**
```bash
ssh-keygen -t rsa -b 4096 -C "your_email@example.com"
```
按回车使用默认路径，可以设置密码或直接回车。

**3. 查看公钥并复制**
```bash
cat ~/.ssh/id_rsa.pub
```
复制输出的内容。

**4. 在GitHub添加SSH密钥**
- 登录GitHub
- 点击右上角头像 -> Settings
- 左侧菜单选择 SSH and GPG keys
- 点击 New SSH key
- 粘贴刚才复制的公钥，保存

**5. 修改远程仓库地址为SSH**
```bash
git remote set-url origin git@github.com:keisean/sprot-fun-back.git
```

**6. 测试连接**
```bash
ssh -T git@github.com
```

**7. 重新推送**
```bash
git push -u origin master
```

### 方案二：配置HTTP代理（如果使用代理）

**1. 设置HTTP代理**
```bash
git config --global http.proxy http://127.0.0.1:7890
git config --global https.proxy http://127.0.0.1:7890
```
（将7890替换为你的代理端口）

**2. 取消代理（如果不需要）**
```bash
git config --global --unset http.proxy
git config --global --unset https.proxy
```

### 方案三：增加缓冲区大小

```bash
git config --global http.postBuffer 524288000
git config --global http.maxRequestBuffer 100M
git config --global core.compression 0
```

### 方案四：使用GitHub CLI（gh）

**1. 安装GitHub CLI**
从 https://cli.github.com/ 下载安装

**2. 登录**
```bash
gh auth login
```

### 方案五：使用国内镜像或Gitee

**1. 使用Gitee（码云）**
```bash
# 在Gitee创建仓库后
git remote set-url origin https://gitee.com/your_username/sprot-fun-back.git
git push -u origin master
```

**2. 使用GitHub镜像**
```bash
# 修改hosts文件（需要管理员权限）
# Windows: C:\Windows\System32\drivers\etc\hosts
# 添加：
# 140.82.112.3 github.com
```

### 方案六：分块推送（大文件时）

```bash
# 先推送少量提交
git push -u origin master --verbose

# 如果还是失败，尝试增加超时时间
git config --global http.lowSpeedLimit 0
git config --global http.lowSpeedTime 999999
```

## 快速诊断命令

```bash
# 查看远程仓库地址
git remote -v

# 测试网络连接
ping github.com

# 查看Git配置
git config --list

# 查看详细错误信息
git push -u origin master --verbose
```

## 推荐操作流程

1. **首先尝试SSH方式**（最稳定）
2. 如果SSH不行，检查网络和代理设置
3. 考虑使用Gitee作为替代方案

