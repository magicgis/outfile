/*
Navicat MySQL Data Transfer

Source Server         : betterair
Source Server Version : 50527
Source Host           : localhost:3306
Source Database       : crm

Target Server Type    : MYSQL
Target Server Version : 50527
File Encoding         : 65001

Date: 2016-08-01 10:19:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for r_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `r_role_menu`;
CREATE TABLE `r_role_menu` (
  `MENU_ID` int(11) NOT NULL,
  `ROLE_ID` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of r_role_menu
-- ----------------------------
INSERT INTO `r_role_menu` VALUES ('10000', '0');
INSERT INTO `r_role_menu` VALUES ('20000', '0');
INSERT INTO `r_role_menu` VALUES ('30000', '0');
INSERT INTO `r_role_menu` VALUES ('10100', '0');
INSERT INTO `r_role_menu` VALUES ('10200', '0');
INSERT INTO `r_role_menu` VALUES ('10300', '0');
INSERT INTO `r_role_menu` VALUES ('10900', '0');
INSERT INTO `r_role_menu` VALUES ('11000', '0');
INSERT INTO `r_role_menu` VALUES ('10101', '0');
INSERT INTO `r_role_menu` VALUES ('10102', '0');
INSERT INTO `r_role_menu` VALUES ('10103', '0');
INSERT INTO `r_role_menu` VALUES ('10104', '0');
INSERT INTO `r_role_menu` VALUES ('10105', '0');
INSERT INTO `r_role_menu` VALUES ('10106', '0');
INSERT INTO `r_role_menu` VALUES ('10201', '0');
INSERT INTO `r_role_menu` VALUES ('10202', '0');
INSERT INTO `r_role_menu` VALUES ('10203', '0');
INSERT INTO `r_role_menu` VALUES ('10204', '0');
INSERT INTO `r_role_menu` VALUES ('10205', '0');
INSERT INTO `r_role_menu` VALUES ('10207', '0');
INSERT INTO `r_role_menu` VALUES ('10208', '0');
INSERT INTO `r_role_menu` VALUES ('10209', '0');
INSERT INTO `r_role_menu` VALUES ('10301', '0');
INSERT INTO `r_role_menu` VALUES ('10901', '0');
INSERT INTO `r_role_menu` VALUES ('10902', '0');
INSERT INTO `r_role_menu` VALUES ('11001', '0');
INSERT INTO `r_role_menu` VALUES ('11002', '0');
INSERT INTO `r_role_menu` VALUES ('20100', '0');
INSERT INTO `r_role_menu` VALUES ('20101', '0');
INSERT INTO `r_role_menu` VALUES ('20102', '0');
INSERT INTO `r_role_menu` VALUES ('30101', '0');
INSERT INTO `r_role_menu` VALUES ('30102', '0');
INSERT INTO `r_role_menu` VALUES ('30103', '0');

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_menu_id` int(11) DEFAULT NULL,
  `menu_name` varchar(255) DEFAULT NULL,
  `menu_url` varchar(255) DEFAULT NULL,
  `is_leaf` char(1) DEFAULT NULL,
  `yxbz` char(1) DEFAULT 'Y',
  `menu_order` varchar(4) DEFAULT NULL,
  `root_menu_id` int(11) DEFAULT '0',
  PRIMARY KEY (`menu_id`)
) ENGINE=MyISAM AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES ('1', '0', 'ϵͳ����', '', 'N', 'Y', '2', '0');
INSERT INTO `t_menu` VALUES ('2', '1', '�û�����', '/xtgl/user/userlist', 'Y', 'Y', null, '1');
INSERT INTO `t_menu` VALUES ('3', '1', '�˵�����', '/xtgl/menu/menulist', 'Y', 'Y', null, '1');
INSERT INTO `t_menu` VALUES ('4', '0', '����ģ��', '', 'N', 'Y', '1', '0');
INSERT INTO `t_menu` VALUES ('5', '4', '����', '', 'Y', 'Y', '1', '4');
INSERT INTO `t_menu` VALUES ('6', '5', '�ͻ�ѯ�۹���', '/sales/clientinquiry/clientinquiryList', 'Y', 'Y', '1', '4');
INSERT INTO `t_menu` VALUES ('12', '11', '������Ӧ��ѯ��', '/purchase/supplierinquiry/inquiryList', 'Y', 'Y', '1', '4');
INSERT INTO `t_menu` VALUES ('9', '5', '�ͻ����۹���', '/clientquote/viewlist', 'Y', 'Y', '2', '4');
INSERT INTO `t_menu` VALUES ('11', '4', '�ɹ�', '', 'Y', 'Y', '2', '4');
INSERT INTO `t_menu` VALUES ('13', '11', '��Ӧ��ѯ�۹���', '/purchase/supplierinquiry/SupplierInquiryManage', 'Y', 'Y', '2', '4');
INSERT INTO `t_menu` VALUES ('17', '11', '��Ӧ�̱��۹���', '/supplierquote/supplierquotelist', 'Y', 'Y', '3', '4');
INSERT INTO `t_menu` VALUES ('15', '5', '�ͻ��������� ', '/sales/clientorder/tolist', 'Y', 'Y', '3', '4');
INSERT INTO `t_menu` VALUES ('16', '11', '������Ӧ�̶���', '/purchase/supplierorder/addSupplierOrderPage', 'Y', 'Y', '4', '4');
INSERT INTO `t_menu` VALUES ('18', '11', '������', '/importpackage/importpackagelist', 'Y', 'Y', '6', '4');
INSERT INTO `t_menu` VALUES ('19', '11', '��Ӧ�̶�������', '/purchase/supplierorder/SupplierOrder', 'Y', 'Y', '5', '4');
INSERT INTO `t_menu` VALUES ('20', '4', 'demo', '', 'Y', 'Y', '3', '4');
INSERT INTO `t_menu` VALUES ('21', '20', '��̬��demo', '/demo/excelMgmtDemo', 'Y', 'Y', '1', '4');
INSERT INTO `t_menu` VALUES ('22', '20', 'fj', '/demo/uploadAttachmentDemo', 'Y', 'Y', '2', '4');
INSERT INTO `t_menu` VALUES ('23', '5', 'δ��ɹ���', '/sales/clientorder/toUnFinishWork', 'Y', 'Y', '4', '4');
INSERT INTO `t_menu` VALUES ('24', '11', 'δ��ɹ���', '/purchase/supplierorder/unFinishWork', 'Y', 'Y', '7', '4');
INSERT INTO `t_menu` VALUES ('25', '4', 'ϵͳ����', '', 'Y', 'Y', '4', '4');
INSERT INTO `t_menu` VALUES ('26', '25', '�ͻ�����', '/system/clientmanage/toClientList', 'Y', 'Y', '1', '4');
INSERT INTO `t_menu` VALUES ('27', '5', '��������', '/dueremind/viewListByInquiry?type=marketing', 'Y', 'Y', '5', '4');
INSERT INTO `t_menu` VALUES ('28', '11', '��������', '/dueremind/viewListByInquiry?type=purchase', 'Y', 'Y', '8', '4');
INSERT INTO `t_menu` VALUES ('29', '5', '������������', '/dueremind/viewListByClientOrder', 'Y', 'Y', '6', '4');
INSERT INTO `t_menu` VALUES ('30', '11', '������������', '/dueremind/viewListBySupplierOrder', 'Y', 'Y', '9', '4');
INSERT INTO `t_menu` VALUES ('31', '25', '��Ӧ�̹���', '/suppliermanage/viewList', 'Y', 'Y', '2', '4');
