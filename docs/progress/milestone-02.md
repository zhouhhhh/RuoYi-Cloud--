# 里程碑 02：请求链路

## 状态

- 里程碑状态：已完成
- 完成范围：验证码、登录认证、用户查询、密码校验、Token、Gateway 鉴权、
  用户信息、动态路由、用户列表和分页
- 下一里程碑：`03 客户档案`

本文只记录可复核的学习进度和证据，不保存密码、JWT、内部 Token 或完整
Redis Value。

## 已完成链路

### 1. 验证码生成

```text
login.vue getCode()
-> src/api/login.js getCodeImg()
-> GET /dev-api/code
-> Vite 删除 /dev-api
-> Gateway GET /code
-> RouterFunction
-> ValidateCodeHandler
-> ValidateCodeService.createCaptcha()
-> Redis
```

已验证：

- Gateway 的 `/code` 使用 WebFlux 函数式路由，不经过传统 Controller。
- 数学验证码的题目用于生成图片，计算结果作为答案保存。
- Redis Key 为 `captcha_codes:<uuid>`，Value 为验证码答案。
- Redis 类型为 `string`，TTL 初始约为 120 秒。
- 响应包含 `captchaEnabled`、`img`、`uuid`、`code` 和 `msg`。

### 2. 登录请求与验证码过滤

```text
login.vue handleLogin()
-> Pinia userStore.login()
-> POST /dev-api/auth/login
-> Vite 删除 /dev-api
-> Gateway /auth/**
-> CacheRequestBody
-> ValidateCodeFilter
-> StripPrefix=1
-> Auth POST /login
```

已验证：

- 请求体包含 `username`、`password`、`code` 和 `uuid`。
- `CacheRequestBody` 缓存 WebFlux 中只能消费一次的请求体。
- 验证码错误时 Gateway 返回业务 `code=500`，Auth 断点不命中。
- 验证码正确时执行 `chain.filter()`，Auth 断点命中。
- `/auth/login` 经 `StripPrefix=1` 后变为 `/login`。

### 3. Auth 到 System 的用户认证

```text
TokenController.login()
-> SysLoginService.login()
-> RemoteUserService.getUserInfo()
-> OpenFeign / Nacos
-> ruoyi-system
-> SysUserController.info()
```

已验证：

- Auth 在远程查询前检查用户名和密码非空、长度范围及 IP 黑名单。
- OpenFeign 目标服务名为 `ruoyi-system`。
- 内部请求为 `GET /user/info/{username}`，Header 为
  `from-source: inner`。
- System 断点命中，证明 Auth 到 System 的跨服务调用成功。

用户查询链路：

```text
SysUserController
-> ISysUserService
-> SysUserServiceImpl
-> SysUserMapper
-> SysUserMapper.xml
-> MySQL
```

对应 SQL 以 `sys_user` 为主表，左连接 `sys_dept`、`sys_user_role` 和
`sys_role`，并通过 `u.del_flag = '0'` 排除逻辑删除用户。调试结果中的
用户名与登录账号一致。

System 将 `SysUser`、角色标识和菜单权限组装为 `LoginUser`。管理员角色
标识为 `admin`，权限标识为 `*:*:*`。

### 4. 密码校验

已验证错误密码和正确密码两个场景：

```text
错误密码
-> pwd_err_cnt:<username> = 1
-> Redis 类型 string
-> TTL 初始约 600 秒
-> 业务 code=500

正确密码
-> 清除 pwd_err_cnt:<username>
-> Redis GET 返回 nil
-> 业务 code=200
```

密码比较调用链：

```text
SysPasswordService.validate()
-> matches()
-> SecurityUtils.matchesPassword()
```

密码最大错误次数为 5 次，锁定时间为 10 分钟。

### 5. Token 与 Redis 登录会话

```text
TokenService.createToken(LoginUser)
-> 生成内部 UUID
-> 补充用户标识、用户名、IP 和 Token
-> refreshToken()
-> Redis login_tokens:<内部 UUID>
-> 创建 JWT Access Token
```

已验证：

- Redis Key 前缀为 `login_tokens:`。
- Redis Value 为序列化后的 `LoginUser`，Redis 数据结构类型为 `string`。
- Redis TTL 初始为 720 分钟。
- JWT Claims 为 `user_key`、`user_id` 和 `username`。
- 前端响应字段为 `access_token` 和 `expires_in`。
- Access Token 是签名后的 JWT，内部 UUID 位于 `user_key` Claim 中。

当前项目采用“JWT 凭证 + Redis 有状态会话”：

```text
Authorization: Bearer <JWT>
-> Gateway 解析并验证 JWT
-> 提取 user_key
-> 检查 login_tokens:<user_key>
-> 存在则放行，不存在则返回登录状态已过期
```

