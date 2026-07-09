# RuoYi-Cloud 微服务学习项目

这是一个基于 RuoYi-Cloud 的长期学习项目。项目采用业务驱动方式，通过逐步
开发客户关系管理系统，学习微服务业务开发、服务拆分、治理、测试、部署和
故障排查。

## 当前进度

- 当前里程碑：`01 环境基线`
- 当前目标：启动完整开发环境，打通登录链路并保存验证证据
- 完整任务和验收标准：[AGENTS.md](AGENTS.md)

## 项目目标

1. 掌握 RuoYi-Cloud 的前后端结构和请求链路。
2. 独立完成客户、联系人、跟进记录和合同等业务功能。
3. 掌握权限、文件、任务、缓存和服务调用等平台能力。
4. 完成微服务拆分、流量治理、分布式事务和容器化交付。

## 技术栈

| 范围 | 技术 |
| --- | --- |
| 后端 | Java 17、Spring Boot 3.5.14、Spring Cloud 2025.0.2、MyBatis |
| 前端 | Vue 3.5.26、Vite 6.4.1、Element Plus |
| 基础设施 | MySQL 8、Redis 7、Nacos 3.1.2、Gateway、Sentinel、Seata |
| 工具链 | Maven、npm、Docker Compose |

## 项目结构

```text
.
├── AGENTS.md
├── README.md
├── docker-compose.local.yml
├── docs/
│   ├── ai-tutor-mode.md
│   └── ai-review-mode.md
└── ruoyi-cloud-learning/
    ├── RuoYi-Cloud-springboot3/
    └── RuoYi-Cloud-Vue3-master/
```

- `RuoYi-Cloud-springboot3`：Spring Boot 3 后端主工程。
- `RuoYi-Cloud-Vue3-master`：唯一使用的 Vue3 管理后台前端。
- `docker-compose.local.yml`：本地 MySQL、Redis 和 Nacos。
- `docs`：AI 教学、审查和实施文档。

## 快速开始

### 1. 环境要求

- JDK 17
- Maven
- Node.js 与 npm
- Docker 与 Docker Compose

### 2. 启动基础设施

在项目根目录先启动 MySQL 和 Redis：

```bash
docker compose -f docker-compose.local.yml up -d rcl-mysql rcl-redis
```

首次启动时：

1. 创建 `ry-cloud` 数据库并导入
   `ruoyi-cloud-learning/RuoYi-Cloud-springboot3/sql/ry_20260417.sql`。
2. 执行
   `ruoyi-cloud-learning/RuoYi-Cloud-springboot3/sql/ry_config_20260611.sql`，
   创建并初始化 `ry-config`。
3. 核对 Nacos 配置中的 MySQL、Redis 凭据与本地 Compose 配置一致。

然后启动 Nacos：

```bash
docker compose -f docker-compose.local.yml up -d nacos
docker compose -f docker-compose.local.yml ps
```

### 3. 构建和启动后端

在 `ruoyi-cloud-learning/RuoYi-Cloud-springboot3` 执行：

```bash
mvn -DskipTests package
```

学习阶段建议在 IDEA 中启动 `ruoyi-gateway`、`ruoyi-auth`、
`ruoyi-system` 和 `ruoyi-gen`，并分别观察服务日志。

### 4. 启动前端

在 `ruoyi-cloud-learning/RuoYi-Cloud-Vue3-master` 执行：

```bash
npm install
npm run dev
```

### 5. 常用地址

| 服务 | 地址 |
| --- | --- |
| Vue3 前端 | <http://localhost> |
| Gateway | <http://localhost:8080> |
| Nacos 控制台 | <http://localhost:18080/nacos> |

## 学习路线

1. 环境基线与请求链路。
2. 客户业务 CRUD、校验和关联数据。
3. 权限、日志、文件和定时任务。
4. 拆分 Customer 与 Contract 微服务。
5. 缓存、幂等、限流、熔断和分布式事务。
6. 测试、监控、Docker Compose 与故障演练。

16 个里程碑的开发内容、完成标准和验证证据统一维护在
[AGENTS.md](AGENTS.md)。

## 文档

- [完整学习清单与项目约束](AGENTS.md)
- [AI 教学模式](docs/ai-tutor-mode.md)
- [AI 代码审查模式](docs/ai-review-mode.md)
- [学习路线设计](docs/superpowers/specs/2026-07-09-ruoyi-cloud-learning-roadmap-design.md)

## 注意事项

- 首次启动必须先初始化数据库，再启动依赖 `ry-config` 的 Nacos。
- 修改 Nacos 配置后，需要重启依赖该配置的服务。
- 停止 Compose 时不要默认增加 `-v`，避免删除学习数据库卷。
- 遇到问题时按前端、Gateway、Auth、业务服务、Nacos、Redis、MySQL 和
  Docker 的顺序收集证据；完整排错规则见 `AGENTS.md`。
