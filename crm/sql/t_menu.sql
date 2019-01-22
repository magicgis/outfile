/*
Navicat MySQL Data Transfer

Source Server         : betterair
Source Server Version : 50527
Source Host           : localhost:3306
Source Database       : crm

Target Server Type    : MYSQL
Target Server Version : 50527
File Encoding         : 65001

Date: 2016-08-01 10:21:22
*/

SET FOREIGN_KEY_CHECKS=0;

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
INSERT INTO `t_menu` VALUES ('10000', '0', '功能模块', '', 'N', 'Y', '1', '0');
INSERT INTO `t_menu` VALUES ('20000', '0', '商品库管理', '', 'N', 'Y', '2', '0');
INSERT INTO `t_menu` VALUES ('30000', '0', '系统管理', '', 'N', 'Y', '3', '0');
INSERT INTO `t_menu` VALUES ('10100', '10000', '销售', '', 'Y', 'Y', '1', '10000');
INSERT INTO `t_menu` VALUES ('10200', '10000', '采购', '', 'Y', 'Y', '2', '10000');
INSERT INTO `t_menu` VALUES ('10300', '10000', '物流', '', 'Y', 'Y', '3', '10000');
INSERT INTO `t_menu` VALUES ('10400', '10000', '财务', '', 'Y', 'Y', '4', '10000');
INSERT INTO `t_menu` VALUES ('10900', '10000', '系统配置', '', 'Y', 'Y', '3', '10000');
INSERT INTO `t_menu` VALUES ('11000', '10000', 'demo', '', 'Y', 'Y', '4', '10000');
INSERT INTO `t_menu` VALUES ('10101', '10100', '客户询价管理', '/sales/clientinquiry/clientinquiryList', 'Y', 'Y', '1', '10000');
INSERT INTO `t_menu` VALUES ('10102', '10100', '客户报价管理', '/clientquote/viewlist', 'Y', 'Y', '2', '10000');
INSERT INTO `t_menu` VALUES ('10103', '10100', '客户订单管理 ', '/sales/clientorder/tolist', 'Y', 'Y', '3', '10000');
INSERT INTO `t_menu` VALUES ('10104', '10100', '未发货清单', '/sales/clientorder/toUnFinishWork', 'Y', 'Y', '4', '10000');
INSERT INTO `t_menu` VALUES ('10105', '10100', '客户询价单到期提醒', '/dueremind/viewListByInquiry?type=marketing', 'Y', 'Y', '5', '10000');
INSERT INTO `t_menu` VALUES ('10106', '10100', '客户订单到期提醒', '/dueremind/viewListByClientOrder', 'Y', 'Y', '6', '10000');
INSERT INTO `t_menu` VALUES ('10201', '10200', '新增供应商询价', '/purchase/supplierinquiry/inquiryList', 'Y', 'Y', '1', '10000');
INSERT INTO `t_menu` VALUES ('10202', '10200', '供应商询价管理', '/purchase/supplierinquiry/SupplierInquiryManage', 'Y', 'Y', '2', '10000');
INSERT INTO `t_menu` VALUES ('10203', '10200', '供应商报价管理', '/supplierquote/supplierquotelist', 'Y', 'Y', '3', '10000');
INSERT INTO `t_menu` VALUES ('10204', '10200', '新增供应商订单', '/purchase/supplierorder/addSupplierOrderPage', 'Y', 'Y', '4', '10000');
INSERT INTO `t_menu` VALUES ('10205', '10200', '供应商订单管理', '/purchase/supplierorder/SupplierOrder', 'Y', 'Y', '5', '10000');
INSERT INTO `t_menu` VALUES ('10207', '10200', '未到货清单', '/purchase/supplierorder/unFinishWork', 'Y', 'Y', '6', '10000');
INSERT INTO `t_menu` VALUES ('10208', '10200', '供应商询价单到期提醒', '/dueremind/viewListByInquiry?type=purchase', 'Y', 'Y', '7', '10000');
INSERT INTO `t_menu` VALUES ('10209', '10200', '供应商订单到期提醒', '/dueremind/viewListBySupplierOrder', 'Y', 'Y', '8', '10000');
INSERT INTO `t_menu` VALUES ('10210', '10200', '供应商到货清单', '/supplierquote/supplierarrivallist', 'Y', 'Y', '9', '10000');
INSERT INTO `t_menu` VALUES ('10213	', '10200', '供应商询价单统计', '/supplierstatistic/list', 'Y', 'Y', '11', '10000');
INSERT INTO `t_menu` VALUES ('10214	', '10200', '付款管理', '/finance/importpackagepayment/toPaymenyList', 'Y', 'Y', '114', '10000');
INSERT INTO `t_menu` VALUES ('10301', '10300', '入库管理', '/importpackage/importpackagelist', 'Y', 'Y', '1', '10000');
INSERT INTO `t_menu` VALUES ('10901', '10900', '客户管理', '/system/clientmanage/toClientList', 'Y', 'Y', '1', '10000');
INSERT INTO `t_menu` VALUES ('10902', '10900', '供应商管理', '/suppliermanage/viewList', 'Y', 'Y', '2', '10000');
INSERT INTO `t_menu` VALUES ('11001', '11000', '动态列demo', '/demo/excelMgmtDemo', 'Y', 'Y', '1', '10000');
INSERT INTO `t_menu` VALUES ('11002', '11000', 'fj', '/demo/uploadAttachmentDemo', 'Y', 'Y', '2', '10000');
INSERT INTO `t_menu` VALUES ('20100', '20000', 'NSNCenter', '', 'N', 'Y', '1', '20000');
INSERT INTO `t_menu` VALUES ('20101', '20100', '按CAGE查询', '/rfqstock/nsncenter/listByCage', 'Y', 'Y', '1', '20000');
INSERT INTO `t_menu` VALUES ('20102', '20100', '按件号查询', '/rfqstock/nsncenter/listByNsnPart', 'Y', 'Y', '2', '20000');
INSERT INTO `t_menu` VALUES ('30101', '30000', '用户管理', '/xtgl/user/userlist?type=all', 'Y', 'Y', '1', '30000');
INSERT INTO `t_menu` VALUES ('30102', '30000', '菜单管理', '/xtgl/menu/menulist', 'Y', 'Y', '2', '30000');
INSERT INTO `t_menu` VALUES ('30103', '30000', '权限管理', '/xtgl/role/rolelist', 'Y', 'Y', '3', '30000');
INSERT INTO `t_menu` VALUES ('10303', '10300', '客户箱单管理', '/storage/clientship/toClientShip', 'Y', 'Y', '3', '10000');
INSERT INTO `t_menu` VALUES ('10302', '10300', '出库管理', '/storage/exportpackage/toExportList', 'Y', 'Y', '2', '10000');
INSERT INTO `t_menu` VALUES ('10304', '10300', '库存明细', '/storage/detail/toStorageDetail', 'Y', 'Y', '4', '10000');
INSERT INTO `t_menu` VALUES ('10306', '10300', '出库指令', '/storage/exportpackage/toExportPackageInstructions', 'Y', 'Y', '6', '10000');
INSERT INTO `t_menu` VALUES ('10308', '10300', '出库流水', '/storage/exportpackage/toExportPackageFlowList', 'Y', 'Y', '108', '10000');
INSERT INTO `t_menu` VALUES ('10401', '10400', '收款管理', '/finance/clientincome/toIncome', 'Y', 'Y', '1', '10000');
INSERT INTO `t_menu` VALUES ('10402', '10400', '形式发票管理', '/finance/invoice/proformainvoicelist', 'Y', 'Y', '2', '10000');
INSERT INTO `t_menu` VALUES ('10403', '10400', '收款发票管理', '/finance/invoice/receivablesinvoicelist', 'Y', 'Y', '3', '10000');
INSERT INTO `t_menu` VALUES ('10404', '10400', '利润表', '/clientquote/profitStatistics', 'Y', 'Y', '4', '10000');
INSERT INTO `t_menu` VALUES ('10405', '10400', '付款管理', '/finance/importpackagepayment/toList', 'Y', 'Y', '105', '10000');
INSERT INTO `t_menu` VALUES ('10406', '10400', '欠款管理', '/finance/importpackagepayment/toDebtList', 'Y', 'Y', '106', '10000');
INSERT INTO `t_menu` VALUES ('10305', '10300', '库存流水', '/importpackage/storageflowlist', 'Y', 'Y', '5', '10000');
INSERT INTO `t_menu` VALUES ('10107', '10100', '部件资料', '/market/partsinformation/tomarketinformation', 'Y', 'Y', '7', '10000');
INSERT INTO `t_menu` VALUES ('10212', '10200', '部件资料', '/market/partsinformation/topurchaseinformation', 'Y', 'Y', '9', '10000');
INSERT INTO `t_menu` VALUES ('10211', '10200', '未订货', '/purchase/supplierorder/toNoOrder', 'Y', 'Y', '10', '10000');
INSERT INTO `t_menu` VALUES ('10903', '10900', '汇率管理', '/system/exchangerate/toExchangeRate', 'Y', 'Y', '3', '10000');
INSERT INTO `t_menu` VALUES ('10904', '10900', '机型管理', '/system/airmanage/toAirManage', 'Y', 'Y', '4', '10000');
INSERT INTO `t_menu` VALUES ('10905', '10900', '商业类型管理', '/system/businessmanage/toBusinessManage', 'Y', 'Y', '5', '10000');
INSERT INTO `t_menu` VALUES ('10906', '10900', '竞争对手管理', '/system/competitormanage/toCompetitorManage', 'Y', 'Y', '6', '10000');
INSERT INTO `t_menu` VALUES ('10907', '10900', '个人信息', '/xtgl/user/userlist?type=personal', 'Y', 'Y', '107', '10000');

