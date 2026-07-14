# AGENTS.md

本文件是当前 RuoYi-Cloud 学习项目的事实来源。AI 和学习者开始任务前应先阅读
本文件，再按当前里程碑推进。

## 项目定位

- 这是一个长期 RuoYi-Cloud 微服务学习项目。
- 采用项目驱动路线，围绕客户关系管理系统逐步开发。
- 每周投入约 5 至 10 小时，预计完成周期为 14 至 16 周。
- 最终目标是独立完成业务开发、微服务拆分、服务治理、测试、部署和排错。
- 学习重点是理解与独立实现，不以复制完整代码为目标。

## 学习路线

采用“先业务闭环，再拆微服务，最后做分布式治理”的顺序：

1. 建立可重复启动、可观察、可排错的运行基线。
2. 在 `ruoyi-system` 中完成客户关系管理业务闭环。
3. 接入权限、日志、文件、定时任务等 RuoYi 平台能力。
4. 将客户业务迁移为独立的 `ruoyi-customer` 微服务。
5. 增加 `ruoyi-contract`，形成真实的跨服务协作场景。
6. 完成缓存、幂等、限流、熔断、分布式事务、监控和容器化交付。

不得为了提前接触微服务而跳过业务闭环。后续任务依赖前一里程碑的验证结果。

## 技术栈

### 后端

- Java 17
- Spring Boot 3.5.14
- Spring Cloud 2025.0.2
- Spring Cloud Alibaba 2025.0.0.0
- RuoYi-Cloud 3.6.8
- MyBatis、Mapper XML、PageHelper
- Maven

### 前端

- Vue 3.5.26
- Vite 6.4.1
- Element Plus 2.13.1
- Pinia、Vue Router、Axios
- npm

### 基础设施

- MySQL 8.0
- Redis 7
- Nacos 3.1.2
- Spring Cloud Gateway
- Sentinel
- Seata
- Docker Compose

## 目录结构

```text
AGENTS.md
  项目事实、开发约束、学习路线和当前进度。

docker-compose.local.yml
  部署到远程 Docker 主机的 MySQL、Redis、Nacos 基础设施配置。

docs/
  AI 教学模式和代码审查模式。

ruoyi-cloud-learning/RuoYi-Cloud-springboot3/
  Spring Boot 3 后端主工程。

ruoyi-cloud-learning/RuoYi-Cloud-springboot3/ruoyi-gateway/
  统一入口、动态路由、鉴权过滤和流量治理。

ruoyi-cloud-learning/RuoYi-Cloud-springboot3/ruoyi-auth/
  登录、退出、令牌签发和认证流程。

ruoyi-cloud-learning/RuoYi-Cloud-springboot3/ruoyi-modules/ruoyi-system/
  用户、部门、角色、菜单，以及学习前半程的 CRM 业务。

ruoyi-cloud-learning/RuoYi-Cloud-springboot3/ruoyi-modules/ruoyi-gen/
  数据表导入和代码生成。

ruoyi-cloud-learning/RuoYi-Cloud-springboot3/ruoyi-modules/ruoyi-file/
  文件上传和访问。

ruoyi-cloud-learning/RuoYi-Cloud-springboot3/ruoyi-modules/ruoyi-job/
  Quartz 定时任务。

ruoyi-cloud-learning/RuoYi-Cloud-springboot3/ruoyi-common/
  通用核心、安全、日志、Redis、数据权限和 Seata 支持。

ruoyi-cloud-learning/RuoYi-Cloud-Vue3-master/
  唯一使用的 Vue3 管理后台前端。
```

## 当前运行拓扑

```text
本机
├── Vue3 前端       3000
├── ruoyi-gateway    8080
├── ruoyi-auth       9200
├── ruoyi-system     9201
└── ruoyi-gen        9202
        │
        │ 局域网
        ▼
远程 Docker 主机 192.168.106.199
├── MySQL             3306
├── Redis             6379
├── Nacos             8848
└── Nacos 控制台     18080
```

