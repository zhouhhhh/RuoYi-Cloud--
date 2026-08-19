# 里程碑 03：客户档案

## 状态

- 里程碑状态：已完成
- 完成日期：2026-08-19
- 完成范围：客户表结构、代码生成、人工修正、菜单与字典、列表、分页、
  条件查询、新增、修改、详情、单条逻辑删除和批量逻辑删除
- 下一里程碑：`04 业务校验`

本文只记录可复核的实现、运行证据和学习结论。测试客户数据不作为初始化
数据保存。

## 1. 表结构

正式建表脚本：

```text
ruoyi-cloud-learning/RuoYi-Cloud-springboot3/sql/func-table/crm_customer.sql
```

`crm_customer` 包含客户编号、名称、类型、联系电话、邮箱、地址、来源、
等级、状态、负责人、部门、最后跟进时间、逻辑删除和审计字段。

已设计并验证以下索引：

```text
PRIMARY KEY (customer_id)
UNIQUE KEY uk_customer_no (customer_no)
UNIQUE KEY uk_customer_phone (phone)
KEY idx_customer_owner (owner_id)
KEY idx_customer_dept (dept_id)
KEY idx_customer_status (status)
```

客户编号和联系电话由数据库唯一索引提供最终并发约束。负责人和部门暂时只
保存逻辑关联 ID，不添加物理外键。

表及字段注释曾因 SQL 文件导入时的字符集解释错误产生乱码。重新使用
UTF-8 文件和 `mysql --default-character-set=utf8mb4` 导入后，代码生成器中
表描述恢复为“客户信息表”。

## 2. 菜单与字典

归档脚本：

```text
ruoyi-cloud-learning/RuoYi-Cloud-springboot3/sql/func-table/crm_customer_menu.sql
ruoyi-cloud-learning/RuoYi-Cloud-springboot3/sql/func-table/crm_customer_dict.sql
```

菜单脚本包含客户档案菜单以及查询、新增、修改、删除和导出权限。当前菜单
使用 `parent_id=0`，作为顶级菜单。

字典脚本包含：

```text
crm_customer_type    0 个人客户、1 企业客户
crm_customer_source  0 主动开发、1 客户推荐、2 网络推广、3 其他
crm_customer_level   A A级、B B级、C C级
crm_customer_status  0 潜在、1 跟进中、2 已成交、3 已流失
```

字典数据不固定写入自增主键 `dict_code`，避免导入其他环境时发生主键冲突。

## 3. 生成代码与人工修正

代码生成器生成了以下分层：

```text
Vue3 index.vue / view.vue
-> customer.js
-> CrmCustomerController
-> ICrmCustomerService / CrmCustomerServiceImpl
-> CrmCustomerMapper
-> CrmCustomerMapper.xml
-> crm_customer
```

生成后完成的主要人工修正：

- 列表、详情和更新 SQL 固定过滤 `del_flag='0'`。
- 单条和批量删除由物理删除改为将 `del_flag` 更新为 `2`。
- 更新 SQL 不更新 `customer_no`，前端编辑时禁用客户编号输入框。
- 新增时由 Controller 设置 `createBy`，Service 设置 `createTime`。
- 修改时由 Controller 设置 `updateBy`，Service 设置 `updateTime`。
- 新增表单默认值与数据库一致：类型 `0`、来源 `3`、等级 `C`、状态 `0`。
- 客户类型、来源、等级和状态接入字典显示。
- 增加客户来源筛选，创建时间范围包含结束日期当天，并增加稳定排序。
- 详情页补充邮箱、地址、来源、部门、备注和更新时间，时间显示到秒。
- 工具栏保留新增、批量删除和导出；行内保留详情、修改和单条删除。

## 4. 请求链路

新增客户调用链已经完成源码和运行验证：

```text
index.vue submitForm()
-> customer.js addCustomer()
-> POST /dev-api/system/customer
-> Vite 删除 /dev-api
-> Gateway 匹配 /system/**、鉴权并执行 StripPrefix=1
-> ruoyi-system POST /customer
-> CrmCustomerController.add()
-> CrmCustomerServiceImpl.insertCrmCustomer()
-> CrmCustomerMapper.insertCrmCustomer()
-> CrmCustomerMapper.xml id="insertCrmCustomer"
-> crm_customer
```

`createBy` 在 Controller 调用 Service 前设置，`createTime` 在 ServiceImpl
调用 Mapper 前设置。

## 5. CRUD 与查询证据

### 列表、分页和筛选

- 分页请求使用 `pageNum=1`、`pageSize=10`。
- 响应包含 `code`、`msg`、`rows` 和 `total`。
- 客户来源筛选请求携带 `source=3`，返回记录的 `source="3"`。
- 点击重置后，请求不再携带 `source`。

### 详情

- `GET /dev-api/system/customer/{customerId}` 返回业务 `code=200`。
- 响应包含客户业务字段、负责人、部门、逻辑删除和审计字段。
- 数据库列值为 `NULL` 时，响应字段为 `null`；这不表示 Mapper 漏查字段。
- 来源等枚举值在详情页通过字典转换为业务标签。

### 新增

测试请求：

```text
POST /dev-api/system/customer
customerNo=CUST-20260819-001
source=1
level=B
status=0
```

响应为业务 `code=200`、`msg=操作成功`。数据库生成 `customer_id=2`，
`create_by=admin`、`create_time=2026-08-19 11:42:04`，首次新增时
`update_by` 和 `update_time` 为 `NULL`，`del_flag=0`。

### 修改

- `PUT /dev-api/system/customer` 携带 `customerId`，响应业务 `code=200`。
- 客户编号保持不变，其他修改字段正确更新。
- `create_by/create_time` 保持不变，`update_by/update_time` 产生新值。
- 修改后详情页能够显示来源字典、完整时间和补充字段。

### 单条逻辑删除

- `DELETE /dev-api/system/customer/2` 返回业务 `code=200`。
- 页面列表中客户消失。
- 数据库记录仍存在，`customer_id=2` 的 `del_flag=2`。

### 批量逻辑删除

- 请求路径为 `DELETE /dev-api/system/customer/4,3`。
- 响应为业务 `code=200`、`msg=操作成功`。
- 数据库中 `customer_id=3` 和 `customer_id=4` 均保留，`del_flag=2`。

## 6. 构建验证

前端生产构建：

```text
npm run build:prod
结果：成功
```

后端构建：

```text
mvn -pl ruoyi-modules/ruoyi-system -am -DskipTests package
结果：13 个 Reactor 模块全部 SUCCESS，最终 BUILD SUCCESS
```

后端命令使用了 `-DskipTests`，因此只证明编译和打包成功，不表示测试用例
已经执行或通过。

## 7. 当前边界

以下内容不属于里程碑 03，留到里程碑 04：

- 手机号必填和格式的后端参数校验。
- 重复手机号、重复客户编号的明确业务错误提示。
- 数据库唯一约束异常的统一转换。
- 客户状态合法流转规则。
- 相关正常与失败自动化测试。
