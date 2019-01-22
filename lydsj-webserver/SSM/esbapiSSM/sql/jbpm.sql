--
-- 数据库: `crm`
--

-- --------------------------------------------------------

--
-- 表的结构 `jbpm4_deployment`
--

CREATE TABLE IF NOT EXISTS `jbpm4_deployment` (
  `dbid_` decimal(19,0) NOT NULL,
  `name_` varchar(1000) DEFAULT NULL,
  `timestamp_` decimal(19,0) DEFAULT NULL,
  `state_` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`dbid_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `jbpm4_deployprop`
--

CREATE TABLE IF NOT EXISTS `jbpm4_deployprop` (
  `dbid_` decimal(19,0) NOT NULL,
  `deployment_` decimal(19,0) DEFAULT NULL,
  `objname_` varchar(255) DEFAULT NULL,
  `key_` varchar(255) DEFAULT NULL,
  `stringval_` varchar(255) DEFAULT NULL,
  `longval_` decimal(19,0) DEFAULT NULL,
  PRIMARY KEY (`dbid_`),
  KEY `IX_KEY_STRINGVAL` (`key_`,`stringval_`),
  KEY `FK_DEPLPROP_DEPL` (`deployment_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `jbpm4_execution`
--

CREATE TABLE IF NOT EXISTS `jbpm4_execution` (
  `dbid_` decimal(19,0) NOT NULL,
  `class_` varchar(255) NOT NULL,
  `dbversion_` int(10) NOT NULL,
  `activityname_` varchar(255) DEFAULT NULL,
  `procdefid_` varchar(255) DEFAULT NULL,
  `hasvars_` int(1) DEFAULT NULL,
  `name_` varchar(255) DEFAULT NULL,
  `key_` varchar(255) DEFAULT NULL,
  `id_` varchar(255) DEFAULT NULL,
  `state_` varchar(255) DEFAULT NULL,
  `susphiststate_` varchar(255) DEFAULT NULL,
  `priority_` int(10) DEFAULT NULL,
  `hisactinst_` decimal(19,0) DEFAULT NULL,
  `parent_` decimal(19,0) DEFAULT NULL,
  `instance_` decimal(19,0) DEFAULT NULL,
  `superexec_` decimal(19,0) DEFAULT NULL,
  `subprocinst_` decimal(19,0) DEFAULT NULL,
  `parent_idx_` int(10) DEFAULT NULL,
  PRIMARY KEY (`dbid_`),
  KEY `dbid_` (`dbid_`),
  KEY `dbid__2` (`dbid_`),
  KEY `dbid__3` (`dbid_`),
  KEY `IX_PARENT` (`parent_`),
  KEY `FK_EXEC_INSTANCE` (`instance_`),
  KEY `FK_EXEC_SUBPI` (`subprocinst_`),
  KEY `FK_EXEC_SUPEREXEC` (`superexec_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `jbpm4_hist_actinst`
--

CREATE TABLE IF NOT EXISTS `jbpm4_hist_actinst` (
  `dbid_` decimal(19,0) NOT NULL,
  `class_` varchar(255) NOT NULL,
  `dbversion_` decimal(10,0) NOT NULL,
  `hproci_` decimal(19,0) DEFAULT NULL,
  `type_` varchar(255) DEFAULT NULL,
  `execution_` varchar(255) DEFAULT NULL,
  `activity_name_` varchar(255) DEFAULT NULL,
  `start_` date DEFAULT NULL,
  `end_` date DEFAULT NULL,
  `duration_` decimal(19,0) DEFAULT NULL,
  `transition_` varchar(255) DEFAULT NULL,
  `nextidx_` decimal(10,0) DEFAULT NULL,
  `htask_` decimal(19,0) DEFAULT NULL,
  PRIMARY KEY (`dbid_`),
  KEY `FK_HACTI_HPROCI` (`hproci_`),
  KEY `FK_HTI_HTASK` (`htask_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `jbpm4_hist_detail`
--

CREATE TABLE IF NOT EXISTS `jbpm4_hist_detail` (
  `dbid_` decimal(19,0) NOT NULL,
  `class_` varchar(255) NOT NULL,
  `dbversion_` decimal(10,0) NOT NULL,
  `userid_` varchar(255) DEFAULT NULL,
  `time_` date DEFAULT NULL,
  `hproci_` decimal(19,0) DEFAULT NULL,
  `hprociidx_` decimal(10,0) DEFAULT NULL,
  `hacti_` decimal(19,0) DEFAULT NULL,
  `hactiidx_` decimal(10,0) DEFAULT NULL,
  `htask_` decimal(19,0) DEFAULT NULL,
  `htaskidx_` decimal(10,0) DEFAULT NULL,
  `hvar_` decimal(19,0) DEFAULT NULL,
  `hvaridx_` decimal(10,0) DEFAULT NULL,
  `message_` varchar(1000) DEFAULT NULL,
  `old_str_` varchar(255) DEFAULT NULL,
  `new_str_` varchar(255) DEFAULT NULL,
  `old_int_` decimal(10,0) DEFAULT NULL,
  `new_int_` decimal(10,0) DEFAULT NULL,
  `old_time_` date DEFAULT NULL,
  `new_time_` date DEFAULT NULL,
  `parent_` decimal(19,0) DEFAULT NULL,
  `parent_idx_` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`dbid_`),
  KEY `FK_HDETAIL_HACTI` (`hacti_`),
  KEY `FK_HDETAIL_HPROCI` (`hproci_`),
  KEY `FK_HDETAIL_HTASK` (`htask_`),
  KEY `FK_HDETAIL_HVAR` (`hvar_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `jbpm4_hist_procinst`
--

CREATE TABLE IF NOT EXISTS `jbpm4_hist_procinst` (
  `dbid_` decimal(19,0) NOT NULL,
  `dbversion_` decimal(10,0) NOT NULL,
  `id_` varchar(255) DEFAULT NULL,
  `procdefid_` varchar(255) DEFAULT NULL,
  `key_` varchar(255) DEFAULT NULL,
  `start_` date DEFAULT NULL,
  `end_` date DEFAULT NULL,
  `duration_` decimal(19,0) DEFAULT NULL,
  `state_` varchar(255) DEFAULT NULL,
  `endactivity_` varchar(255) DEFAULT NULL,
  `nextidx_` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`dbid_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `jbpm4_hist_task`
--

CREATE TABLE IF NOT EXISTS `jbpm4_hist_task` (
  `dbid_` decimal(19,0) NOT NULL,
  `dbversion_` decimal(10,0) NOT NULL,
  `execution_` varchar(255) DEFAULT NULL,
  `outcome_` varchar(255) DEFAULT NULL,
  `assignee_` varchar(255) DEFAULT NULL,
  `priority_` decimal(10,0) DEFAULT NULL,
  `state_` varchar(255) DEFAULT NULL,
  `create_` date DEFAULT NULL,
  `end_` date DEFAULT NULL,
  `duration_` decimal(19,0) DEFAULT NULL,
  `nextidx_` decimal(10,0) DEFAULT NULL,
  `supertask_` decimal(19,0) DEFAULT NULL,
  PRIMARY KEY (`dbid_`),
  KEY `dbid_` (`dbid_`),
  KEY `FK_HSUPERT_SUB` (`supertask_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `jbpm4_hist_var`
--

CREATE TABLE IF NOT EXISTS `jbpm4_hist_var` (
  `dbid_` decimal(19,0) NOT NULL,
  `dbversion_` decimal(10,0) NOT NULL,
  `procinstid_` varchar(255) DEFAULT NULL,
  `executionid_` varchar(255) DEFAULT NULL,
  `varname_` varchar(255) DEFAULT NULL,
  `value_` varchar(255) DEFAULT NULL,
  `hproci_` decimal(19,0) DEFAULT NULL,
  `htask_` decimal(19,0) DEFAULT NULL,
  PRIMARY KEY (`dbid_`),
  KEY `FK_HVAR_HPROCI` (`hproci_`),
  KEY `FK_HVAR_HTASK` (`htask_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `jbpm4_id_group`
--

CREATE TABLE IF NOT EXISTS `jbpm4_id_group` (
  `dbid_` decimal(19,0) NOT NULL,
  `dbversion_` decimal(10,0) NOT NULL,
  `id_` varchar(255) DEFAULT NULL,
  `name_` varchar(255) DEFAULT NULL,
  `type_` varchar(255) DEFAULT NULL,
  `parent_` decimal(19,0) DEFAULT NULL,
  PRIMARY KEY (`dbid_`),
  KEY `dbid_` (`dbid_`),
  KEY `FK_GROUP_PARENT` (`parent_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `jbpm4_id_membership`
--

CREATE TABLE IF NOT EXISTS `jbpm4_id_membership` (
  `dbid_` decimal(19,0) NOT NULL,
  `dbversion_` decimal(10,0) NOT NULL,
  `user_` decimal(19,0) DEFAULT NULL,
  `group_` decimal(19,0) DEFAULT NULL,
  `name_` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`dbid_`),
  KEY `FK_MEM_GROUP` (`group_`),
  KEY `FK_MEM_USER` (`user_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `jbpm4_id_user`
--

CREATE TABLE IF NOT EXISTS `jbpm4_id_user` (
  `dbid_` decimal(19,0) NOT NULL,
  `dbversion_` decimal(10,0) NOT NULL,
  `id_` varchar(255) DEFAULT NULL,
  `password_` varchar(255) DEFAULT NULL,
  `givenname_` varchar(255) DEFAULT NULL,
  `familyname_` varchar(255) DEFAULT NULL,
  `businessemail_` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`dbid_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `jbpm4_jbyj`
--

CREATE TABLE IF NOT EXISTS `jbpm4_jbyj` (
  `jbyj_id` varchar(32) NOT NULL COMMENT '经办意见主键',
  `processinstance_id` varchar(255) DEFAULT NULL COMMENT '流程实例ID',
  `task_id` varchar(50) DEFAULT NULL COMMENT '任务ID',
  `business_key` varchar(255) DEFAULT NULL COMMENT '业务主键',
  `user_id` varchar(11) DEFAULT NULL COMMENT '用户主键',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `outcome` varchar(50) DEFAULT NULL COMMENT '处理结果',
  `jbyj` varchar(1000) DEFAULT NULL COMMENT '经办意见',
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户姓名',
  `role_id` varchar(13) DEFAULT NULL COMMENT '用户角色ID',
  `role_name` varchar(64) DEFAULT NULL COMMENT '用户角色名',
  `task_name` varchar(255) DEFAULT NULL COMMENT '任务节点名称',
  `task_info_url` varchar(500) DEFAULT NULL COMMENT '任务信息页面地址',
  `task_szmpy` varchar(255) DEFAULT NULL COMMENT '任务名称首字母拼音',
  PRIMARY KEY (`jbyj_id`),
  KEY ` IX_BUSINESS_TASKSZMPY` (`business_key`,`task_szmpy`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `jbpm4_job`
--

CREATE TABLE IF NOT EXISTS `jbpm4_job` (
  `dbid_` decimal(19,0) NOT NULL,
  `class_` varchar(255) NOT NULL,
  `dbversion_` decimal(10,0) NOT NULL,
  `duedate_` date DEFAULT NULL,
  `state_` varchar(255) DEFAULT NULL,
  `isexclusive_` int(1) DEFAULT NULL,
  `lockowner_` varchar(255) DEFAULT NULL,
  `lockexptime_` date DEFAULT NULL,
  `exception_` varchar(1024) DEFAULT NULL,
  `retries_` decimal(10,0) DEFAULT NULL,
  `processinstance_` decimal(19,0) DEFAULT NULL,
  `execution_` decimal(19,0) DEFAULT NULL,
  `cfg_` decimal(19,0) DEFAULT NULL,
  `signal_` varchar(255) DEFAULT NULL,
  `event_` varchar(255) DEFAULT NULL,
  `repeat_` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`dbid_`),
  KEY `FK_JOB_CFG` (`cfg_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `jbpm4_lob`
--

CREATE TABLE IF NOT EXISTS `jbpm4_lob` (
  `dbid_` decimal(19,0) NOT NULL,
  `dbversion_` decimal(10,0) NOT NULL,
  `blob_value_` blob,
  `deployment_` decimal(19,0) DEFAULT NULL,
  `name_` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`dbid_`),
  KEY `IX_DEPLOYID` (`deployment_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `jbpm4_participation`
--

CREATE TABLE IF NOT EXISTS `jbpm4_participation` (
  `dbid_` decimal(19,0) NOT NULL,
  `dbversion_` decimal(10,0) NOT NULL,
  `groupid_` varchar(255) DEFAULT NULL,
  `userid_` varchar(255) DEFAULT NULL,
  `type_` varchar(255) DEFAULT NULL,
  `task_` decimal(19,0) DEFAULT NULL,
  `swimlane_` decimal(19,0) DEFAULT NULL,
  PRIMARY KEY (`dbid_`),
  KEY `FK_PART_SWIMLANE` (`swimlane_`),
  KEY `FK_PART_TASK` (`task_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `jbpm4_property`
--

CREATE TABLE IF NOT EXISTS `jbpm4_property` (
  `key_` varchar(255) NOT NULL,
  `version_` decimal(10,0) NOT NULL,
  `value_` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`key_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `jbpm4_swimlane`
--

CREATE TABLE IF NOT EXISTS `jbpm4_swimlane` (
  `dbid_` decimal(19,0) NOT NULL,
  `dbversion_` decimal(10,0) NOT NULL,
  `name_` varchar(255) DEFAULT NULL,
  `assignee_` varchar(255) DEFAULT NULL,
  `execution_` decimal(19,0) DEFAULT NULL,
  PRIMARY KEY (`dbid_`),
  KEY `FK_SWIMLANE_EXEC` (`execution_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `jbpm4_task`
--

CREATE TABLE IF NOT EXISTS `jbpm4_task` (
  `dbid_` decimal(19,0) NOT NULL,
  `class_` char(1) NOT NULL,
  `dbversion_` decimal(10,0) NOT NULL,
  `name_` varchar(255) DEFAULT NULL,
  `descr_` varchar(255) DEFAULT NULL,
  `state_` varchar(255) DEFAULT NULL,
  `susphiststate_` varchar(255) DEFAULT NULL,
  `assignee_` varchar(255) DEFAULT NULL,
  `form_` varchar(255) DEFAULT NULL,
  `priority_` decimal(10,0) DEFAULT NULL,
  `create_` date DEFAULT NULL,
  `duedate_` date DEFAULT NULL,
  `progress_` decimal(10,0) DEFAULT NULL,
  `signalling_` decimal(1,0) DEFAULT NULL,
  `execution_id_` varchar(255) DEFAULT NULL,
  `activity_name_` varchar(255) DEFAULT NULL,
  `hasvars_` decimal(1,0) DEFAULT NULL,
  `supertask_` decimal(19,0) DEFAULT NULL,
  `execution_` decimal(19,0) DEFAULT NULL,
  `procinst_` decimal(19,0) DEFAULT NULL,
  `swimlane_` decimal(19,0) DEFAULT NULL,
  `taskdefname_` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`dbid_`),
  KEY `dbid_` (`dbid_`),
  KEY `FK_TASK_SUPERTASK` (`supertask_`),
  KEY `FK_TASK_SWIML` (`swimlane_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `jbpm4_variable`
--

CREATE TABLE IF NOT EXISTS `jbpm4_variable` (
  `dbid_` decimal(19,0) NOT NULL,
  `class_` varchar(255) NOT NULL,
  `dbversion_` decimal(10,0) NOT NULL,
  `key_` varchar(255) DEFAULT NULL,
  `converter_` varchar(255) DEFAULT NULL,
  `hist_` decimal(1,0) DEFAULT NULL,
  `execution_` decimal(19,0) DEFAULT NULL,
  `task_` decimal(19,0) DEFAULT NULL,
  `lob_` decimal(19,0) DEFAULT NULL,
  `date_value_` date DEFAULT NULL,
  `double_value_` float DEFAULT NULL,
  `classname_` varchar(255) DEFAULT NULL,
  `long_value_` decimal(19,0) DEFAULT NULL,
  `string_value_` varchar(255) DEFAULT NULL,
  `text_value_` varchar(255) DEFAULT NULL,
  `exesys_` decimal(19,0) DEFAULT NULL,
  PRIMARY KEY (`dbid_`),
  KEY `FK_VAR_EXECUTION` (`execution_`),
  KEY `FK_VAR_EXESYS` (`exesys_`),
  KEY `FK_VAR_LOB` (`lob_`),
  KEY `FK_VAR_TASK` (`task_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- 限制表 `jbpm4_deployprop`
--
ALTER TABLE `jbpm4_deployprop`
  ADD CONSTRAINT `FK_DEPLPROP_DEPL` FOREIGN KEY (`deployment_`) REFERENCES `jbpm4_deployment` (`dbid_`);

--
-- 限制表 `jbpm4_execution`
--
ALTER TABLE `jbpm4_execution`
  ADD CONSTRAINT `FK_EXEC_PARENT` FOREIGN KEY (`parent_`) REFERENCES `jbpm4_execution` (`dbid_`),
  ADD CONSTRAINT `FK_EXEC_SUBPI` FOREIGN KEY (`subprocinst_`) REFERENCES `jbpm4_execution` (`dbid_`),
  ADD CONSTRAINT `FK_EXEC_SUPEREXEC` FOREIGN KEY (`superexec_`) REFERENCES `jbpm4_execution` (`dbid_`);

--
-- 限制表 `jbpm4_hist_actinst`
--
ALTER TABLE `jbpm4_hist_actinst`
  ADD CONSTRAINT `FK_HTI_HTASK` FOREIGN KEY (`htask_`) REFERENCES `jbpm4_hist_task` (`dbid_`),
  ADD CONSTRAINT `FK_HACTI_HPROCI` FOREIGN KEY (`hproci_`) REFERENCES `jbpm4_hist_procinst` (`dbid_`);

--
-- 限制表 `jbpm4_hist_detail`
--
ALTER TABLE `jbpm4_hist_detail`
  ADD CONSTRAINT `FK_HDETAIL_HVAR` FOREIGN KEY (`hvar_`) REFERENCES `jbpm4_hist_var` (`dbid_`),
  ADD CONSTRAINT `FK_HDETAIL_HACTI` FOREIGN KEY (`hacti_`) REFERENCES `jbpm4_hist_actinst` (`dbid_`),
  ADD CONSTRAINT `FK_HDETAIL_HPROCI` FOREIGN KEY (`hproci_`) REFERENCES `jbpm4_hist_procinst` (`dbid_`),
  ADD CONSTRAINT `FK_HDETAIL_HTASK` FOREIGN KEY (`htask_`) REFERENCES `jbpm4_hist_task` (`dbid_`);

--
-- 限制表 `jbpm4_hist_task`
--
ALTER TABLE `jbpm4_hist_task`
  ADD CONSTRAINT `FK_HSUPERT_SUB` FOREIGN KEY (`supertask_`) REFERENCES `jbpm4_hist_task` (`dbid_`);

--
-- 限制表 `jbpm4_hist_var`
--
ALTER TABLE `jbpm4_hist_var`
  ADD CONSTRAINT `FK_HVAR_HTASK` FOREIGN KEY (`htask_`) REFERENCES `jbpm4_hist_task` (`dbid_`),
  ADD CONSTRAINT `FK_HVAR_HPROCI` FOREIGN KEY (`hproci_`) REFERENCES `jbpm4_hist_procinst` (`dbid_`);

--
-- 限制表 `jbpm4_id_group`
--
ALTER TABLE `jbpm4_id_group`
  ADD CONSTRAINT `FK_GROUP_PARENT` FOREIGN KEY (`parent_`) REFERENCES `jbpm4_id_group` (`dbid_`);

--
-- 限制表 `jbpm4_id_membership`
--
ALTER TABLE `jbpm4_id_membership`
  ADD CONSTRAINT `FK_MEM_USER` FOREIGN KEY (`user_`) REFERENCES `jbpm4_id_user` (`dbid_`),
  ADD CONSTRAINT `FK_MEM_GROUP` FOREIGN KEY (`group_`) REFERENCES `jbpm4_id_group` (`dbid_`);

--
-- 限制表 `jbpm4_job`
--
ALTER TABLE `jbpm4_job`
  ADD CONSTRAINT `FK_JOB_CFG` FOREIGN KEY (`cfg_`) REFERENCES `jbpm4_lob` (`dbid_`);

--
-- 限制表 `jbpm4_lob`
--
ALTER TABLE `jbpm4_lob`
  ADD CONSTRAINT `FK_LOB_DEPLOYMENT` FOREIGN KEY (`deployment_`) REFERENCES `jbpm4_deployment` (`dbid_`);

--
-- 限制表 `jbpm4_participation`
--
ALTER TABLE `jbpm4_participation`
  ADD CONSTRAINT `FK_PART_TASK` FOREIGN KEY (`task_`) REFERENCES `jbpm4_task` (`dbid_`),
  ADD CONSTRAINT `FK_PART_SWIMLANE` FOREIGN KEY (`swimlane_`) REFERENCES `jbpm4_swimlane` (`dbid_`);

--
-- 限制表 `jbpm4_swimlane`
--
ALTER TABLE `jbpm4_swimlane`
  ADD CONSTRAINT `FK_SWIMLANE_EXEC` FOREIGN KEY (`execution_`) REFERENCES `jbpm4_execution` (`dbid_`);

--
-- 限制表 `jbpm4_task`
--
ALTER TABLE `jbpm4_task`
  ADD CONSTRAINT `FK_TASK_SWIML` FOREIGN KEY (`swimlane_`) REFERENCES `jbpm4_swimlane` (`dbid_`),
  ADD CONSTRAINT `FK_TASK_SUPERTASK` FOREIGN KEY (`supertask_`) REFERENCES `jbpm4_task` (`dbid_`);

--
-- 限制表 `jbpm4_variable`
--
ALTER TABLE `jbpm4_variable`
  ADD CONSTRAINT `FK_VAR_TASK` FOREIGN KEY (`task_`) REFERENCES `jbpm4_task` (`dbid_`),
  ADD CONSTRAINT `FK_VAR_EXECUTION` FOREIGN KEY (`execution_`) REFERENCES `jbpm4_execution` (`dbid_`),
  ADD CONSTRAINT `FK_VAR_EXESYS` FOREIGN KEY (`exesys_`) REFERENCES `jbpm4_execution` (`dbid_`),
  ADD CONSTRAINT `FK_VAR_LOB` FOREIGN KEY (`lob_`) REFERENCES `jbpm4_lob` (`dbid_`);