INSERT INTO `t_menu` VALUES ('20200', '20000', '商品库查询', '', 'N', 'Y', '1', '20000');
INSERT INTO `t_menu` VALUES ('20201', '20200', '按件号查询', '/stock/search/listByNsnPart', 'Y', 'Y', '2', '20000');
INSERT INTO `t_menu` VALUES ('20202', '20200', '按CAGE查询', '/stock/search/listByCage', 'Y', 'Y', '1', '20000');
INSERT INTO `t_menu` VALUES ('10108', '10100', '客户询价单统计', '/sales/clientorder/toStatistics', 'Y', 'Y', '108', '10000');
INSERT INTO `t_menu` VALUES ('10109', '10100', '订单货款到期提醒', '/sales/clientorder/toDeadlineOrder', 'Y', 'Y', '109', '10000');
INSERT INTO `t_menu` VALUES ('10307', '10300', '供应商发货清单', '/purchase/supplierorder/toImportPrepare', 'Y', 'Y', '107', '10000');
INSERT INTO `t_menu`  VALUES(30104, 30000, '流程部署', '/workflow/deploymentPage', 'Y', 'Y', '104', 30000);
INSERT INTO `t_menu` VALUES ('10309', '10300', '在途库存', '/storage/onpassagestorage/toListPage', 'Y', 'Y', '109', '10000');

