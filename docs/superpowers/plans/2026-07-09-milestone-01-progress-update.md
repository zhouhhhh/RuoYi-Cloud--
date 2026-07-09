# Milestone 01 Progress Update Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Mark milestone 01 complete, move the active learning task to milestone 02, and preserve a sanitized milestone 01 learning record.

**Architecture:** Keep project state and next-task facts in `AGENTS.md`, keep the public overview in `README.md`, and store chronological troubleshooting detail under `docs/progress/`. Cross-link the files without copying sensitive request data.

**Tech Stack:** Markdown, Git

## Global Constraints

- Do not save HAR files, passwords, tokens, cookies, or secret values.
- Do not modify business code, infrastructure configuration, or AI collaboration rules.
- Keep `AGENTS.md` focused on project facts, constraints, current state, and the next learning task.
- Do not expand work beyond milestone 02's first request-chain exercise.

---

### Task 1: Add The Milestone 01 Learning Record

**Files:**
- Create: `docs/progress/2026-07-09-milestone-01.md`

**Interfaces:**
- Consumes: The verified container state, Nacos service list, login flow, failed captcha request, and two resolved startup failures.
- Produces: A sanitized historical record linked from `README.md`.

- [x] **Step 1: Create the progress directory and record**

Create `docs/progress/2026-07-09-milestone-01.md` with these sections:

```markdown
# 里程碑 01：环境基线

- 日期：2026-07-09
- 状态：已完成

## 完成内容

- 启动 MySQL 8.0、Redis 7 和 Nacos 3.1.2。
- 初始化 `ry-cloud` 和 `ry-config`。
- 启动 Gateway、Auth、System、Gen 和 Vue3 前端。
- 在 Nacos 中确认四个服务各有一个健康实例。
- 验证验证码、登录、用户信息、动态路由和用户列表请求。
- 使用错误验证码验证失败路径。

## 验证结果

- MySQL 与 Redis 容器健康检查通过。
- Nacos 可以读取配置，四个核心服务注册正常。
- 正确验证码可以登录并进入用户管理页面。
- 错误验证码被拒绝；HTTP 状态与业务 `code` 需要分别判断。

## 排错记录

### 公共模块编译产物不完整

- 现象：System 启动时出现 `ClassNotFoundException: com.ruoyi.common.core.domain.R`。
- 证据：`R.java` 和 Maven 依赖存在，但运行 classpath 对应目录没有 `R.class`。
- 根因：`ruoyi-common-core` 的编译产物不完整。
- 处理：从后端根项目重新构建，确认 `R.class` 生成后重启服务。
- 结果：System 正常启动并注册到 Nacos。

### Redis 认证失败

- 现象：Gateway 无法生成验证码，相关服务访问 Redis 失败。
- 证据：本地 Redis 已开启认证，Nacos 中部分服务的 Redis 密码为空。
- 根因：运行配置与本地 Compose 的 Redis 认证配置不一致。
- 处理：补全相关 Nacos 配置并重启依赖服务。
- 结果：验证码、登录和用户列表恢复正常。

## 本次掌握

- Gateway 是请求入口，不是其他服务的启动前置条件。
- `clean` 只删除构建产物，重新编译才会生成缺失的 `.class`。
- 排查 `ClassNotFoundException` 时应检查源码、依赖、classpath 和实际类文件。
- 服务启动成功不代表延迟访问的 Redis 功能一定正常。
- HTTP 状态码和业务响应 `code` 是两个不同层次的结果。
- 连续出现的错误可能有不同根因，需要分别收集证据。

## 下一步

进入里程碑 02，从 `/code` 开始跟踪：

`login.vue` → `getCodeImg()` → Vite 代理 → Gateway 路由函数 →
`ValidateCodeHandler` → `ValidateCodeServiceImpl` → Redis。
```

- [x] **Step 2: Verify the record is sanitized**

Run:

```bash
rg -n 'root1234|rEdis211|access_token|Authorization:|Cookie:|localhost\.har' docs/progress/2026-07-09-milestone-01.md
```

Expected: no matches.

### Task 2: Advance The Active Milestone

**Files:**
- Modify: `AGENTS.md`
- Modify: `README.md`

**Interfaces:**
- Consumes: `docs/progress/2026-07-09-milestone-01.md`.
- Produces: Consistent current-state documentation for the next session.

- [x] **Step 1: Update `AGENTS.md`**

Replace the current-task section with:

```markdown
## 当前任务

当前里程碑：`02 请求链路`。

开始时只完成验证码请求链路这一小闭环：

1. 在浏览器 Network 中重新请求 `/dev-api/code`。
2. 从 `src/views/login.vue` 定位到 `src/api/login.js` 的 `getCodeImg()`。
3. 说明 `/dev-api` 如何经过 Vite 代理到 Gateway。
4. 定位 Gateway 中 `/code` 的路由函数、Handler 和 Service。
5. 在 Redis 中观察 `captcha_codes:*` 的 Key 与 TTL。
6. 使用源码、Network、Redis 和必要日志画出验证码请求链路。

本轮只跟踪验证码生成，不同时展开登录、Token、用户信息和动态路由。
```

Change milestone 01 to:

```markdown
- **状态：** [x]
```

Keep milestone 02 as:

```markdown
- **状态：** [ ]
```

- [x] **Step 2: Update `README.md`**

Replace the current-progress list with:

```markdown
- 已完成：`01 环境基线`
- 当前里程碑：`02 请求链路`
- 当前目标：从验证码请求开始，跟踪 Vue3、Vite、Gateway 和 Redis 的完整调用链
- 完整任务和验收标准：[AGENTS.md](AGENTS.md)
- 最近学习记录：[里程碑 01：环境基线](docs/progress/2026-07-09-milestone-01.md)
```

Add the learning record under `## 文档`:

```markdown
- [里程碑 01 学习记录](docs/progress/2026-07-09-milestone-01.md)
```

- [x] **Step 3: Verify state consistency and links**

Run:

```bash
rg -n '当前里程碑|已完成|状态：' AGENTS.md README.md
test -f docs/progress/2026-07-09-milestone-01.md
git diff --check
```

Expected:

- Both current-progress sections name `02 请求链路`.
- Milestone 01 is `[x]`.
- Milestone 02 remains `[ ]`.
- The progress file exists.
- `git diff --check` exits successfully.

- [x] **Step 4: Review the final diff**

Run:

```bash
git diff -- AGENTS.md README.md docs/progress/2026-07-09-milestone-01.md
```

Expected: only the approved progress, milestone, and learning-record changes are present.
