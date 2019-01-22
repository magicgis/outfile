/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : localhost:3306
 Source Schema         : backend

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 18/12/2018 19:44:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
INSERT INTO `menu` VALUES (2001, 'role', '角色管理', 'role:list', '查看');
INSERT INTO `menu` VALUES (2002, 'role', '角色管理', 'role:add', '新增');
INSERT INTO `menu` VALUES (2003, 'role', '角色管理', 'role:update', '修改');
INSERT INTO `menu` VALUES (2004, 'role', '角色管理', 'role:delete', '删除');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '超级管理员');
INSERT INTO `role` VALUES (2, '管理员');
INSERT INTO `role` VALUES (3, '普通用户');
INSERT INTO `role` VALUES (4, '开发工程师');
INSERT INTO `role` VALUES (5, '需求分析师');
INSERT INTO `role` VALUES (6, '软件架构师');
INSERT INTO `role` VALUES (7, '项目经理');

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
INSERT INTO `role_menu` VALUES (2001, 1);
INSERT INTO `role_menu` VALUES (2002, 1);
INSERT INTO `role_menu` VALUES (2003, 1);
INSERT INTO `role_menu` VALUES (2004, 1);

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
INSERT INTO `role_user` VALUES (3, 2);
INSERT INTO `role_user` VALUES (4, 3);
INSERT INTO `role_user` VALUES (5, 3);
INSERT INTO `role_user` VALUES (9, 2);
INSERT INTO `role_user` VALUES (10, 2);
INSERT INTO `role_user` VALUES (11, 2);
INSERT INTO `role_user` VALUES (12, 3);

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
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '21232f297a57a5a743894a0e4a801fc3', '942364283@qq.com', '1', '2018-12-17 00:50:26', '2018-12-17 00:50:26', '超级管理员', '1', NULL);
INSERT INTO `user` VALUES (2, 'white', 'e10adc3949ba59abbe56e057f20f883e', 'white@naswork.com', '1', '2018-12-17 00:50:58', '2018-12-17 00:50:58', '普通用户', '1', NULL);
INSERT INTO `user` VALUES (3, 'nick', 'e10adc3949ba59abbe56e057f20f883e', 'nick@naswork.com', '1', '2018-12-17 00:51:30', '2018-12-17 00:51:30', '梁总', '1', NULL);
INSERT INTO `user` VALUES (4, 'jordar', 'e10adc3949ba59abbe56e057f20f883e', 'jordan@naswork.com', '1', '2018-12-17 00:51:32', '2018-12-17 00:51:32', '我是帅帅的达达', '1', NULL);
INSERT INTO `user` VALUES (5, 'winnie', 'e10adc3949ba59abbe56e057f20f883e', 'winnie@naswork.com', '0', '2018-12-17 00:51:34', '2018-12-17 00:51:34', '我是女的', '1', NULL);
INSERT INTO `user` VALUES (9, 'guicheng', 'e10adc3949ba59abbe56e057f20f883e', 'guichneg@qq.com', '1', '2018-12-17 00:51:36', '2018-12-17 00:51:36', '贵大人', '1', NULL);
INSERT INTO `user` VALUES (10, 'Elvis', 'e10adc3949ba59abbe56e057f20f883e', 'elvis@naswork', '1', '2018-12-17 00:51:39', '2018-12-17 00:51:39', '邓总', '1', NULL);
INSERT INTO `user` VALUES (11, 'Doudou', 'e10adc3949ba59abbe56e057f20f883e', 'doudou@naswork', '1', '2018-12-17 00:51:41', '2018-12-17 00:51:41', '麦总', '1', NULL);

SET FOREIGN_KEY_CHECKS = 1;
