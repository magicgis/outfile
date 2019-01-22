/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : localhost:3306
 Source Schema         : mxxmgl

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 20/12/2018 13:59:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file`  (
  `id` int(11) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `file_size` double NULL DEFAULT NULL,
  `file_location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `delete_status` int(255) NULL DEFAULT NULL,
  `download_times` int(11) NULL DEFAULT NULL,
  `create_user` int(11) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `save_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `project_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` int(11) NOT NULL,
  `menu_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `menu_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `permission_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `permission_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1001, 'user', '用户管理', 'user:list', '查看');
INSERT INTO `menu` VALUES (1002, 'user', '用户管理', 'user:add', '新增');
INSERT INTO `menu` VALUES (1003, 'user', '用户管理', 'user:update', '修改');
INSERT INTO `menu` VALUES (1004, 'user', '用户管理', 'user:delete', '删除');
INSERT INTO `menu` VALUES (2001, 'project', '项目管理', 'project:list', '查看');
INSERT INTO `menu` VALUES (2002, 'project', '项目管理', 'project:add', '新增');
INSERT INTO `menu` VALUES (2003, 'project', '项目管理', 'project:update', '更新');
INSERT INTO `menu` VALUES (2004, 'project', '项目管理', 'project:delete', '删除');
INSERT INTO `menu` VALUES (3001, 'role', '角色管理', 'role:list', '查看');
INSERT INTO `menu` VALUES (3002, 'role', '角色管理', 'role:add', '新增');
INSERT INTO `menu` VALUES (3003, 'role', '角色管理', 'role:update', '修改');
INSERT INTO `menu` VALUES (3004, 'role', '角色管理', 'role:delete', '删除');
INSERT INTO `menu` VALUES (4001, 'plan', '计划管理', 'plan:list', '查看');
INSERT INTO `menu` VALUES (4002, 'plan', '计划管理', 'plan:add', '新增');
INSERT INTO `menu` VALUES (4003, 'plan', '计划管理', 'plan:update', '更新');
INSERT INTO `menu` VALUES (4004, 'plan', '计划管理', 'plan:delete', '删除');
INSERT INTO `menu` VALUES (5001, 'task', '任务管理', 'task:list', '查看');
INSERT INTO `menu` VALUES (5002, 'task', '任务管理', 'task:add', '新增');
INSERT INTO `menu` VALUES (5003, 'task', '任务管理', 'task:update', '更新');
INSERT INTO `menu` VALUES (5004, 'task', '任务管理', 'task:delete', '删除');
INSERT INTO `menu` VALUES (6001, 'outcome', '汇总展示', 'outcome:list', '查看');
INSERT INTO `menu` VALUES (6002, 'outcome', '汇总展示', 'outcome:add', '增加');
INSERT INTO `menu` VALUES (6003, 'outcome', '汇总展示', 'outcome:update', '更新');
INSERT INTO `menu` VALUES (6004, 'outcome', '汇总展示', 'outcome:delete', '删除');
INSERT INTO `menu` VALUES (7001, 'file', '文件管理', 'file:list', '查看');
INSERT INTO `menu` VALUES (7002, 'file', '文件管理', 'file:add', '增加');
INSERT INTO `menu` VALUES (7003, 'file', '文件管理', 'file:update', '更新');
INSERT INTO `menu` VALUES (7004, 'file', '文件管理', 'file:delete', '删除');

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `project_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `project_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `plan_days` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `plan_finish_time` datetime(0) NULL DEFAULT NULL,
  `real_finish_time` datetime(0) NULL DEFAULT NULL,
  `create_user` int(11) NULL DEFAULT NULL,
  `project_money` decimal(10, 0) NULL DEFAULT NULL,
  `project_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `project_progress` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of project