-- 2-7
INSERT INTO `t_menu` VALUES ('10215	', '10200', '付款提醒', '/finance/importpackagepayment/toPaymentWarn', 'Y', 'Y', '113', '10000');

-- 2-16
INSERT INTO `t_menu` VALUES ('10908', '10900', '仓库地址管理', '/system/storehouseaddress/toStorehouseAddressList', 'Y', 'Y', '108', '10000');

-- 2-20
INSERT INTO `t_menu` VALUES ('10310', '10300', '供应商寄卖', '/storage/suppliercommissionsale/toList', 'Y', 'Y', '110', '10000');
INSERT INTO `t_menu` VALUES ('10216', '10200', '供应商寄卖', '/storage/suppliercommissionsale/toList', 'Y', 'Y', '116', '10000');

-- 3-5
INSERT INTO `t_menu` VALUES ('10110','10100', '历史采购价', '/order/historicalorderprice/toClientOrderList', 'Y', 'Y', '110', '10000');

-- 3-6
INSERT INTO `t_menu` VALUES ('10909', '10900', '客户固定价目表', '/system/staticprice/toClientList', 'Y', 'Y', '109', '10000');

-- 3-7
INSERT INTO `t_menu` VALUES ('10910', '10900', '供应商固定价目表', '/system/staticprice/toSupplierList', 'Y', 'Y', '110', '10000');