已完成有效与过期会话的对照：

- 过期会话：JWT 可解析，但 Redis Key 不存在，不执行 `chain.filter()`。
- 有效会话：Redis Key 存在，`islogin=true`，执行 `chain.filter()`。

### 6. 获取用户信息

```text
src/permission.js
-> Pinia userStore.getInfo()
-> src/api/login.js getInfo()
-> GET /dev-api/system/user/getInfo
-> Vite 代理
-> Gateway /system/**
-> AuthFilter
-> StripPrefix=1
-> System GET /user/getInfo
-> SysUserController.getInfo()
```

已验证：

- 浏览器请求为 `GET /dev-api/system/user/getInfo`，并携带
  `Authorization`。
- Gateway 匹配 `/system/**`，经 `StripPrefix=1` 后转发
  `/user/getInfo`。
- 响应包含用户、角色、权限、账号字符类型和密码状态字段。
- Pinia 保存 `id`、`name`、`nickName`、`avatar`、`roles` 和
  `permissions`。

### 7. 动态路由

调用起点是前端全局路由守卫：

```text
router.beforeEach()
-> userStore.getInfo()
-> permissionStore.generateRoutes()
-> GET /dev-api/system/menu/getRouters
-> Gateway /system/**
-> StripPrefix=1
-> System /menu/getRouters
-> SysMenuController.getRouters()
-> selectMenuTreeByUserId()
-> buildMenus()
```

已验证：

- `getInfo` 完成后，路由守卫调用 `generateRoutes()`。
- 响应路由写入 Pinia 的 `routes`、`addRoutes`、`defaultRoutes`、
  `topbarRouters` 和 `sidebarRouters`。

### 8. 用户列表与分页

```text
用户管理页面 getList()
-> listUser()
-> GET /dev-api/system/user/list?pageNum=1&pageSize=10
-> Gateway /system/**
-> System /user/list
-> SysUserController.list()
-> startPage()
-> SysUserServiceImpl.selectUserList()
-> SysUserMapper.selectUserList()
-> SysUserMapper.xml selectUserList
-> MySQL
```

已验证：

- 分页参数为 `pageNum=1`、`pageSize=10`。
- 分页响应包含 `code`、`msg`、`rows` 和 `total`。
- SQL 主表为 `sys_user`，左连接 `sys_dept`。
- `startPage()` 在 Mapper 查询前建立 PageHelper 分页上下文。

### 9. 无 Token 请求

对受保护的 `/system/user/getInfo` 发起无 Token 请求，结果为：

```text
HTTP 状态：200
业务 code：401
msg：令牌不能为空
```

该结果证明当前统一响应中的业务状态和 HTTP 状态是两个不同层次。

## 最终链路图

```text
Vue3 页面或路由守卫
-> Axios
-> /dev-api
-> Vite 代理删除 /dev-api
-> Gateway
   -> 路由匹配
   -> AuthFilter 解析 JWT
   -> Redis 检查 login_tokens:<user_key>
   -> StripPrefix
-> Auth 或 System
   -> Controller
   -> Service
   -> Mapper
   -> Mapper XML
   -> MySQL
-> 统一响应
-> Pinia 或页面
```

登录场景还包含：

```text
Gateway ValidateCodeFilter
-> Redis captcha_codes:<uuid>
-> Auth
-> OpenFeign
-> System
-> 密码校验
-> Redis login_tokens:<user_key>
-> JWT Access Token
```

## 状态码归属

- `401`：主要属于 Gateway 登录鉴权，表示 Token 缺失、无效或 Redis
  登录会话已过期。
- `403`：主要属于业务服务权限校验，表示用户已经登录但缺少接口权限。
- `404`：属于路由或接口映射，常见于 Gateway 路由未匹配、路径重写错误
  或下游 Controller 不存在。
- `500`：属于 Gateway、Auth 或业务服务异常；项目中的业务 `code=500`
  也可能表示验证码错误、密码错误等可预期业务失败，不等同于 HTTP 500。

## 验收结论

已具备以下证据：

- 正常登录、正确验证码、有效 Token 和列表查询的成功请求。
- 错误验证码、错误密码、过期会话和无 Token 请求的失败响应。
- 验证码、密码错误次数和登录会话的 Redis Key、类型与 TTL。
- Gateway、Auth 和 System 的断点或响应证据。
- 用户查询的 Controller、Service、Mapper、Mapper XML 和 SQL 链路。
- Vite 代理、Gateway 路由、`StripPrefix`、JWT、Redis 会话和分页的解释。

里程碑 02 验收通过。
