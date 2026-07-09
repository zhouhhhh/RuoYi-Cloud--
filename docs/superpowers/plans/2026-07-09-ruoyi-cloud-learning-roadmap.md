# RuoYi-Cloud Learning Roadmap Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace the generic `AGENTS.md` with the approved project-driven learning roadmap and remove the unrelated Vue2 frontend without breaking Docker frontend packaging.

**Architecture:** Keep the Spring Boot 3 backend and the standalone Vue3 frontend as the only development targets. Record 16 sequential, evidence-based learning milestones in `AGENTS.md`; update the Docker copy script before deleting the embedded Vue2 source tree.

**Tech Stack:** Markdown, POSIX shell, Git, Java 17, Spring Boot 3.5, Spring Cloud 2025, Vue 3, Vite, Element Plus, Docker Compose

## Global Constraints

- Preserve the user's existing deletion of `CODEX_CONTEXT.md`.
- Do not include unrelated refactors or new dependencies.
- Use `ruoyi-cloud-learning/RuoYi-Cloud-springboot3` as the backend root.
- Use `ruoyi-cloud-learning/RuoYi-Cloud-Vue3-master` as the only frontend root.
- Delete `ruoyi-cloud-learning/RuoYi-Cloud-springboot3/ruoyi-ui`.
- Keep the container-internal `/home/ruoyi/projects/ruoyi-ui` path; it is a deployment destination, not a source tree.
- Do not mark any learning milestone complete without reproducible evidence.
- Do not commit implementation changes unless the user explicitly requests a commit.

---

## File Map

- Modify `AGENTS.md`: authoritative project facts, commands, constraints, learning workflow, and 16 milestones.
- Modify `ruoyi-cloud-learning/RuoYi-Cloud-springboot3/docker/copy.sh`: copy the standalone Vue3 build instead of the deleted Vue2 build.
- Delete `ruoyi-cloud-learning/RuoYi-Cloud-springboot3/ruoyi-ui/`: remove the unrelated Vue2 application.
- Preserve `docs/ai-tutor-mode.md` and `docs/ai-review-mode.md`: existing AI collaboration rules remain authoritative.

### Task 1: Remove The Legacy Vue2 Frontend Safely

**Files:**
- Modify: `ruoyi-cloud-learning/RuoYi-Cloud-springboot3/docker/copy.sh:15-17`
- Delete: `ruoyi-cloud-learning/RuoYi-Cloud-springboot3/ruoyi-ui/`
- Verify: `ruoyi-cloud-learning/RuoYi-Cloud-Vue3-master/package.json`

**Interfaces:**
- Consumes: Vue3 production output at `ruoyi-cloud-learning/RuoYi-Cloud-Vue3-master/dist`.
- Produces: Docker static files at `ruoyi-cloud-learning/RuoYi-Cloud-springboot3/docker/nginx/html/dist`.

- [ ] **Step 1: Verify the retained frontend is Vue3**

Run:

```bash
node -e "const p=require('./ruoyi-cloud-learning/RuoYi-Cloud-Vue3-master/package.json'); console.log(p.dependencies.vue, p.scripts['build:prod'])"
```

Expected output contains:

```text
3.5.26 vite build
```

- [ ] **Step 2: Redirect Docker packaging to the Vue3 build**

Replace:

```sh
cp -r ../ruoyi-ui/dist/** ./nginx/html/dist
```

with:

```sh
cp -r ../../RuoYi-Cloud-Vue3-master/dist/** ./nginx/html/dist
```

- [ ] **Step 3: Delete the explicitly rejected Vue2 source tree**

Run:

```bash
rm -rf ruoyi-cloud-learning/RuoYi-Cloud-springboot3/ruoyi-ui
```

Expected: the directory no longer exists.

- [ ] **Step 4: Verify the cleanup**

Run:

```bash
sh -n ruoyi-cloud-learning/RuoYi-Cloud-springboot3/docker/copy.sh
test ! -d ruoyi-cloud-learning/RuoYi-Cloud-springboot3/ruoyi-ui
rg -n '\.\./ruoyi-ui/dist' ruoyi-cloud-learning/RuoYi-Cloud-springboot3/docker
```

Expected: shell syntax passes, the directory test passes, and `rg` returns no matches.

### Task 2: Replace AGENTS.md With The Learning Roadmap

**Files:**
- Modify: `AGENTS.md`
- Reference: `docs/superpowers/specs/2026-07-09-ruoyi-cloud-learning-roadmap-design.md`
- Reference: `docs/ai-tutor-mode.md`
- Reference: `docs/ai-review-mode.md`

