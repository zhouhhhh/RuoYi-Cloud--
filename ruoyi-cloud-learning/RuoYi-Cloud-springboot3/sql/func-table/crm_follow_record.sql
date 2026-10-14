/**
  * @Version: 里程碑 06，V1，首次建表。
  * @Description: CRM 客户跟进记录表
  * @BusinessRules:
  * 1. 客户必须有效，才能创建跟进记录。
  * 2. 联系人可不指定；指定时必须存在、未逻辑删除，且属于该客户。
  * 3. 删除客户前，必须检查该客户是否有有效联系人及跟进记录，若有则禁止删除。
  * 4. 以上规则由 Service 实现，避免把注释误认为数据库约束。  
  */
create table `crm_follow_record` (
  `follow_id` bigint not null auto_increment comment '主键，跟进记录 ID',
  `customer_id` bigint not null comment '所属客户 ID，逻辑关联客户表',
  `contact_id` bigint default null comment '客户联系人 ID；不指定联系人时为空',
  `follow_type` char(1) not null comment '跟进方式：0 电话、1 微信、2 拜访、3 其他',
  `content` varchar(2000) not null comment '跟进内容，业务上禁止空字符串和纯空格',
  `follow_time` datetime not null comment '实际跟进时间，精确到秒',
  `next_follow_time` datetime default null comment '本次约定的下次跟进时间',
  `del_flag` char(1) not null default '0' comment '0 正常、2 删除',
  `create_by` varchar(64) not null comment '创建者，后端取当前登录用户名',
  `create_time` datetime not null comment '创建时间，后端生成',
  `update_by` varchar(64) default null comment '更新者，预留',
  `update_time` datetime default null comment '更新时间，预留',
  primary key (`follow_id`),
  key `idx_follow_customer_time` (`customer_id`, `del_flag`, `follow_time`, `follow_id`)
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_0900_ai_ci comment 'CRM 客户跟进记录表，记录客户的跟进情况，支持多种跟进方式。';