当前 `ruoyi-gateway`、`ruoyi-auth`、`ruoyi-system`、`ruoyi-gen`
已指向 `192.168.106.199:8848`。`ruoyi-job`、`ruoyi-file` 和
`ruoyi-monitor` 仍指向 `127.0.0.1:8848`，启用这些服务前必须先完成
Nacos 地址迁移。

## 常用命令

### 远程基础设施

在远程 Docker 主机的部署目录执行：

```bash
docker compose -f docker-compose.local.yml up -d
docker compose -f docker-compose.local.yml ps
docker compose -f docker-compose.local.yml logs -f nacos
docker compose -f docker-compose.local.yml down
```

停止服务时默认不要增加 `-v`，避免误删学习数据库卷。

从本机检查远程基础设施端口：

```bash
nc -vz 192.168.106.199 3306
nc -vz 192.168.106.199 6379
nc -vz 192.168.106.199 8848
nc -vz 192.168.106.199 9848
nc -vz 192.168.106.199 9849
nc -vz 192.168.106.199 18080
```

### 后端

在 `ruoyi-cloud-learning/RuoYi-Cloud-springboot3` 执行：

```bash
mvn -DskipTests package
mvn test
```

学习阶段可在 IDEA 中分别启动以下服务并观察日志：

```text
ruoyi-gateway   8080
ruoyi-auth      9200
ruoyi-system    9201
ruoyi-gen       9202
ruoyi-job       9203
ruoyi-file      9300
```

服务端口以各模块 `bootstrap.yml` 为准。应用配置从 Nacos 的
`application-dev.yml` 和 `<service-name>-dev.yml` 读取。

### 前端

在 `ruoyi-cloud-learning/RuoYi-Cloud-Vue3-master` 执行：

```bash
npm install
npm run dev
npm run build:prod
```

当前 Vite 开发端口为 `3000`，`/dev-api` 代理到
`http://localhost:8080`。

### 当前访问点

```text
Vue3 前端       http://localhost:3000
Gateway         http://localhost:8080
Nacos 控制台    http://192.168.106.199:18080/nacos
Nacos 服务端口  192.168.106.199:8848
MySQL           192.168.106.199:3306
Redis           192.168.106.199:6379
```

## 请求链路

基础阶段：

```text
Vue3:3000 -> Vite 代理 -> Gateway:8080 -> Auth/System -> MyBatis
                                                |              |
                                                |              -> 远程 MySQL
                                                -> 远程 Redis

本机 Java 服务 -> 远程 Nacos（注册发现与配置中心）
```

微服务拆分后：

```text
Vue3 -> Gateway -> Customer/Contract -> OpenFeign -> System/其他服务
```

服务只能通过明确接口协作，不允许直接读取其他服务的数据库。

## 开发约束

- 不要随意重构无关文件。
- 不要引入当前里程碑不需要的新依赖。
- 修改前先理解现有模块、配置来源和调用链。
- 保持代码风格、响应结构、异常体系和权限写法与项目一致。
- 数据库变更必须保留可重复执行或有明确版本的 SQL。
- 新接口必须考虑参数校验、权限、异常响应和必要的测试。
- 服务拆分后，每个业务服务拥有自己的数据表，不跨库直连。
- 不得把密码、令牌或本机绝对路径提交到业务配置。
- 远程 MySQL、Redis、Nacos 端口只允许可信局域网或本机 IP 访问，不对公网暴露。
- 涉及删除数据、删除卷、权限放开或安全白名单时，必须先说明风险。
- 对不确定的事实先通过源码、配置或运行结果验证，不得假装确定。

## 学习执行规则

每个里程碑按以下循环完成：

```text
概念说明 -> 用户实现 -> 运行验证 -> AI 审查 -> 学习总结
```

执行要求：

