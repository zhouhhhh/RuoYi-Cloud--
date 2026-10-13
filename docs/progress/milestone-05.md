# 里程碑 05：联系人管理

## 状态

- 里程碑状态：已完成，按当前业务规则验收
- 完成日期：2026-10-13
- 完成范围：联系人表设计、联系人增删改查、客户详情联系人列表、客户有效性
  校验、联系人逻辑删除，以及删除客户前的联系人约束
- 下一里程碑：`06 跟进记录`，待开始

本文依据学习者在本学习任务中提交的请求、响应、数据库查询、页面截图、日志、
构建输出，以及 AI 对当前源码的只读审查归档。未独立留存的原始输出会明确
注明，不把推断写成实测事实。本次归档没有删除或修改数据库测试数据，也没有
重新运行接口或构建。

## 1. 最终表关系与业务规则

`crm_customer` 与 `crm_contact` 是一对多关系：一名联系人只属于一个客户，
一个客户可以暂时没有联系人，也可以有多名联系人，因此联系人一侧的基数为
`0..N`。

`crm_contact.customer_id` 与 `crm_customer.customer_id` 建立逻辑关联。当前表结构
没有创建数据库 `FOREIGN KEY` 约束，客户有效性由 Service 在写入前校验。最终
规则如下：

- `customer_id` 和 `contact_name` 必填；姓名不能是空字符串或纯空格。
- 联系人电话可以为空，不设置唯一约束；客户主手机号与联系人电话分别维护。
- 不存在或已逻辑删除的客户不能新增联系人。
- 普通修改不能把联系人转移给其他客户；修改 SQL 不更新 `customer_id`。
- 联系人删除采用逻辑删除，将 `del_flag` 更新为 `2`。
- 联系人列表和详情只读取 `del_flag='0'` 的记录。
- 客户仍有未删除联系人时，单条和批量客户删除均被拒绝；系统不会级联删除联系人。

建表 SQL 保存在
`ruoyi-cloud-learning/RuoYi-Cloud-springboot3/sql/func-table/crm_contact.sql`。
表使用 `contact_id` 作为主键，并建立联合索引
`idx_contact_customer (customer_id, del_flag)`，支持按客户筛选未删除联系人。

## 2. 后端实现与职责边界

联系人业务仍位于 `ruoyi-modules/ruoyi-system`，没有提前拆分微服务或引入新依赖。

### 参数校验与审计字段

`CrmContact` 在 getter 上声明：

| 属性 | 约束 | 消息 |
|---|---|---|
| `customerId` | `@NotNull` | 所属客户不能为空 |
| `contactName` | `@NotBlank` | 联系人姓名不能为空 |

Controller 的新增和修改请求均使用 `@Validated @RequestBody`。新增时由后端写入
当前登录用户和创建时间；修改时由后端写入当前登录用户和更新时间。请求中伪造
的 `delFlag`、`createBy` 和 `createTime` 不进入联系人修改 SQL。

同一轮还为 `CrmCustomer.customerName` 补充了 `@NotBlank`，新增和修改传纯空格
均返回“客户名称不能为空”。

### 新增联系人

调用链为：

```text
Vue 客户详情 submitContact
-> addContact
-> POST /system/contact
-> Gateway 的 system 路由
-> CrmContactController.add
-> CrmContactServiceImpl.insertCrmContact
-> CrmCustomerMapper.selectCrmCustomerByCustomerId
-> CrmContactMapper.insertCrmContact
-> MySQL
```

客户存在且 `del_flag='0'` 时才执行联系人 INSERT；否则 Service 抛出
`ServiceException("客户不存在或已删除，不能新增联系人")`。

### 查询与修改联系人

列表按 `customer_id` 筛选，并固定增加 `del_flag='0'`，按
`create_time desc, contact_id desc` 排序。详情查询同样过滤逻辑删除记录。