-- ----------------------------
INSERT INTO `project` VALUES (1, '梅州旅游大数据', '梅州市旅游数据统计展示系统', '1', '2018-12-09 22:04:22', '2018-12-10 00:03:31', '两年', '2020-01-09 22:04:37', '2018-12-09 23:37:17', 1, 100, 'A0001', '全部上线');
INSERT INTO `project` VALUES (2, '大埔旅游', '大埔县旅游数据数据统计展示', '1', '2018-12-10 00:04:26', '2018-12-10 00:04:28', '两个月', '2018-12-10 00:04:38', '2018-12-10 00:04:40', 1, 200, 'A0002', '尚未填写');
INSERT INTO `project` VALUES (3, '梅县旅游', '梅县旅游数据数据统计展示', '2', '2018-12-10 00:06:36', '2018-12-10 00:06:56', '两个月', '2018-12-10 00:06:45', '2018-12-10 00:06:48', 1, 300, 'A0003', '尚未填写');
INSERT INTO `project` VALUES (4, '连锁店项目', '前后端系统和B端系统', '3', '2018-12-10 00:08:31', '2018-12-10 00:08:34', '三个月', '2018-12-10 00:08:47', '2018-12-10 00:08:49', 1, 400, 'A0004', '尚未填写');
INSERT INTO `project` VALUES (6, '舆情系统', '这是一个爬虫系统', '2', '2018-12-10 03:11:25', '2018-12-10 11:16:16', '三年', NULL, NULL, 1, 666, 'A0005', '完成99.9%');
INSERT INTO `project` VALUES (8, '航润CRM项目', '客户管理关系', '2', '2018-12-10 03:17:39', '2018-12-10 11:17:52', '一年', NULL, NULL, 1, 200, 'A0006', '已经完成100%');
INSERT INTO `project` VALUES (9, '海关项目', '为广州海关提供一套最先进的系统', '1', '2018-12-10 07:26:38', '2018-12-10 15:26:37', '两年', NULL, NULL, 1, 300, 'A0007', '尚未填写');
INSERT INTO `project` VALUES (10, '广铁数据统计平台', '广州铁路局', '1', '2018-12-10 07:33:23', '2018-12-10 15:33:23', '两年', NULL, NULL, 1, 200, 'A154', '尚未填写');

-- ----------------------------
-- Table structure for project_plan
-- ----------------------------
DROP TABLE IF EXISTS `project_plan`;
CREATE TABLE `project_plan`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `plan_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `plan_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `plan_days` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `plan_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_user` int(11) NULL DEFAULT NULL,
  `plan_status` int(11) NULL DEFAULT NULL,
  `task_members` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of project_plan
-- ----------------------------
INSERT INTO `project_plan` VALUES (1, 1, '2018-12-11 17:15:33', '项目模块开发', '新需求', '2天', 'P0001', 1, 1, 'jardor');
INSERT INTO `project_plan` VALUES (2, 8, '2018-12-11 17:15:19', '流程部署', 'bug修复', '100天', 'P0002', 1, 1, 'white');
INSERT INTO `project_plan` VALUES (7, 9, '2018-12-11 09:25:28', 'bug修复', '新需求', '一年', 'plan256', 1, 1, 'white');