- 一次只推进一个里程碑。
- 每次先完成一个 30 至 90 分钟的小闭环。
- AI 先说明“做什么、为什么、如何验证”，再让学习者动手。
- 学习者提交代码或现象后，AI 再进行审查或排错。
- 未达到完成标准时，不勾选状态，不进入依赖它的下一里程碑。
- 代码能运行不等于掌握，学习者必须能解释关键请求链路和设计原因。

里程碑完成至少需要以下证据：

- Maven、前端构建或相关测试结果。
- 一个正常请求和至少一个关键失败请求的响应。
- 对应的数据库或缓存状态。
- Gateway 和业务服务的关键日志。
- 学习者对本次请求链路、根因或实现原因的简短说明。

## 当前任务

当前里程碑：`02 请求链路`。

开始时只完成验证码请求链路这一小闭环：

1. 在浏览器 Network 中重新请求 `/dev-api/code`。
2. 从 `src/views/login.vue` 定位到 `src/api/login.js` 的 `getCodeImg()`。
3. 说明 `/dev-api` 如何经过 Vite 代理到 Gateway。
4. 定位 Gateway 中 `/code` 的路由函数、Handler 和 Service。
5. 在远程 Redis（`192.168.106.199:6379`）中观察
   `captcha_codes:*` 的 Key 与 TTL。
6. 使用源码、Network、Redis 和必要日志画出验证码请求链路。

本轮只跟踪验证码生成，不同时展开登录、Token、用户信息和动态路由。

## 开发清单

### 里程碑 01 环境基线

- **状态：** [x]
- **开发内容：** 在远程 Docker 主机启动 MySQL、Redis、Nacos，在本机启动 Gateway、Auth、System、Gen 和 Vue3 前端；初始化数据库并统一远程连接配置。
- **必须理解：** 容器健康检查、服务启动顺序、Nacos 注册中心与配置中心、Bootstrap 配置加载。
- **完成标准：** 前端可登录；核心服务出现在 Nacos；后端无数据库、Redis 或配置加载错误。
- **验证证据：** 远程 `docker compose ps`、Nacos 服务列表、本机到远程端口的连通结果、登录 Network 请求、Gateway 与 Auth/System 日志。

### 里程碑 02 请求链路

- **状态：** [ ]
- **开发内容：** 跟踪验证码、登录、获取用户信息、获取路由和系统列表查询的完整链路。
- **必须理解：** Vite 代理、Gateway 路由与 `StripPrefix`、Token、Redis 会话、统一响应和分页。
- **完成标准：** 能从前端请求定位到 Controller、Service、Mapper 和 SQL，并能解释 401、403、404、500 的所属层。
- **验证证据：** 一张请求链路图、关键断点或日志、一次正常请求和一次无 Token 请求。

### 里程碑 03 客户档案

- **状态：** [ ]
- **开发内容：** 设计 `crm_customer`，使用代码生成器建立客户列表、新增、编辑、删除和详情，再人工修正生成结果。
- **必须理解：** 表结构、代码生成配置、Controller/Service/Mapper/XML 分层、前端 API 与页面职责。
- **完成标准：** 客户 CRUD、分页和条件查询可用；生成代码放置正确；字段和索引设计有说明。
- **验证证据：** 建表 SQL、五类接口响应、页面操作结果、数据库记录和请求链路说明。

### 里程碑 04 业务校验

- **状态：** [ ]
- **开发内容：** 增加手机号唯一、必填和格式校验、客户状态流转规则及统一业务异常。
- **必须理解：** 前后端校验边界、Bean Validation、数据库唯一索引、竞态条件、异常转换。
- **完成标准：** 非法参数、重复手机号和非法状态流转均被后端稳定拒绝并返回明确错误。
- **验证证据：** 正常与失败测试、唯一索引、异常响应、数据库未产生脏数据的证明。

### 里程碑 05 联系人管理