修改前先查询联系人是否存在且未删除，用于给出“联系人不存在或已删除，不能修改”
的明确错误。最终 UPDATE 再次使用 `contact_id` 和 `del_flag='0'` 作为条件，防止
查询后发生并发删除时写入已删除记录。修改 SQL 只更新姓名、电话、备注和更新审计
字段，不更新 `customer_id`、`del_flag` 或创建审计字段。

### 删除策略

联系人删除 SQL 将 `del_flag` 更新为 `2`，数据行继续保留。列表和详情查询过滤
`del_flag='0'`，所以已删除联系人不会再显示。

删除客户前，`CrmCustomerServiceImpl` 调用统一的 `checkNoActiveContacts`。
Mapper 接收客户 ID 数组并用 `IN` 一次统计未删除联系人；单条删除将一个 ID
包装成数组，从而与批量删除复用同一检查逻辑。统计结果大于零时返回：

```text
所选客户存在未删除联系人，请先处理联系人
```

## 3. 客户详情页面

客户详情抽屉中增加联系人分页列表及新增、修改、删除操作：

- 每次打开客户详情都重置联系人列表、页码和当前客户 ID，避免切换客户后残留数据。
- 列表请求始终携带当前 `customerId`，读取响应的 `rows` 和 `total`。
- 新增弹窗锁定当前客户，客户信息不可编辑；姓名前端校验失败时不发送 POST。
- 新增成功后回到第一页并刷新；修改成功后保留当前页并刷新。
- 修改按钮先 GET 最新联系人；若联系人已被逻辑删除，提示并刷新列表，不打开弹窗。
- 修改弹窗继续锁定所属客户，后端 SQL 也忽略伪造的 `customerId`。
- 删除需要二次确认；取消时不发送 DELETE。
- 删除当前页唯一一条记录且页码大于 1 时，先将 `pageNum` 减一，再只发起一次
  列表查询，避免先请求空页后由分页组件再次回到上一页。

按钮分别使用 `system:contact:add`、`system:contact:edit` 和
`system:contact:remove` 权限标识。角色授权和非管理员越权验证留到里程碑 07。

## 4. 运行验证

### 新增及参数校验

- 不传 `customerId`，返回业务 `code=500`、`所属客户不能为空`。
- `contactName` 传纯空格，返回业务 `code=500`、`联系人姓名不能为空`。
- 使用有效客户 `1` 新增联系人成功；即使请求伪造 `delFlag=2`、
  `createBy=fake`，数据库仍保存 `del_flag=0`、`create_by=admin`。
- 客户详情新增 `M05详情新增` 成功，请求体携带 `customerId=1`；数据库生成
  联系人 `11`，`del_flag=0`、`create_by=admin`，列表自动回到第一页并显示新记录。
- 切换到客户 `14/test3` 后，新增弹窗显示新客户且没有残留上一个客户的数据；
  姓名留空只出现前端提示，Network 没有 POST。

### 非法客户

- 使用不存在的客户 `29` 新增，返回
  `客户不存在或已删除，不能新增联系人`，联系人总数保持不变。
- 使用 `del_flag=2` 的客户 `4` 新增，返回同一业务错误，联系人总数保持不变。
- System 日志记录了 `CrmContactServiceImpl.insertCrmContact` 抛出的业务异常，
  失败请求没有产生联系人 INSERT。

### 修改联系人

- 联系人 `7` 正常修改成功；数据库姓名、电话和备注更新，`update_by=admin`，
  `update_time=2026-09-09 19:43:07`。
- 修改请求伪造其他 `customerId`、`delFlag=2`、`createBy=fake` 和历史时间，
  数据库中的 `customer_id=1`、`del_flag=0`、创建审计字段均保持原值。
- 姓名传纯空格返回“联系人姓名不能为空”，数据库内容和更新时间保持不变。
- 将联系人 `7` 逻辑删除后再次 PUT，返回
  `联系人不存在或已删除，不能修改`，数据库记录没有变化。