-- 3-13
INSERT INTO `t_menu` VALUES ('10911', '10900', '类型管理', '/system/parttype/toElementList', 'Y', 'Y', '111', '10000');

--5-11
INSERT INTO `t_menu` VALUES ('10111', '10100', '客户预订单管理 ', '/market/clientweatherorder/tolist', 'Y', 'Y', '11', '10000');

--5-22
INSERT INTO `t_menu` VALUES ('10912', '10900', '报价银行费用管理', '/system/quoteBankCharges/toQuoteBankCharges', 'Y', 'Y', '112', '10000');
INSERT INTO `t_menu` VALUES ('10913', '10900', '订单银行费用管理', '/system/orderBankCharges/toOrderBankCharges', 'Y', 'Y', '113', '10000');
-- 5-22
INSERT INTO `t_menu` VALUES ('10311', '10300', '未换位置提醒', '/importpackage/toUnchangeLocationPage', 'Y', 'Y', '111', '10000');

--6-6
INSERT INTO `t_menu`  VALUES ('10914', '10900', '流程任务管理', '/workflow/toProcessList', 'Y', 'Y', '114', '10000');

-- 6-21
INSERT INTO `t_menu` VALUES ('10312', '10300', '交换库管理', '/storage/exchangeimport/toList', 'Y', 'Y', '112', '10000');

INSERT INTO `t_menu` VALUES ('10112', '10100', '交换件未发货', '/storage/exchangeimport/toWarnList', 'Y', 'Y', '12', '10000');

-- 7-24
INSERT INTO `t_menu` VALUES ('10217', '10200', '历史报价', '/purchase/supplierinquiry/historyquote', 'Y', 'Y', '117', '10000');

INSERT INTO `t_menu`  VALUES ('10915', '10900', '利润管理', '/contractReview/toClientProfitmargin', 'Y', 'Y', '115', '10000');

-- 8-24
INSERT INTO `t_menu` VALUES ('10113', '10100', '历史报价', '/clientquote/historyQuote', 'Y', 'Y', '113', '10000');

-- 9-20
INSERT INTO `t_menu` VALUES ('10218', '10200', '爬虫情况', '/supplierquote/toCrawList', 'Y', 'Y', '118', '10000');

-- 4-27
INSERT INTO `t_menu` VALUES ('10219', '10200', 'MPI', '/purchase/mpi/toList', 'Y', 'Y', '119', '10000');

-- 5-4
INSERT INTO `t_menu` VALUES ('10220', '10300', '非系统库存', '/storage/unknowstorage/toList', 'Y', 'Y', '113', '10000');

-- 5-5
INSERT INTO `t_menu` VALUES ('10221', '10300', '退货处理', '/storage/sendbackmessage/toList', 'Y', 'Y', '114', '10000');

-- 8-7
INSERT INTO `t_menu` VALUES ('10222', '10200', 'StockMarket爬虫', '/storage/suppliercommissionsale/toStockCrawlMessage', 'Y', 'Y', '120', '10000');

-- 8-10
INSERT INTO `t_menu`  VALUES ('10916', '10900', '机型管理', '/system/airmanage/toAirManageForStockMarket', 'Y', 'Y', '116', '10000');

-- 8-14
INSERT INTO `t_menu` VALUES ('30105', '10200', '资产包', '/storage/assetpackage/toList', 'Y', 'Y', '120', '10000');
