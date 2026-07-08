# 若依微服务练习迁移摘要

## 学习定位

这是一个长期若依微服务学习项目，不是玉林项目业务开发。目标是通过标准 RuoYi-Cloud 项目补齐 Spring Cloud、Nacos、Gateway、前后端联调、代码生成、权限、部署和排错能力。

协作方式：
- 以教学和陪练为主，不要默认直接替我做完。
- 先解释概念，再给操作步骤。
- 每一步说明为什么这么做。
- 遇到报错时，先判断属于前端、后端、Gateway、Nacos、Nginx、Docker、数据库还是权限。
- 不要一次给太多内容，按阶段推进。
- 如果要改代码，先说明改哪里、为什么改。

## 工具分工

当前使用方式：
- VSCode：看整个项目、前端 Vue、配置文件、使用 Codex 插件提问。
- IDEA 社区版：运行 Java 后端微服务、管理 Maven、看日志。
- Docker：运行 MySQL 等基础服务。
- DBeaver：执行 SQL、查看数据库。
- 浏览器：访问若依管理后台和 Nacos。

这个组合是合理的：
- VSCode + Codex 负责理解项目和辅助开发。
- IDEA 负责 Java 服务启动和调试。
- 前端开发主要在 VSCode。

## 练习案例规划

已经约定 5 个小案例，按顺序推进：

1. 客户档案管理
   - 单表 CRUD
   - 代码生成
   - 菜单权限
   - 前后端请求链路

2. 客户跟进记录
   - 主表 + 明细表
   - 一对多关系
   - 路由传参
   - 按 customer_id 查询

3. 客户附件上传
   - el-upload
   - ruoyi-file
   - 文件路径保存
   - 文件访问

4. 客户状态定时检查
   - ruoyi-job
   - 定时任务
   - Job 调用业务 Service
   - 日志排错

5. 独立 demo 微服务
   - 新建 ruoyi-demo 服务
   - 注册到 Nacos
   - 配置 Gateway 路由
   - 前端请求新服务
   - Docker Compose 增加服务

当前正在做案例 1：客户档案管理。

## 当前项目状态

练习项目路径大概率是：

/Users/zhou/code/ruoyi_test/RuoYi-Cloud-master

目前已成功启动过这些后端服务：
- ruoyi-gateway
- ruoyi-auth
- ruoyi-system
- ruoyi-gen

前端若依后台已能访问，地址类似：

http://localhost:1024

Nacos 也已能访问。

## 已完成内容

### 1. 建表成功

已经在 MySQL 中创建业务表 `demo_customer`。

建表字段包括：

- id
- customer_name
- id_no
- mobile
- customer_type
- status
- remark
- create_by
- create_time
- update_by
- update_time

建表时曾在 DBeaver 遇到 SQL 1064，原因是多条 SQL 执行方式/语句分隔问题，不是中文注释或单引号问题。后来单独执行 CREATE TABLE 成功。

### 2. ruoyi-gen 数据库连接问题已解决

访问代码生成时曾报错：

Access denied for user 'root'@'192.168.97.1'

最终定位为：
- ruoyi-gen 从 Nacos 读取到的 MySQL 密码不对。
- 修改 Nacos 配置后，需要重启 ruoyi-gen 才生效。

这个问题归类为：
- 后端服务已启动
- Gateway 链路正常
- ruoyi-gen 访问 MySQL 失败
- 根因是 Nacos 数据库配置错误

### 3. 已导入代码生成表

在若依后台：

系统工具 -> 代码生成 -> 导入

已经成功导入 `demo_customer`。

代码生成配置大致为：

- 生成模板：单表（增删改查）
- 前端类型：Vue2 Element UI 模板
- 生成包路径：com.ruoyi.system
- 生成模块名：demo
- 生成业务名：customer
- 生成功能名：客户档案
- 上级菜单：系统工具
- 生成方式：zip 压缩包

字段配置基本保留默认，但做了一些调整：
- remark 不进列表，只用于新增/编辑。
- create_by、update_by、update_time 不作为表单字段。
- create_time 不建议用户手填，可以列表显示。
- customer_name 必填，LIKE 查询。
- customer_type 下拉框。
- status 单选框。

### 4. 已生成代码

生成目录在：

/Users/zhou/Downloads/demo_customer

生成文件包括：

后端 Java：
- main/java/com/ruoyi/system/domain/DemoCustomer.java
- main/java/com/ruoyi/system/mapper/DemoCustomerMapper.java
- main/java/com/ruoyi/system/service/IDemoCustomerService.java
- main/java/com/ruoyi/system/service/impl/DemoCustomerServiceImpl.java
- main/java/com/ruoyi/system/controller/DemoCustomerController.java