- **状态：** [ ]
- **开发内容：** 设计 `crm_contact`，实现客户与联系人一对多关系、联系人 CRUD 和客户详情中的联系人列表。
- **必须理解：** 外键与逻辑关联、主从页面、路由参数、关联查询、删除策略。
- **完成标准：** 可从客户详情维护联系人；不存在的客户不能新增联系人；删除客户行为符合约定策略。
- **验证证据：** 表关系说明、关联接口响应、页面流程、非法客户 ID 测试。

### 里程碑 06 跟进记录

- **状态：** [ ]
- **开发内容：** 设计 `crm_follow_record`，实现跟进新增、时间线查询，并同步客户最近跟进时间。
- **必须理解：** 聚合边界、本地事务、事务回滚、按客户分页、时间排序。
- **完成标准：** 新增跟进与更新客户时间要么同时成功，要么同时回滚；时间线顺序正确。
- **验证证据：** 事务代码审查、成功与回滚测试、两张表的数据、时间线页面。

### 里程碑 07 权限体系

- **状态：** [ ]
- **开发内容：** 配置 CRM 菜单、按钮权限、角色授权，并按部门或负责人限制客户数据范围。
- **必须理解：** `sys_menu`、权限标识、`@RequiresPermissions`、前端权限指令、数据权限 SQL。
- **完成标准：** 管理员、销售和只读角色看到不同菜单、按钮和数据；越权请求被后端拒绝。
- **验证证据：** 菜单 SQL、三类账号测试、403 响应、数据范围查询结果。

### 里程碑 08 平台组件

- **状态：** [ ]
- **开发内容：** 完成客户 Excel 导入导出、操作日志、附件上传、附件元数据保存和文件访问。
- **必须理解：** Excel 注解与校验、异步文件请求、`ruoyi-file` 路由、文件 URL 与业务记录的区别。
- **完成标准：** 导入失败可定位到具体数据；导出内容正确；附件可上传、查询和访问；敏感操作有日志。
- **验证证据：** 导入成功与失败样例、导出文件、附件记录、文件响应、操作日志。

### 里程碑 09 定时任务

- **状态：** [ ]
- **开发内容：** 使用 `ruoyi-job` 定期识别长期未跟进客户，更新客户状态并记录执行结果。
- **必须理解：** Quartz、Cron、任务幂等、批量更新、任务日志、重复执行风险。
- **完成标准：** 任务可手动触发和按计划执行；重复执行不产生错误状态；失败记录可排查。
- **验证证据：** 任务配置、执行前后数据、任务日志、重复执行测试和失败测试。

### 里程碑 10 服务拆分

- **状态：** [ ]
- **开发内容：** 新建 `ruoyi-customer`，迁移 CRM 后端代码和数据配置，接入 Maven、Nacos 与 Gateway。
- **必须理解：** 服务边界、独立配置、服务注册、路由前缀、数据所有权、迁移步骤。
- **完成标准：** CRM 请求只进入 Customer 服务；System 不再承载 CRM 代码；迁移前后功能一致。
- **验证证据：** Nacos 实例、Gateway 路由、服务日志、回归结果、System 中无 CRM 残留的检查。

### 里程碑 11 服务调用

- **状态：** [ ]
- **开发内容：** 使用 OpenFeign 从 System 查询负责人和部门信息，补充超时、异常映射和降级行为。
- **必须理解：** 服务发现、Feign 接口契约、内部鉴权、超时、重试边界、错误传播。
- **完成标准：** 正常调用返回负责人信息；System 超时或不可用时 Customer 返回可预期响应，不无限重试。
- **验证证据：** Feign 契约、正常响应、超时与停服测试、两侧关联日志。

### 里程碑 12 合同服务

