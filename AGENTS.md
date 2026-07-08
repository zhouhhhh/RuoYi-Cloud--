# AGENTS.md

本文件用于说明当前项目的基本信息、技术栈、目录结构、启动方式和开发约束。

AI 在协助本项目时，应优先遵守本文件中的项目事实和工程约束。

## 项目目标

- 这是一个长期 RuoYi-Cloud 微服务学习项目，不是玉林项目业务开发。
- 项目目标是通过标准 RuoYi-Cloud 项目补齐 Spring Cloud、Nacos、Gateway、前后端联调、代码生成、权限、部署和排错能力。
- 项目同时用于练习独立开发能力，因此 AI 不应默认直接代写完整功能。
- 当前正在做案例 1：客户档案管理。

## 技术栈

- 后端：Java、Spring Boot、Spring Cloud、RuoYi-Cloud 微服务体系。
- 注册配置：Nacos。
- 网关：Spring Cloud Gateway，RuoYi 默认网关链路。
- 前端：Vue2、Element UI、RuoYi 管理后台前端。
- 数据库：MySQL。
- ORM / SQL：MyBatis、Mapper XML。
- 代码生成：RuoYi 代码生成模块 `ruoyi-gen`。
- 基础服务：Docker 运行 MySQL 等服务。
- 常用工具：VSCode、IDEA 社区版、DBeaver、浏览器。

## 目录结构

当前工作区主要保存 AI 协作上下文和规则。

```text
CODEX_CONTEXT.md
  当前学习项目的上下文、已完成内容、问题定位记录和下一步建议。

AGENTS.md
  项目事实、技术栈、启动方式和开发约束。

docs/
  AI 协作模式规则。

docs/ai-tutor-mode.md
  需求拆解、方案设计、学习指导规则。

docs/ai-review-mode.md
  代码审查、问题分析、修改建议规则。
```

RuoYi-Cloud 练习项目路径大概率是：

```text
/Users/zhou/code/ruoyi_test/RuoYi-Cloud-master
```

如果实际代码目录与这里不同，请以用户当前打开或明确指定的项目目录为准。

## 当前项目状态

目前已成功启动过这些后端服务：

- `ruoyi-gateway`
- `ruoyi-auth`
- `ruoyi-system`
- `ruoyi-gen`

前端若依后台已能访问，地址类似：

```text
http://localhost:1024
```

Nacos 也已能访问。

已经完成：

- 在 MySQL 中创建业务表 `demo_customer`。
- 修复 `ruoyi-gen` 从 Nacos 读取到错误 MySQL 密码导致的数据库连接问题。
- 在若依后台代码生成中导入 `demo_customer`。
- 生成客户档案相关后端、Mapper XML、前端页面、API 文件和菜单权限 SQL。
- 用户已经说明生成代码已经放完。

## 关键项目事实

生成配置大致为：

- 生成模板：单表（增删改查）
- 前端类型：Vue2 Element UI 模板
- 生成包路径：`com.ruoyi.system`
- 生成模块名：`demo`
- 生成业务名：`customer`
- 生成功能名：客户档案
- 生成方式：zip 压缩包

需要特别注意：

- `mapper/demo/DemoCustomerMapper.xml` 可以放到 `ruoyi-modules/ruoyi-system/src/main/resources/mapper/demo/DemoCustomerMapper.xml`。
- 若依 MyBatis 通常扫描 `classpath*:mapper/**/*.xml`，所以 `mapper/demo` 一般能被扫描到。
- 生成模块名 `demo` 和 Gateway 路由前缀不是一个概念。
- Controller 在 `ruoyi-system` 中时，前端 API 更可能需要从 `/demo/customer` 改为 `/system/customer`。
- 页面路径可以是 `views/demo/customer`。
- 权限标识可以是 `demo:customer:list`、`demo:customer:add` 等。
- `customerMenu.sql` 是菜单和按钮权限 SQL，不是建表 SQL。
- 执行菜单 SQL 后，如果当前用户不是超级管理员，还需要给角色分配菜单权限。

## 启动方式

后端服务通常在 IDEA 社区版中启动和查看日志。

需要重点关注的服务：

```text
ruoyi-gateway
ruoyi-auth
ruoyi-system
ruoyi-gen
```

前端通常在 VSCode 中运行。具体命令以实际 RuoYi-Cloud 前端项目中的 `package.json` 为准，常见形式是：

```bash
npm install
npm run dev
```

Docker 用于运行 MySQL 等基础服务。

执行 SQL 时，本地如果没有 `mysql` 客户端，可以通过 Docker 容器执行：

```bash
docker exec -i mysql容器名 mysql -uroot -p密码 数据库名 < "/Users/zhou/Downloads/demo_customer/customerMenu.sql"
```

注意：

- `-p` 后面不要有空格，例如 `-p123456`。
- 数据库名要用包含 `sys_menu` 的若依系统库。

## 开发约束

请遵守以下约束：

- 不要随意重构无关文件。
- 不要引入不必要的新依赖。
- 修改前先理解现有结构。
- 保持代码风格与项目一致。
- 涉及权限、文件系统、浏览器 API、系统 API 时，要说明风险。
- 对不确定的地方要明确标注，不要假装确定。
- 以教学和陪练为主，不要默认直接替用户做完。
- 先解释概念，再给操作步骤。
- 每一步说明为什么这么做。
- 遇到报错时，先判断属于前端、后端、Gateway、Nacos、Nginx、Docker、数据库还是权限。
- 不要一次给太多内容，按阶段推进。
- 如果要改代码，先说明改哪里、为什么改。

## AI 协作方式

本项目的 AI 协作分为多个模式，具体规则放在 `docs` 目录下：

- `docs/ai-tutor-mode.md`：用于需求拆解、方案设计、学习指导。
- `docs/ai-review-mode.md`：用于代码审查、问题分析、修改建议。

如果当前对话是需求设计，请参考 `docs/ai-tutor-mode.md`。

如果当前对话是代码审查，请参考 `docs/ai-review-mode.md`。

除非用户明确要求直接实现，否则 AI 不应默认输出完整代码。