后端 XML：
- main/resources/mapper/demo/DemoCustomerMapper.xml

前端：
- vue/api/demo/customer.js
- vue/views/demo/customer/index.vue

菜单权限 SQL：
- customerMenu.sql

用户已经说“代码我已经放完了”。

## 重要理解点

### 1. mapper XML 放哪里

生成的 XML 路径是：

main/resources/mapper/demo/DemoCustomerMapper.xml

可以放到：

ruoyi-modules/ruoyi-system/src/main/resources/mapper/demo/DemoCustomerMapper.xml

不一定非要放进 mapper/system。

原因是若依 MyBatis 通常扫描：

classpath*:mapper/**/*.xml

所以 `mapper/demo` 也能被扫描到。

如果项目配置写死了 `mapper/system/*.xml`，才需要改放到 `mapper/system` 或调整扫描配置。

### 2. 生成模块名 demo 和 Gateway 路由不是一个概念

当前生成配置是：

- 生成包路径：com.ruoyi.system
- 生成模块名：demo
- 生成业务名：customer

因此前端 API 生成的是：

/demo/customer/list

但后端 Controller 在 ruoyi-system 里：

@RequestMapping("/customer")

若依微服务 Gateway 默认通常是：

/system/** -> ruoyi-system

所以实际更合理的请求链路是：

前端 /system/customer/list
-> Gateway
-> ruoyi-system
-> /customer/list
-> DemoCustomerController

因此要注意：
- 页面路径可以是 views/demo/customer
- 权限标识可以是 demo:customer:list
- 但前端 API 地址可能需要从 `/demo/customer` 改成 `/system/customer`

这是因为：
- demo 是业务分类
- system 是微服务路由前缀
- customer 是具体业务接口

待确认事项：
- 用户是否已经把 `vue/api/demo/customer.js` 里的 `/demo/customer` 改为 `/system/customer`。

### 3. customerMenu.sql 的作用

`customerMenu.sql` 不是建表 SQL，而是菜单和按钮权限 SQL。

它会往 `sys_menu` 插入：
- 客户档案菜单
- 客户档案查询权限
- 客户档案新增权限
- 客户档案修改权限
- 客户档案删除权限
- 客户档案导出权限

权限标识包括：
- demo:customer:list
- demo:customer:query
- demo:customer:add
- demo:customer:edit
- demo:customer:remove
- demo:customer:export

这些权限对应后端 Controller：

@RequiresPermissions("demo:customer:list")
@RequiresPermissions("demo:customer:add")
@RequiresPermissions("demo:customer:edit")
@RequiresPermissions("demo:customer:remove")
@RequiresPermissions("demo:customer:export")

也对应前端按钮：

v-hasPermi="['demo:customer:add']"

所以必须执行 `customerMenu.sql`，否则菜单和按钮权限不会完整出现。

如果当前用户不是超级管理员，执行后还需要去：

系统管理 -> 角色管理 -> 分配菜单权限

给当前角色勾选“客户档案”。

## Docker MySQL 执行 SQL

因为 MySQL 是 Docker 启动的，本地没有 mysql 客户端或 mysqldump。

注意：
- `mysqldump` 是导出数据库用的。
- 执行 SQL 用的是 `mysql` 客户端。

可用命令格式：

docker exec -i mysql容器名 mysql -uroot -p密码 数据库名 < "/Users/zhou/Downloads/demo_customer/customerMenu.sql"

注意：
- `-p` 后面不要有空格。
- 例如 `-p123456`，不是 `-p 123456`。
- 数据库名要用若依系统库，也就是包含 `sys_menu` 的数据库。

## 下一步建议

新会话继续时，从这里开始：

1. 确认生成代码是否已经放到正确目录。
2. 确认 `vue/api/demo/customer.js` 是否已经把 `/demo/customer` 改为 `/system/customer`。
3. 执行 `/Users/zhou/Downloads/demo_customer/customerMenu.sql`。
4. 重启 `ruoyi-system`。
5. 刷新前端或重新登录。
6. 查看左侧菜单是否出现“客户档案”。
7. 测试客户档案页面：
   - 列表查询
   - 新增
   - 修改
   - 删除
   - 导出
8. 如果报错，按链路定位：
   - 前端控制台
   - Network 请求地址
   - Gateway 是否转发
   - ruoyi-system 后端日志
   - MySQL 数据是否落库
   - 权限是否 401/403