- **状态：** [ ]
- **开发内容：** 新建轻量 `ruoyi-contract`，实现合同创建、查询和客户关联，形成第二个业务微服务。
- **必须理解：** 跨服务数据引用、接口契约、数据库隔离、最终一致性与强一致性的选择。
- **完成标准：** Contract 拥有独立数据表；只通过 Customer 接口校验客户；不能跨库查询客户表。
- **验证证据：** 服务和表结构、调用日志、有效与无效客户测试、服务边界说明。

### 里程碑 13 缓存与幂等

- **状态：** [ ]
- **开发内容：** 缓存客户摘要，处理更新失效、空值缓存和热点数据；为合同创建增加幂等控制。
- **必须理解：** Cache Aside、TTL、缓存穿透、缓存一致性、幂等键、锁的粒度与过期。
- **完成标准：** 缓存命中可观测；更新后不会长期读取旧值；重复提交只产生一份合同。
- **验证证据：** Redis Key 与 TTL、命中日志、更新一致性测试、并发重复请求结果。

### 里程碑 14 流量治理

- **状态：** [ ]
- **开发内容：** 配置 Gateway 限流和 Sentinel 规则，为跨服务调用增加熔断与降级并执行故障演练。
- **必须理解：** QPS、并发数、限流维度、熔断状态、降级返回、规则持久化。
- **完成标准：** 超限请求得到明确响应；下游变慢或失败时触发熔断；恢复后调用自动回归。
- **验证证据：** Nacos/Sentinel 规则、压测或批量请求结果、熔断前后日志、恢复测试。

### 里程碑 15 分布式事务

- **状态：** [ ]
- **开发内容：** 创建合同时同步更新客户成交状态，使用 Seata 验证全局提交和异常回滚。
- **必须理解：** 全局事务、分支事务、XID、AT 模式、`undo_log`、事务边界和补偿方案。
- **完成标准：** 正常场景两侧提交；任一服务失败时两侧回滚；能够说明何时不应使用分布式事务。
- **验证证据：** Seata 配置、XID 日志、提交数据、故障注入后的回滚数据和事务说明。

### 里程碑 16 交付验收

- **状态：** [ ]
- **开发内容：** 补充核心测试、监控、Docker Compose、环境配置隔离、部署文档和综合故障演练。
- **必须理解：** 测试分层、健康检查、日志与指标、配置外置、镜像构建、启动依赖和回滚。
- **完成标准：** 新环境按文档可启动；核心流程通过；任意停止一个依赖后能定位故障并恢复。
- **验证证据：** 测试报告、容器状态、监控截图或数据、部署记录、故障演练报告。

## 排错顺序

遇到问题时按调用链收集证据，不先猜原因：

1. 前端页面、Console、Network、请求参数和响应。
2. Vite 代理及 Gateway 路由、过滤器、鉴权和限流。
3. Auth、Token、Redis 会话和权限上下文。
4. Customer、Contract、System 等业务服务日志。
5. Nacos 服务实例、Data ID、Group 和实际配置内容。
6. Redis 连接、Key、TTL 和序列化。
7. MySQL 连接、库名、SQL、索引和事务。
8. Docker 容器健康、端口、挂载、网络和资源。

排错记录至少包含：

```text
现象 -> 所属层 -> 证据 -> 根因 -> 修改 -> 验证结果
```

## AI 协作方式

- 需求拆解、方案设计和学习指导遵守 `docs/ai-tutor-mode.md`。
- 代码审查、问题分析和修改建议遵守 `docs/ai-review-mode.md`。
- AI 开始工作前必须先读取本文件和对应协作模式。
- AI 一次只给当前里程碑所需内容，不一次展开全部后续知识。
- AI 默认提供方案、伪代码、接口签名、关键片段和调试步骤。
- 除非学习者明确要求“直接实现”或“给完整代码”，AI 不直接完成整个功能。
- 学习者给出实现后，AI 应优先审查错误、风险、回归和缺失测试。
- 没有可复核的验证证据时，AI 不得将任务标记为完成。