-- ----------------------------
-- Table structure for project_task
-- ----------------------------
DROP TABLE IF EXISTS `project_task`;
CREATE TABLE `project_task`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `finish_time` datetime(0) NULL DEFAULT NULL,
  `plan_id` int(11) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `task_outcome` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_user` int(11) NULL DEFAULT NULL,
  `task_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `finish_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `finish_days` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of project_task
-- ----------------------------
INSERT INTO `project_task` VALUES (4, '2018-12-13 10:14:24', 1, '2018-12-13 10:14:39', NULL, '完成用户模块', 1, 't123453', 'white', '1天');
INSERT INTO `project_task` VALUES (5, '2018-12-13 10:16:24', 1, '2018-12-13 10:16:32', NULL, 'asdasdas', 1, 'sadsa', 'white', '12天');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '超级管理员');
INSERT INTO `role` VALUES (2, '管理员');
INSERT INTO `role` VALUES (3, '普通用户');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `menu_id` int(11) NOT NULL,
  `role_id` int(11) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES (1001, 1);
INSERT INTO `role_menu` VALUES (1002, 1);
INSERT INTO `role_menu` VALUES (1003, 1);
INSERT INTO `role_menu` VALUES (1004, 1);
INSERT INTO `role_menu` VALUES (1001, 2);
INSERT INTO `role_menu` VALUES (1002, 2);
INSERT INTO `role_menu` VALUES (2001, 1);
INSERT INTO `role_menu` VALUES (2002, 1);
INSERT INTO `role_menu` VALUES (2003, 1);
INSERT INTO `role_menu` VALUES (2004, 1);
INSERT INTO `role_menu` VALUES (1003, 2);
INSERT INTO `role_menu` VALUES (1004, 2);
INSERT INTO `role_menu` VALUES (2001, 2);
INSERT INTO `role_menu` VALUES (2002, 2);
INSERT INTO `role_menu` VALUES (2003, 2);
INSERT INTO `role_menu` VALUES (2004, 2);
INSERT INTO `role_menu` VALUES (3001, 1);
INSERT INTO `role_menu` VALUES (3002, 1);
INSERT INTO `role_menu` VALUES (3003, 1);
INSERT INTO `role_menu` VALUES (3004, 1);
INSERT INTO `role_menu` VALUES (3001, 2);
INSERT INTO `role_menu` VALUES (2001, 3);
INSERT INTO `role_menu` VALUES (4001, 1);
INSERT INTO `role_menu` VALUES (4002, 1);
INSERT INTO `role_menu` VALUES (4003, 1);
INSERT INTO `role_menu` VALUES (4001, 1);
INSERT INTO `role_menu` VALUES (5001, 1);
INSERT INTO `role_menu` VALUES (5002, 1);
INSERT INTO `role_menu` VALUES (5003, 1);
INSERT INTO `role_menu` VALUES (5004, 1);
INSERT INTO `role_menu` VALUES (6001, 1);
INSERT INTO `role_menu` VALUES (6002, 1);
INSERT INTO `role_menu` VALUES (6003, 1);
INSERT INTO `role_menu` VALUES (6004, 1);
INSERT INTO `role_menu` VALUES (4001, 2);
INSERT INTO `role_menu` VALUES (6001, 2);
INSERT INTO `role_menu` VALUES (5001, 2);
INSERT INTO `role_menu` VALUES (4004, 1);
INSERT INTO `role_menu` VALUES (5001, 3);
INSERT INTO `role_menu` VALUES (4001, 3);
INSERT INTO `role_menu` VALUES (6001, 3);
INSERT INTO `role_menu` VALUES (7001, 1);
INSERT INTO `role_menu` VALUES (7002, 1);
INSERT INTO `role_menu` VALUES (7003, 1);
INSERT INTO `role_menu` VALUES (7004, 1);
INSERT INTO `role_menu` VALUES (2001, 3);
INSERT INTO `role_menu` VALUES (2002, 3);
INSERT INTO `role_menu` VALUES (2003, 3);
INSERT INTO `role_menu` VALUES (2004, 3);
INSERT INTO `role_menu` VALUES (4001, 3);
INSERT INTO `role_menu` VALUES (4002, 3);
INSERT INTO `role_menu` VALUES (4003, 3);
INSERT INTO `role_menu` VALUES (4003, 3);
INSERT INTO `role_menu` VALUES (5001, 3);
INSERT INTO `role_menu` VALUES (5002, 3);
INSERT INTO `role_menu` VALUES (5003, 3);
INSERT INTO `role_menu` VALUES (5004, 3);
INSERT INTO `role_menu` VALUES (6001, 3);
INSERT INTO `role_menu` VALUES (6002, 3);
INSERT INTO `role_menu` VALUES (6003, 3);
INSERT INTO `role_menu` VALUES (6004, 3);
INSERT INTO `role_menu` VALUES (7001, 3);
INSERT INTO `role_menu` VALUES (7002, 3);
INSERT INTO `role_menu` VALUES (7003, 3);
INSERT INTO `role_menu` VALUES (7004, 3);

-- ----------------------------
-- Table structure for role_user
-- ----------------------------
DROP TABLE IF EXISTS `role_user`;
CREATE TABLE `role_user`  (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_user
-- ----------------------------
INSERT INTO `role_user` VALUES (1, 1);
INSERT INTO `role_user` VALUES (2, 3);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `delete_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '21232f297a57a5a743894a0e4a801fc3', '942364283@qq.com', '1', '2018-12-19 22:59:51', '2018-12-19 22:59:51', '超级管理员', '1', 'http://pozitiv-news.ru/wp-content/uploads/2010/12/0715.jpg');
INSERT INTO `user` VALUES (2, 'meixian', '1eac2189971c5ea073f3f14f611d643d', 'meixian@qq.com', '1', '2018-12-20 11:45:54', '2018-12-20 11:45:54', '梅县项目管理', '1', 'https://www.gravatar.com/avatar/6560ed55e62396e40b34aac1e5041028');

SET FOREIGN_KEY_CHECKS = 1;
