
/**
  * @Version: 里程碑 05，V1，首次建表。
  * @Description: CRM 联系人表
  * @Description: customer_id 仅作逻辑关联，不创建物理外键；客户有效性由 Service 校验。
  * @Description: 每条联系人记录属于一个客户；客户可有多位联系人。
  * @Description: 联系人电话可空，不设置唯一约束。
  * @Description: 客户不存在或已逻辑删除时，禁止新增联系人。
  * @Description: 联系人采用逻辑删除；客户仍有未删除联系人时，拒绝删除客户。
  * @Description: 客户主要联系手机号与联系人电话分别维护。
  */
create table `crm_contact` (
  `contact_id` bigint NOT NULL AUTO_INCREMENT COMMENT '联系人记录 ID',
  `customer_id` bigint NOT NULL COMMENT '所属客户，与客户主键类型一致',
  `contact_name` varchar(100) NOT NULL COMMENT '联系人姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话，按字符串保存',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '0 正常、2 删除',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '补充说明',
  PRIMARY KEY (`contact_id`),
  KEY idx_contact_customer (customer_id, del_flag)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='CRM 联系人表';