- 页面保留联系人 `12` 的旧行后在数据库将其置为 `del_flag=2`，再点击修改：
  GET 详情返回空数据，页面提示“联系人不存在或已删除”，随后列表从 3 条刷新为
  2 条，弹窗没有打开。

### 客户删除约束

- 客户 `1` 有 3 名未删除联系人。单条 DELETE 返回
  `所选客户存在未删除联系人，请先处理联系人`；客户仍为 `del_flag=0`，联系人
  数量不变。
- 批量删除客户 `1` 和无联系人的临时客户 `15` 时，整个批次被拒绝，两名客户均
  保持未删除。
- 单独删除无联系人的客户 `15` 成功，数据库 `del_flag` 变为 `2`。

### 联系人删除与分页

- 删除确认框选择取消时，Network 没有 DELETE。
- 删除联系人 `9` 请求为 `DELETE /system/contact/9`，返回业务 `code=200`；
  页面联系人数量由 2 条变为 1 条，数据库记录保留且 `del_flag=2`。
- 在第二页只有一条联系人时删除，页面先把 `pageNum` 从 2 调整为 1；Network
  出现一次 DELETE 和一次 `pageNum=1` 的列表 GET，最终回到第一页。

## 5. 构建与验证边界

学习者提交的最终构建结果：

| 日期 | 命令 | 结果 |
|---|---|---|
| 2026-10-13 | `npm run build:prod` | Vite 生产构建成功，共转换 1985 个模块 |
| 2026-10-13 17:05:42 +08:00 | `mvn -pl ruoyi-modules/ruoyi-system -am -DskipTests package` | 13 个 Reactor 模块全部 SUCCESS，最终 BUILD SUCCESS |

- Maven 使用 `-DskipTests`，日志明确显示测试被跳过；本次完成的是源码审查、
  手工功能验证和打包验收，不宣称自动化测试通过。
- 本轮没有执行真正的并发修改或删除测试；UPDATE 的 `del_flag='0'` 条件由源码
  审查确认，已删除数据的普通失败路径通过手工请求验证。
- 表结构没有数据库外键，绕过应用直接写库仍可能制造无效 `customer_id`；当前
  数据完整性由业务接口中的 Service 校验维护。
- 本轮复用里程碑 02、03 已确认的 Vite、Gateway 和 System 路由，没有新增
  Gateway 规则；不宣称为每个联系人请求单独留存了 Gateway 日志。
- 菜单和按钮权限使用管理员账号完成当前功能验证，管理员具有通配权限；销售、
  只读角色和 403 场景将在里程碑 07 验证。
- 独立联系人管理页面和生成器菜单不是当前主流程的完成条件；本里程碑验收的是
  从客户详情维护联系人。

## 6. 学习结论与验收

结合学习者的实现、解释和运行证据，本轮学习结论为：

- 一对多关系不意味着每个客户必须已有联系人；当前业务允许客户对应 `0..N`
  名联系人。
- `customer_id` 在当前表中是逻辑关联字段，不是由数据库约束的物理外键；Service
  必须在新增前确认客户存在且未删除。
- 打开新增弹窗是纯前端状态变化；真正提交后才经过 Axios、Gateway、Controller、
  Service、Mapper 和 MySQL。
- 前端禁用所属客户用于交互提示，修改 SQL 不包含 `customer_id` 才是防止伪造
  请求转移联系人的后端保障。
- 逻辑删除保留历史行；所有正常查询必须统一过滤 `del_flag='0'`。
- 当前删除策略是“存在未删除联系人则拒绝删除客户”，需要先显式处理联系人，
  系统不会自动级联删除。
- 客户 ID 数组使同一个统计方法同时服务单条和批量删除，并由 MyBatis `IN`
  查询一次完成检查。

联系人表设计、关联校验、联系人 CRUD、客户详情维护流程和客户删除策略均已按
上述证据和边界验收。里程碑 05 完成，下一次按 AI Tutor Mode 从里程碑 06
“跟进记录”的一个 30 至 90 分钟小闭环开始。
