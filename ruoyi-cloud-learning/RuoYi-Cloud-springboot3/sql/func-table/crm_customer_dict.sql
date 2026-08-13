INSERT INTO `ry-cloud`.sys_dict_type (dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark) VALUES
	 ('客户类型','crm_customer_type','0','admin','2026-08-04 12:36:35','',NULL,NULL),
	 ('客户来源','crm_customer_source','0','admin','2026-08-04 12:37:09','',NULL,NULL),
	 ('客户等级','crm_customer_level','0','admin','2026-08-04 12:37:29','',NULL,NULL),
	 ('客户状态','crm_customer_status','0','admin','2026-08-04 12:37:44','',NULL,NULL);

INSERT INTO `ry-cloud`.sys_dict_data
( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES( 0, '个人客户', '0', 'crm_customer_type', NULL, 'default', 'N', '0', 'admin', '2026-08-04 12:38:17', 'admin', '2026-08-04 12:43:15', NULL);
INSERT INTO `ry-cloud`.sys_dict_data
( dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES( 1, '企业客户', '1', 'crm_customer_type', NULL, 'default', 'N', '0', 'admin', '2026-08-04 12:38:32', 'admin', '2026-08-04 12:43:21', NULL);
INSERT INTO `ry-cloud`.sys_dict_data
(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES( 0, '主动开发', '0', 'crm_customer_source', NULL, 'default', 'N', '0', 'admin', '2026-08-04 12:40:41', 'admin', '2026-08-04 12:42:42', NULL);
INSERT INTO `ry-cloud`.sys_dict_data
(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES( 1, '客户推荐', '1', 'crm_customer_source', NULL, 'default', 'N', '0', 'admin', '2026-08-04 12:40:54', 'admin', '2026-08-04 12:42:49', NULL);
INSERT INTO `ry-cloud`.sys_dict_data
(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES( 2, '网络推广', '2', 'crm_customer_source', NULL, 'default', 'N', '0', 'admin', '2026-08-04 12:41:23', 'admin', '2026-08-04 12:42:55', NULL);
INSERT INTO `ry-cloud`.sys_dict_data
(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES( 3, '其他', '3', 'crm_customer_source', NULL, 'default', 'N', '0', 'admin', '2026-08-04 12:43:02', '', NULL, NULL);
INSERT INTO `ry-cloud`.sys_dict_data
(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES( 0, 'A级', 'A', 'crm_customer_level', NULL, 'default', 'N', '0', 'admin', '2026-08-04 12:43:43', '', NULL, NULL);
INSERT INTO `ry-cloud`.sys_dict_data
(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES( 1, 'B级', 'B', 'crm_customer_level', NULL, 'default', 'N', '0', 'admin', '2026-08-04 12:43:55', '', NULL, NULL);
INSERT INTO `ry-cloud`.sys_dict_data
(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES( 2, 'C级', 'C', 'crm_customer_level', NULL, 'default', 'N', '0', 'admin', '2026-08-04 12:44:04', '', NULL, NULL);
INSERT INTO `ry-cloud`.sys_dict_data
(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES( 0, '潜在', '0', 'crm_customer_status', NULL, 'default', 'N', '0', 'admin', '2026-08-04 12:44:18', '', NULL, NULL);
INSERT INTO `ry-cloud`.sys_dict_data
(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES( 1, '跟进中', '1', 'crm_customer_status', NULL, 'default', 'N', '0', 'admin', '2026-08-04 12:44:28', '', NULL, NULL);
INSERT INTO `ry-cloud`.sys_dict_data
(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES( 2, '已成交', '2', 'crm_customer_status', NULL, 'default', 'N', '0', 'admin', '2026-08-04 12:44:35', 'admin', '2026-08-04 12:45:03', NULL);
INSERT INTO `ry-cloud`.sys_dict_data
(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, update_by, update_time, remark)
VALUES( 3, '已流失', '3', 'crm_customer_status', NULL, 'default', 'N', '0', 'admin', '2026-08-04 12:44:48', 'admin', '2026-08-04 12:45:07', NULL);