**Interfaces:**
- Consumes: approved learning-roadmap design and actual repository paths.
- Produces: the authoritative instructions used by future AI sessions.

- [ ] **Step 1: Replace generic project metadata**

Write exact project facts:

```markdown
## 项目定位

- 这是一个长期 RuoYi-Cloud 微服务学习项目。
- 采用项目驱动路线，围绕客户关系管理系统逐步开发。
- 每周投入约 5 至 10 小时，预计完成周期为 14 至 16 周。
- 最终目标是独立完成业务开发、微服务拆分、服务治理、测试、部署和排错。
```

Document Java 17, Spring Boot 3.5.14, Spring Cloud 2025.0.2, Spring Cloud
Alibaba 2025.0.0.0, MyBatis, Vue 3.5.26, Vite 6.4.1, Element Plus 2.13.1,
MySQL, Redis, Nacos, Gateway, Sentinel, Seata, Maven, npm, and Docker Compose.

- [ ] **Step 2: Document authoritative directories and commands**

Include these source roots:

```text
ruoyi-cloud-learning/RuoYi-Cloud-springboot3/
ruoyi-cloud-learning/RuoYi-Cloud-Vue3-master/
docs/
docker-compose.local.yml
```

Include these verification commands:

```bash
docker compose -f docker-compose.local.yml up -d
docker compose -f docker-compose.local.yml ps
mvn -DskipTests package
npm install
npm run dev
npm run build:prod
```

State the working directory required for each command and explain that application
services may be launched from IDEA while learning.

- [ ] **Step 3: Add the execution and evidence rules**

Require this cycle:

```text
概念说明 -> 用户实现 -> 运行验证 -> AI 审查 -> 学习总结
```

Require build/test output, normal and failure responses, database or cache state,
key logs, and a request-chain explanation before a milestone can be checked.

- [ ] **Step 4: Add all 16 milestones**

Each `### 里程碑 NN` section must contain exactly these labels:

```markdown
- **状态：** [ ]
- **开发内容：**
- **必须理解：**
- **完成标准：**
- **验证证据：**
```

Use the approved milestone order:

```text
01 环境基线
02 请求链路
03 客户档案
04 业务校验
05 联系人管理
06 跟进记录
07 权限体系
08 平台组件
09 定时任务
10 服务拆分
11 服务调用
12 合同服务
13 缓存与幂等
14 流量治理
15 分布式事务
16 交付验收
```

Set the current milestone to `01 环境基线`; leave every status unchecked.

- [ ] **Step 5: Restore project-specific AI constraints**

Require AI to:

```text
先读取 AGENTS.md 和对应协作模式
一次只推进一个里程碑
先解释概念和原因，再给操作步骤
默认不输出完整功能代码
按前端、Gateway、Auth、业务服务、Nacos、Redis、MySQL、权限、Docker 分层排错
没有验证证据时不得标记完成
```

### Task 3: Validate The Roadmap And Cleanup

**Files:**
- Verify: `AGENTS.md`
- Verify: `ruoyi-cloud-learning/RuoYi-Cloud-springboot3/docker/copy.sh`
- Verify deletion: `ruoyi-cloud-learning/RuoYi-Cloud-springboot3/ruoyi-ui/`

**Interfaces:**
- Consumes: outputs from Tasks 1 and 2.
- Produces: a self-consistent workspace ready for milestone 01.

- [ ] **Step 1: Check for template placeholders and milestone count**

Run:

```bash
rg -n '这里填写|例如：|TB[D]|TO[D]O' AGENTS.md
rg -c '^### 里程碑 [0-9]{2}' AGENTS.md
```

Expected: no placeholder matches and milestone count is `16`.

- [ ] **Step 2: Check source-path consistency**

Run:

```bash
rg -n 'RuoYi-Cloud-springboot3/ruoyi-ui|\.\./ruoyi-ui/dist' AGENTS.md ruoyi-cloud-learning/RuoYi-Cloud-springboot3/docker/copy.sh
test ! -d ruoyi-cloud-learning/RuoYi-Cloud-springboot3/ruoyi-ui
```

Expected: no obsolete source-path matches and the deleted directory is absent.

- [ ] **Step 3: Check formatting and inspect the final diff**

Run:

```bash
git diff --check
git status --short
git diff -- AGENTS.md ruoyi-cloud-learning/RuoYi-Cloud-springboot3/docker/copy.sh
git diff --stat
```

Expected: `git diff --check` is silent; status shows the intended `AGENTS.md`,
`docker/copy.sh`, and Vue2 directory changes while preserving the user's
`CODEX_CONTEXT.md` deletion.
