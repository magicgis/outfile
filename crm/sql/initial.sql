CREATE TABLE `t_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_menu_id` int(11) DEFAULT NULL,
  `menu_name` varchar(255) DEFAULT NULL,
  `menu_url` varchar(255) DEFAULT NULL,
  `is_leaf` char(1) DEFAULT NULL,
  `yxbz` char(1) DEFAULT 'Y',
  `menu_order` varchar(4) DEFAULT NULL,
  `root_menu_id` int(11) DEFAULT 0,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into t_menu (menu_id,parent_menu_id,menu_name,menu_url,is_leaf) values(1,0,'系统管理','','N');

insert into t_menu (parent_menu_id,menu_name,menu_url,is_leaf,root_menu_id) values(1,'用户管理','/xtgl/user/userlist','Y',1);
insert into t_menu (parent_menu_id,menu_name,menu_url,is_leaf,root_menu_id) values(1,'菜单管理','/xtgl/menu/menulist','Y',1);
CREATE TABLE `t_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) DEFAULT NULL,
  `password` varchar(45) DEFAULT '',
  `login_name` varchar(255) DEFAULT NULL,
  `information` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `fax` varchar(32) DEFAULT NULL,
  `phone` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `t_role` (
  `role_id` int NOT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  `role_comment` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
);
CREATE TABLE R_ROLE_USER (
   USER_ID INT not null,
   ROLE_ID INT NOT NULL
);	
insert into t_role (role_id, role_name) values(0, '管理员');
insert into t_role (role_id, role_name) values(1, '经理');
insert into t_role (role_id, role_name) values(2, '员工');

CREATE TABLE R_ROLE_MENU (
   MENU_ID INT not null,
   ROLE_ID INT NOT NULL
);	
insert into r_role_menu (role_id,menu_id) values(0,1);
insert into r_role_menu (role_id,menu_id) values(0,2);
insert into r_role_menu (role_id,menu_id) values(0,3);

insert into t_user (user_id, user_name, password, login_name) values (1,'testuser','t1','t1');
insert into r_role_user (role_id, user_id) values(0,1);


CREATE TABLE `T_GY_FJ` (
  `FJ_ID` int(11) NOT NULL AUTO_INCREMENT,
  `YW_ID` int(11) NOT NULL,
  `YW_TABLE_NAME` varchar(255) DEFAULT NULL,
  `YW_TABLE_PK_NAME` varchar(255) DEFAULT NULL,
  `FJ_NAME` varchar(255) DEFAULT NULL,
  `FJ_PATH` varchar(255) DEFAULT NULL,
  `FJ_TYPE` varchar(10) DEFAULT NULL,
  `FJ_LENGTH` int(11) DEFAULT NULL,
  `USER_ID` int(11) DEFAULT NULL,
  `LRSJ` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`FJ_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `T_GY_EXCEL` (
  `EXCEL_FILE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `YW_ID` int(11) NOT NULL,
  `YW_TABLE_NAME` varchar(255) DEFAULT NULL,
  `YW_TABLE_PK_NAME` varchar(255) DEFAULT NULL,
  `EXCEL_FILE_NAME` varchar(255) DEFAULT NULL,
  `EXCEL_FILE_PATH` varchar(255) DEFAULT NULL,
  `EXCEL_TYPE` varchar(10) DEFAULT NULL,
  `EXCEL_TEMPLATE_NAME` varchar(255) DEFAULT NULL,
  `EXCEL_FILE_LENGTH` int(11) DEFAULT NULL,
  `USER_ID` int(11) DEFAULT NULL,
  `LRSJ` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `XH` int(11) NOT NULL,
  PRIMARY KEY (`EXCEL_FILE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Add alternative part number for 3 tables: client_inquiry_element, import_package_element, supplier_quote_element
ALTER TABLE `client_inquiry_element` ADD COLUMN `ALTER_PART_NUMBER` varchar(255) DEFAULT NULL;
ALTER TABLE `import_package_element` ADD COLUMN `ALTER_PART_NUMBER` varchar(255) DEFAULT NULL;
ALTER TABLE `supplier_quote_element` ADD COLUMN `ALTER_PART_NUMBER` varchar(255) DEFAULT NULL;

ALTER TABLE `client_inquiry_element` ADD COLUMN `CSN` int(11) NOT NULL;
ALTER TABLE `client_inquiry_element` MODIFY COLUMN `REMARK` varchar(1024) DEFAULT NULL;

-- Add location list table for storage
CREATE TABLE `import_storage_location_list` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LOCATION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);


-- Add status column for export package element for it is already export, or preparing or approval status.
ALTER TABLE `export_package_element` ADD COLUMN `STATUS` int NOT NULL DEFAULT '0';

-- Add 6 columns into table client_quote for payment rule,
ALTER TABLE `client_quote` ADD COLUMN `PREPAY_RATE` double(10,4) NOT NULL DEFAULT '0.0000';
ALTER TABLE `client_quote` ADD COLUMN `SHIP_PAY_RATE` double(10,4) NOT NULL DEFAULT '0.0000';
ALTER TABLE `client_quote` ADD COLUMN `SHIP_PAY_PERIOD` varchar(255) NOT NULL DEFAULT '0';
ALTER TABLE `client_quote` ADD COLUMN `RECEIVE_PAY_RATE` double(10,4) NOT NULL DEFAULT '0.0000';
ALTER TABLE `client_quote` ADD COLUMN `RECEIVE_PAY_PERIOD` varchar(255) NOT NULL DEFAULT '0';

-- Add leadtime into table client_quote_element for how long it can be shipped to client,
ALTER TABLE `client_quote_element` ADD COLUMN `LEAD_TIME` int NOT NULL DEFAULT '0';

-- Add 5 columns into table client_order for payment rule,
ALTER TABLE `client_order` ADD COLUMN `PREPAY_RATE` double(10,4) NOT NULL DEFAULT '0.0000';
ALTER TABLE `client_order` ADD COLUMN `SHIP_PAY_RATE` double(10,4) NOT NULL DEFAULT '0.0000';
ALTER TABLE `client_order` ADD COLUMN `SHIP_PAY_PERIOD` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `client_order` ADD COLUMN `RECEIVE_PAY_RATE` double(10,4) NOT NULL DEFAULT '0.0000';
ALTER TABLE `client_order` ADD COLUMN `RECEIVE_PAY_PERIOD` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `client_order` MODIFY COLUMN `TERMS` varchar(11) DEFAULT NULL;

-- add table showing the relationship of part_number and alter_part_number
CREATE TABLE `client_inquiry_alter_element` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_INQUIRY_ELEMENT_ID` int(11) NOT NULL,
  `ALTER_PART_NUMBER` varchar(255) NOT NULL,
  `ELEMENT_ID` int(11) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- add table client some columns
alter TABLE client add COLUMN `PREPAY_RATE` double(10,4) NOT NULL DEFAULT '0.0000';
alter TABLE client add COLUMN `SHIP_PAY_RATE` double(10,4) NOT NULL DEFAULT '0.0000';
alter TABLE client add COLUMN `SHIP_PAY_PERIOD` int(11) NOT NULL DEFAULT '0',
alter TABLE client add COLUMN `RECEIVE_PAY_RATE` double(10,4) NOT NULL DEFAULT '0.0000';
alter TABLE client add COLUMN `RECEIVE_PAY_PERIOD` int(11) NOT NULL DEFAULT '0';
alter TABLE client add COLUMN `OWNER`  int(11) NOT NULL ;
alter TABLE client add COLUMN `CREATE_DATE` date NOT NULL ;
alter TABLE client add COLUMN `CLIENT_STATUS_ID` int(11) NOT NULL ;
alter TABLE client add COLUMN `TAX_RETRUN` int(2) NOT NULL ;
alter TABLE client add COLUMN `CLIENT_LEVEL_ID` int(11) DEFAULT NULL;
alter TABLE client add COLUMN `CLIENT_STAGE` varchar(255) DEFAULT NULL;
alter TABLE client add COLUMN `CLIENT_SOURCE` varchar(255) DEFAULT NULL;
alter TABLE client add COLUMN `CLIENT_ABILITY` varchar(255) DEFAULT NULL;
alter TABLE client add COLUMN `CLIENT_SHORT_NAME` varchar(255) DEFAULT NULL;
alter TABLE client add COLUMN `CLIENT_TYPE` varchar(255) DEFAULT NULL;
alter TABLE client add COLUMN `CLIENT_SHIP_WAY` int(11) DEFAULT NULL;
alter TABLE client MODIFY COLUMN `CODE` varchar(8) DEFAULT NULL;

ALTER TABLE `SUPPLIER` MODIFY COLUMN `UPDATE_TIMESTAMP` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;-- 更新时间
ALTER TABLE `SUPPLIER` ADD COLUMN `SUPPLER_SERIAL_NUMBER` VARCHAR(255) NULL ;-- 供应商编号
ALTER TABLE `SUPPLIER` ADD COLUMN `OWNER`  VARCHAR(255) NULL;-- 拥有人
ALTER TABLE `SUPPLIER` ADD COLUMN `SUPPLIER_STATUS` VARCHAR(255) NULL ;-- 供应商状态
ALTER TABLE `SUPPLIER` ADD COLUMN `SUPPLIER_ABBREVIATION`  VARCHAR(255) NULL ;-- 供应商简称
ALTER TABLE `SUPPLIER` ADD COLUMN `SUPPLIER_FULL_NAME` VARCHAR(255) NULL;-- 供应商全称
ALTER TABLE `SUPPLIER` ADD COLUMN `SUPPLIER_CLASSIFY` VARCHAR(255) NULL;-- 供应商归类
ALTER TABLE `SUPPLIER` ADD COLUMN `TAX_REIMBURSEMENT` VARCHAR(255) NULL;-- 是否可退税
ALTER TABLE `SUPPLIER` ADD COLUMN `SUPPLIER_GRADE` VARCHAR(255) NULL;-- 供应商等级
ALTER TABLE `SUPPLIER` ADD COLUMN `SUPPLIER_PHASES` VARCHAR(255) NULL;-- 供应商阶段
ALTER TABLE `SUPPLIER` ADD COLUMN `SUPPLIER_SOURCE` VARCHAR(255) NULL;-- 供应商来源
ALTER TABLE `SUPPLIER` ADD COLUMN `COMPETENCE_SCOPE` VARCHAR(255) NULL;-- 能力范围
ALTER TABLE `SUPPLIER` ADD COLUMN `DATE_CREATED` TIMESTAMP NOT NULL DEFAULT NULL;-- 创建时间
ALTER TABLE `SUPPLIER` ADD COLUMN `PAYMENT_RULE` VARCHAR(255) NULL;-- 付款规则
ALTER TABLE `SUPPLIER` ADD COLUMN `BANK_PHONE` VARCHAR(255) NULL;-- 银行电话
ALTER TABLE `SUPPLIER` ADD COLUMN `ACCOUNT_NAME` VARCHAR(255) NULL;-- 开户名
ALTER TABLE `SUPPLIER` ADD COLUMN `ENGLISH_NAME`  VARCHAR(255) NULL;-- 英文名
ALTER TABLE `SUPPLIER` ADD COLUMN `BANK_ADDRESS` VARCHAR(255) NULL;-- 银行地址
ALTER TABLE `SUPPLIER` ADD COLUMN `old_code` VARCHAR(8) NULL;-- 旧系统编号

CREATE TABLE `supplier_biz_relation` (
  `SUPPLIER_ID` int(11) NOT NULL,
  `BIZ_ID` int(11) NOT NULL,
  PRIMARY KEY (`SUPPLIER_ID`,`BIZ_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `supplier_contact` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUPPLIER_ID` int(11) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `APPELLATION` varchar(255) DEFAULT NULL,
  `SEX_ID` int(11) NOT NULL,
  `POSITION` varchar(255) DEFAULT NULL,
  `BIRTHDAY` datetime DEFAULT NULL,
  `DEPARTMENT` varchar(255) DEFAULT NULL,
  `PHONE` varchar(32) DEFAULT NULL,
  `MOBILE` varchar(32) DEFAULT NULL,
  `FAX` varchar(100) DEFAULT NULL,
  `CREATION_DATE` datetime DEFAULT NULL,
  `EMAIL` varchar(255) DEFAULT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- add table for saving client's bank message
CREATE TABLE `client_bank_message` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_ID` int(11) NOT NULL,
  `BANK_ACCOUNT_NUMBER` int(20) NOT NULL,
  `BANK_NAME` varchar(255) NOT NULL,
  `ACCOUNT_NAME` varchar(255) NOT NULL,
  `ENGLISH_NAME` varchar(255) DEFAULT NULL,
  `BANK_ADDRESS` varchar(255) DEFAULT NULL,
  `BANK_PHONE_NUMBER` varchar(32) DEFAULT NULL,
  `REAMRK` varchar(255) DEFAULT NULL,
  `UPDATE_TIMESTAMP` datetime NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- update all table which has id to auto_increment
ALTER TABLE  client                                MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  client_bank_message                   MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  client_contact                        MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  client_inquiry                        MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  client_inquiry_alter_element          MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  client_inquiry_element                MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  client_inquiry_file                   MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  client_invoice                        MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  client_invoice_element                MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  client_order                          MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  client_order_element                  MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  client_order_file                     MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  client_quote                          MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  client_quote_element                  MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  client_receipt                        MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  client_ship                           MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  competitor                            MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  competitor_quote                      MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  competitor_quote_element              MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  element                               MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  export_package                        MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  export_package_element                MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  import_package                        MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  import_package_element                MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  import_package_payment                MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  import_package_payment_element        MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  import_storage_location_list          MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  income                                MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  income_detail                         MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  supplier                              MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  supplier_inquiry                      MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  supplier_inquiry_element              MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  supplier_order                        MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  supplier_order_element                MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  supplier_payment                      MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  supplier_prepayment                   MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  supplier_quote                        MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  supplier_quote_element                MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE  supplier_quote_file                   MODIFY COLUMN  `ID` int(11) NOT NULL AUTO_INCREMENT;

CREATE TABLE `system_code` (
  `ID` int(11) NOT NULL,
  `TYPE` varchar(32) NOT NULL,
  `CODE` varchar(32) NOT NULL,
  `VALUE` varchar(32) NOT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  `UPDATE_TIMESTAMP` datetime NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_SYSTEM_CODE_1` (`TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- insert data for core.system_code 
INSERT into core.system_code (id,type,code,value,UPDATE_timestamp) values(512,'SUPPLIER_STATUS_ID',1,'状态1',CURRENT_TIMESTAMP)
INSERT into core.system_code (id,type,code,value,UPDATE_timestamp) values(513,'SUPPLIER_STATUS_ID',2,'状态2',CURRENT_TIMESTAMP);
INSERT into core.system_code (id,type,code,value,UPDATE_timestamp) values(514,'SUPPLIER_STATUS_ID',3,'状态3',CURRENT_TIMESTAMP);
INSERT into core.system_code (id,type,code,value,UPDATE_timestamp) values(515,'SUPPLIER_CLASSIFY_ID',1,'类别1',CURRENT_TIMESTAMP)
INSERT into core.system_code (id,type,code,value,UPDATE_timestamp) values(516,'SUPPLIER_CLASSIFY_ID',2,'类别2',CURRENT_TIMESTAMP);
INSERT into core.system_code (id,type,code,value,UPDATE_timestamp) values(517,'SUPPLIER_CLASSIFY_ID',3,'类别3',CURRENT_TIMESTAMP);
INSERT into core.system_code (id,type,code,value,UPDATE_timestamp) values(518,'SUPPLIER_TAXREIMBURSEMENT_ID',1,'是',CURRENT_TIMESTAMP);
INSERT into core.system_code (id,type,code,value,UPDATE_timestamp) values(519,'SUPPLIER_TAXREIMBURSEMENT_ID',2,'否',CURRENT_TIMESTAMP);
INSERT into core.system_code (id,type,code,value,UPDATE_timestamp) values(520,'SUPPLIER_GRADE_ID',1,'等级1',CURRENT_TIMESTAMP);
INSERT into core.system_code (id,type,code,value,UPDATE_timestamp) values(521,'SUPPLIER_GRADE_ID',2,'等级2',CURRENT_TIMESTAMP);
INSERT into core.system_code (id,type,code,value,UPDATE_timestamp) values(522,'SUPPLIER_GRADE_ID',3,'等级3',CURRENT_TIMESTAMP);
INSERT into core.system_code (id,type,code,value,UPDATE_timestamp) values(523,'SUPPLIER_PHASES_ID',1,'阶段1',CURRENT_TIMESTAMP);
INSERT into core.system_code (id,type,code,value,UPDATE_timestamp) values(524,'SUPPLIER_PHASES_ID',2,'阶段2',CURRENT_TIMESTAMP);
INSERT into core.system_code (id,type,code,value,UPDATE_timestamp) values(525,'SUPPLIER_PHASES_ID',3,'阶段3',CURRENT_TIMESTAMP);


-- add table for saving SUPPLIER_contact message
CREATE TABLE `supplier_contact` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUPPLIER_ID` int(11) NOT NULL,
	`NAME` varchar(255) NOT NULL,
	`APPELLATION` varchar(255) NOT NULL,
	`SEX_ID` int(11) NOT NULL,
	`POSITION` varchar(255) NOT NULL,
	`BIRTHDAY` datetime DEFAULT NULL,
	`DEPARTMENT` varchar(255) DEFAULT NULL,
	`PHONE` varchar(32) DEFAULT NULL,	
	`MOBILE` varchar(32) DEFAULT NULL,
	`FAX` varchar(100) DEFAULT NULL,
	`CREATION_DATE` datetime DEFAULT NULL,
	`EMAIL` varchar(255) DEFAULT NULL,
	`REMARK`  varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1,'SEX_ID',man,'男',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(0,'SEX_ID',woman,'女',CURRENT_TIMESTAMP);

-- move table exchange_rate and seq_key, view v_currency from core to crm db
CREATE TABLE `exchange_rate` (
  `CURRENCY_ID` int(11) NOT NULL,
  `RATE` double(10,4) NOT NULL,
  `UPDATE_TIMESTAMP` datetime NOT NULL,
  PRIMARY KEY (`CURRENCY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `seq_key` (
  `SCHEMA_NAME` varchar(32) NOT NULL,
  `TABLE_NAME` varchar(64) NOT NULL,
  `KEY_NAME` varchar(64) NOT NULL,
  `SEQ` int(11) NOT NULL,
  `UPDATE_TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`SCHEMA_NAME`,`TABLE_NAME`,`KEY_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE VIEW `v_currency` AS select `sc`.`ID` AS `id`,`sc`.`CODE` AS `code`,`sc`.`VALUE` AS `value`,`er`.`RATE` AS `rate`,`sc`.`REMARK` AS `remark`,`er`.`UPDATE_TIMESTAMP` AS `update_timestamp` from (`exchange_rate` `er` join `system_code` `sc` on((`er`.`CURRENCY_ID` = `sc`.`ID`)));

CREATE TABLE `SUPPLIER_CLASSIFY` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUPPLIER_CLASSIFY_ID` varchar(255) DEFAULT NULL,
  `SUPPLIER_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(80,'SUPPLIER_CLASSIFY_ID',1,'OEM',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(81,'SUPPLIER_CLASSIFY_ID',2,'分销商',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(82,'SUPPLIER_CLASSIFY_ID',3,'中间商',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(83,'SUPPLIER_CLASSIFY_ID',4,'MRO',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(84,'SUPPLIER_CLASSIFY_ID',5,'旧件',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(85,'SUPPLIER_CLASSIFY_ID',6,'',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(86,'SUPPLIER_CLASSIFY_ID',7,'',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(87,'SUPPLIER_CLASSIFY_ID',8,'',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(88,'SUPPLIER_CLASSIFY_ID',9,'',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(89,'SUPPLIER_CLASSIFY_ID',10,'',CURRENT_TIMESTAMP);


-- add colum showing the user information
ALTER TABLE t_user ADD COLUMN `information` VARCHAR(255) DEFAULT NULL;
ALTER TABLE t_user ADD COLUMN `email` VARCHAR(255) DEFAULT NULL;
ALTER TABLE t_user ADD COLUMN `fax` VARCHAR(32) DEFAULT NULL;
ALTER TABLE t_user ADD COLUMN `phone` VARCHAR(32) DEFAULT NULL;


-add colum calculate their own rate
ALTER TABLE exchange_rate ADD COLUMN `TRANSFER_RANGE` DOUBLE(10,4) NOT NULL ;

ALTER TABLE supplier_quote ADD COLUMN `QUOTE_STATUS_ID` int(11) NOT NULL DEFAULT 90;
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(90,'QUOTE_STATUS_ID','QUOTE_STATUS_ID','有效',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(91,'QUOTE_STATUS_ID','QUOTE_STATUS_ID','无效',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(55,'CERT','MFR CERT','MFR CERT',CURRENT_TIMESTAMP);

ALTER TABLE supplier_quote_element ADD COLUMN `LOCATION` VARCHAR(255) DEFAULT NULL;
ALTER TABLE supplier_quote_element ADD COLUMN `MOQ` INT(11) DEFAULT NULL;

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(140,'TERMS_OF_DELIVERY','EXW','EXW',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(141,'TERMS_OF_DELIVERY','FCA','FCA',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(142,'TERMS_OF_DELIVERY','FAS','FAS',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(143,'TERMS_OF_DELIVERY','FOB','FOB',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(144,'TERMS_OF_DELIVERY','CFR','CFR',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(145,'TERMS_OF_DELIVERY','CIF','CIF',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(146,'TERMS_OF_DELIVERY','CPT','CPT',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(147,'TERMS_OF_DELIVERY','CPI','CPI',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(148,'TERMS_OF_DELIVERY','DAF','DAF',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(149,'TERMS_OF_DELIVERY','DES','DES',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(150,'TERMS_OF_DELIVERY','DEQ','DEQ',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(151,'TERMS_OF_DELIVERY','DDU','DDU',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(152,'TERMS_OF_DELIVERY','DDP','DDP',CURRENT_TIMESTAMP);

--add column TERMS_OF_DELIVERY`
ALTER TABLE client ADD COLUMN `TERMS_OF_DELIVERY` INT(11) NOT NULL DEFAULT 140;

ALTER TABLE client_quote ADD COLUMN `TERMS_OF_DELIVERY` INT(11) NOT NULL DEFAULT 140;

ALTER TABLE client_quote_element ADD COLUMN `LOCATION` VARCHAR(255) DEFAULT NULL;

ALTER TABLE import_package ADD COLUMN `logistics_way` INT(11)  NULL ;
ALTER TABLE import_package ADD COLUMN `logistics_no` VARCHAR(255)  NULL ;

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(160,'LOGISTICS_WAY','FEDEX','FEDEX',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(161,'LOGISTICS_WAY','DHL','DHL',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(162,'LOGISTICS_WAY','UPS','UPS',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(163,'LOGISTICS_WAY','EMS','EMS',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(164,'LOGISTICS_WAY','顺丰快递','顺丰快递',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(165,'LOGISTICS_WAY','德邦物流','德邦物流',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(166,'LOGISTICS_WAY','天地华宇','天地华宇',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(167,'LOGISTICS_WAY','空运','空运',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(168,'LOGISTICS_WAY','其他物流','其他物流',CURRENT_TIMESTAMP);

ALTER TABLE client_inquiry_element ADD COLUMN `IS_MAIN` INT(2)  NULL ;
ALTER TABLE client_inquiry_element ADD COLUMN `MAIN_ID` INT(11)  NULL ;
ALTER TABLE import_package ADD COLUMN `SUPPLIER_ORDER_NUMBER` VARCHAR(255)  NULL ;
  
ALTER TABLE supplier_quote_element ADD COLUMN `FREIGHT` INT(11)  NULL ;

ALTER TABLE client ADD COLUMN `CLIENT_TEMPLATE_TYPE` INT(11) NOT NULL DEFAULT 170;
 INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(170,'CLIENT_TEMPLATE_TYPE','类型1','类型1',CURRENT_TIMESTAMP);
 INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(171,'CLIENT_TEMPLATE_TYPE','类型2','类型2',CURRENT_TIMESTAMP);
 INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(172,'CLIENT_TEMPLATE_TYPE','类型3','类型3',CURRENT_TIMESTAMP);
 

ALTER TABLE client_inquiry_element ADD COLUMN `IS_BLACKLIST` INT(2) NOT NULL DEFAULT 0 ;
ALTER TABLE t_part ADD COLUMN `IS_BLACKLIST` INT(2) NOT NULL DEFAULT 0 ;
ALTER TABLE T_PART add ColUMN PART_TYPE INT(11) NOT NULL DEFAULT 200;

 
CREATE TABLE `t_part_upload_backup` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `PART_NUM` varchar(63) NOT NULL,
  `PART_NAME` varchar(1000) NOT NULL,
  `CAGE_CODE` char(5) NOT NULL,
  `NSN` varchar(16) NOT NULL,
  `REPLACED_NSN` varchar(16) DEFAULT NULL,
  `WEIGHT` varchar(31) DEFAULT NULL,
  `DIMENTIONS` varchar(63) DEFAULT NULL,
  `COUNTRY_OF_ORIGIN` char(2)DEFAULT NULL,
  `ECCN` varchar(15) DEFAULT NULL,
  `SCHEDULE_B_CODE` varchar(31) DEFAULT NULL,
  `SHELF_LIFE` int(6) DEFAULT NULL,
  `ATA_CHAPTER_SECTION` int(6) DEFAULT NULL,
  `CATEGORY_NO` int(11) DEFAULT NULL,
  `USML` varchar(255) DEFAULT NULL,
  `HAZMAT_CODE` varchar(255) DEFAULT NULL,
  `IMG_PATH` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `LINE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
)

CREATE TABLE `authority_relation` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `CLIENT_ID` int(11) DEFAULT NULL,
  `SUPPLIER_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
)

 INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(200,'PART_TYPE','STANDARD_PARTS','标准件',CURRENT_TIMESTAMP);
 INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(201,'PART_TYPE','CONSUMABLE_PARTS','消耗件',CURRENT_TIMESTAMP);
 INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(202,'PART_TYPE','TRANSFER_PART','中转件',CURRENT_TIMESTAMP);
 
 CREATE TABLE `supplier_air_relation` (
  `SUPPLIER_ID` int(11) NOT NULL,
  `AIR_ID` int(11) NOT NULL,
  PRIMARY KEY (`SUPPLIER_ID`,`AIR_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE supplier_order_element ADD COLUMN `AWB` varchar(21) DEFAULT NULL;


CREATE TABLE `client_order_element_upload` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `ITEM` int(11) NOT NULL,
  `PART_NUMBER` VARCHAR(255) NOT NULL,
  `ERROR` VARCHAR(255) NOT NULL,
  `LINE` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
)


CREATE TABLE `client_inquiry_element_upload` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `ITEM` int(11) NOT NULL,
  `PART_NUMBER` VARCHAR(255) NOT NULL,
  `ERROR` VARCHAR(255) NOT NULL,
  `LINE` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
)

CREATE TABLE `client_quote_element_upload` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `SN` int(11) NOT NULL,
  `PART_NUMBER` VARCHAR(255) NOT NULL,
	`MESSAGE` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`ID`)
)

CREATE TABLE `supplier_quote_element_upload` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `SN` int(11) NOT NULL,
  `PART_NUMBER` VARCHAR(255) NOT NULL,
	`MESSAGE` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`ID`)
)



ALTER TABLE client_invoice ADD COLUMN `INVOICE_TYPE` int(2) NOT NULL;

 INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(173,'INVOICE_TEMPLET','TEMPLET_ONE','模板一',CURRENT_TIMESTAMP);
 INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(174,'INVOICE_TEMPLET','TEMPLET_TWO','模板二',CURRENT_TIMESTAMP);
 INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(175,'INVOICE_TEMPLET','TEMPLET_THREE','模板三',CURRENT_TIMESTAMP);
 INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(176,'INVOICE_TEMPLET','TEMPLET_FOUR','模板四',CURRENT_TIMESTAMP);
  INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(177,'INVOICE_TEMPLET','TEMPLET_FOUR','模板五',CURRENT_TIMESTAMP);
 
ALTER TABLE client_quote ADD COLUMN `FREIGHT` double(10,4) DEFAULT NULL;
ALTER TABLE client_quote ADD COLUMN `LOWEST_FREIGHT` double(10,4) DEFAULT NULL;
ALTER TABLE income ADD COLUMN `CLIENT_INVOICE_ID` int(11) NOT NULL;

ALTER TABLE client_order ADD COLUMN `LC` VARCHAR(255) DEFAULT NULL;
ALTER TABLE client_order ADD COLUMN `IMPORTERS_REGISTRATION` VARCHAR(255) DEFAULT NULL;
ALTER TABLE client_order ADD COLUMN `CREATE_USER_ID` INT(11) NOT NULL;

view:v_income_bill
select i.CLIENT_INVOICE_ID,id.INCOME_ID,SUM(TOTAL) AS TOTAL,SUM(AMOUNT) AS TOTAL_COUNT from income_detail id 
LEFT JOIN income i ON (i.ID = id.INCOME_ID) GROUP BY id.INCOME_ID 

ALTER TABLE income_detail ADD COLUMN `TOTAL` double(10,4) NOT NULL;

view:v_invoice_bill
SELECT DISTINCT id.CLIENT_ORDER_ELEMENT_ID,i.RECEIVE_DATE,SUM(id.TOTAL) AS TOTAL from client_invoice ci LEFT JOIN income i 
ON (i.CLIENT_INVOICE_ID = ci.ID) LEFT JOIN income_detail id 
ON (id.INCOME_ID = i.ID) GROUP BY ci.CLIENT_ORDER_ID 

ALTER TABLE client_order ADD COLUMN `PURCHASE_APPLY` INT(2) NOT NULL DEFAULT 0;

ALTER TABLE import_package_element ADD COLUMN `BOX_WEIGHT` double(10,2) DEFAULT NULL;

ALTER TABLE t_user add COLUMN email_password varchar(45) NOT NULL;

ALTER TABLE `supplier` ADD COLUMN `PREPAY_RATE` double(10,4) NOT NULL DEFAULT '0.0000';
ALTER TABLE `supplier` ADD COLUMN `SHIP_PAY_RATE` double(10,4) NOT NULL DEFAULT '0.0000';
ALTER TABLE `supplier` ADD COLUMN `RECEIVE_PAY_RATE` double(10,4) NOT NULL DEFAULT '0.0000';
ALTER TABLE `supplier` ADD COLUMN `RECEIVE_PAY_PERIOD` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `supplier` ADD COLUMN `MOV`  VARCHAR(255) DEFAULT NULL;

﻿CREATE TABLE `T_APPROVAL` (
  `APPROVAL_ID` int(11) NOT NULL AUTO_INCREMENT,
  `APPROVAL_NAME` VARCHAR(255) DEFAULT '',
  `APPROVAL_TYPE` VARCHAR(255) NOT NULL,
  `ASSOCIATION_KEY` varchar(255) NOT NULL DEFAULT 0,
  `REQUESTER_ID` int(11) NOT NULL,
  `REQUESTER_NAME` VARCHAR(255) DEFAULT '',
  `APPROVER_ROLE_ID` int NOT NULL,
  `APPROVER_ROLE_NAME` VARCHAR(255) DEFAULT '',
  `APPROVER_ID` int(11) NOT NULL DEFAULT 0,
  `APPROVER_NAME` VARCHAR(255) DEFAULT '',
  `APPROVAL_STATUS` TINYINT DEFAULT 0,
  `CREATE_DATETIME` DATETIME not null DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_DATETIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`APPROVAL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `T_APPROVAL_DETAIL`(
  `APPROVAL_ID` int(11) NOT NULL,
  `APPROVAL_DETAIL_ID` int(11) NOT NULL AUTO_INCREMENT,
  `UPDATER_ID` int(11) NOT NULL,
  `UPDATER_NAME` VARCHAR(255) DEFAULT '',
  `UPDATE_DATETIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `APPROVAL_STATUS` TINYINT DEFAULT 0,
  `COMMENT` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`APPROVAL_DETAIL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `supplier_contact` ADD COLUMN `SUR_NAME`  VARCHAR(255) DEFAULT NULL;
ALTER TABLE `supplier_contact` ADD COLUMN `FULL_NAME`  VARCHAR(255) DEFAULT NULL;

View：v_client_order_export
SELECT coe.ID AS CLIENT_ORDER_ELEMENT_ID,coe.CLIENT_ORDER_ID,epe.AMOUNT AS export_amount, 
coe.AMOUNT AS client_order_element_amount,(coe.PRICE*epe.AMOUNT) AS export_total from client_order_element coe
LEFT JOIN supplier_order_element soe ON (coe.ID = soe.CLIENT_ORDER_ELEMENT_ID)
LEFT JOIN supplier_import_element sie ON (soe.ID = sie.SUPPLIER_ORDER_ELEMENT_ID)
LEFT JOIN  export_package_element epe ON (sie.IMPORT_PACKAGE_ELEMENT_ID = epe.IMPORT_PACKAGE_ELEMENT_ID) 


ALTER TABLE supplier ADD COLUMN `URL` VARCHAR(255) NOT NULL ;

ALTER TABLE `IMPORT_PACKAGE_ELEMENT` ADD COLUMN `COMPLIANCE_CERTIFICATE`  INT(11) DEFAULT 300;
ALTER TABLE `IMPORT_PACKAGE_ELEMENT` ADD COLUMN `COMPLETE_COMPLIANCE_CERTIFICATE`  INT(11) DEFAULT 520;

 INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(301,'COMPLIANCE_CERTIFICATE','COMPLIANCE_CERTIFICATE','履历本',CURRENT_TIMESTAMP);
 INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(300,'COMPLIANCE_CERTIFICATE','COMPLIANCE_CERTIFICATE','合格证',CURRENT_TIMESTAMP);

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(56,'CERT','CAAC','CAAC',CURRENT_TIMESTAMP);

ALTER TABLE export_package ADD COLUMN `LOGISTICS_WAY` INT(11) DEFAULT NULL ;
ALTER TABLE export_package ADD COLUMN `AWB` VARCHAR(255) DEFAULT NULL ;
ALTER TABLE export_package ADD COLUMN `WEIGHT` DOUBLE(10,4) DEFAULT NULL ;
ALTER TABLE export_package ADD COLUMN `SIZE` VARCHAR(255) DEFAULT NULL ;

ALTER TABLE supplier_order_element ADD COLUMN `IMPORT_STATUS` INT(2) DEFAULT NULL ;
CREATE TABLE `EXPORT_PACKAGE_INSTRUCTIONS`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `EXPORT_PACKAGE_INSTRUCTIONS_NUMBER` VARCHAR(255) NOT NULL ,
  `REMARK` VARCHAR(255),
  `CREAT_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
`CODE` VARCHAR(255),
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `EXPORT_PACKAGE_INSTRUCTIONS_ELEMENT`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `IMPORT_PACKAGE_ELEMENT_ID` int(11) NOT NULL ,
  `EXPORT_PACKAGE_INSTRUCTIONS_ID` int(11) NOT NULL,
 `AMOUNT` double(10,2) NOT NULL ,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE export_package ADD COLUMN `EXPORT_PACKAGE_INSTRUCTIONS_ID` int(11)  ;
ALTER TABLE client_invoice ADD COLUMN `INVOICE_STATUS_ID` INT(11) DEFAULT 0 ;


ALTER TABLE client ADD COLUMN `URL` VARCHAR(255) default NULL ;

ALTER TABLE authority_relation ADD COLUMN `AIR_TYPE_ID` INT(11) default NULL ;

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(57,'CERT','FAA+CAAC','FAA+CAAC',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(58,'CERT','EASA+CAAC','EASA+CAAC',CURRENT_TIMESTAMP);

ALTER TABLE import_package_element ADD COLUMN `BATCH_NUMBER`  VARCHAR(255) default NULL ;

ALTER TABLE supplier_quote ADD COLUMN `QUOTE_NUMBER`  VARCHAR(255) NOT NULL ;

update supplier_quote sq, supplier_inquiry si
  set sq.QUOTE_NUMBER = si.QUOTE_NUMBER
 where sq.SUPPLIER_INQUIRY_ID = si.ID
 
 ALTER TABLE supplier_order_element ADD COLUMN `SHIP_WAY_ID` INT(11) default NULL ;
ALTER TABLE supplier_order_element ADD COLUMN `DESTINATION` int(11) default NULL ;

ALTER TABLE client_inquiry_element ADD COLUMN `TYPE_CODE`  VARCHAR(255) DEFAULT NULL 

CREATE TABLE `purchase_application_form`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL ,
  `APPLICATION_DATE` date NOT NULL,
  `APPLICATION_NUMBER` VARCHAR(255) NOT NULL ,
  `CLIENT_ORDER_ID` int(11) NOT NULL ,
  `UPDATE_TIMESTAMP` TIMESTAMP NOT NULL CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
)

ALTER TABLE import_package_payment ADD COLUMN `SUPPLIER_ID`  INT(11) NOT NULL ;
ALTER TABLE import_package_payment_element ADD COLUMN `SUPPLIER_ORDER_ELEMENT_ID`  INT(11) NOT NULL 

CREATE TABLE `import_package_payment_element_prepare`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUPPLIER_ID` int(11) NOT NULL ,
  `SUPPLIER_ORDER_ELEMENT_ID` int(11) NOT NULL,
  `AMOUNT` double(10,2) NOT NULL,
  `UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
)

ALTER TABLE import_package_payment_element_prepare ADD COLUMN `IMPORT_PACKAGE_ELEMENT_ID`  INT(11) NOT NULL 
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(73,'SQ_STATUS','invalid','无效',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(92,'QUOTE_STATUS_ID','EXPIRED','失效',CURRENT_TIMESTAMP);

ALTER TABLE `supplier_order` ADD COLUMN `PREPAY_RATE` double(10,4) NOT NULL DEFAULT '0.0000';
ALTER TABLE `supplier_order` ADD COLUMN `SHIP_PAY_RATE` double(10,4) NOT NULL DEFAULT '0.0000';
ALTER TABLE `supplier_order` ADD COLUMN `RECEIVE_PAY_RATE` double(10,4) NOT NULL DEFAULT '0.0000';
ALTER TABLE `supplier_order` ADD COLUMN `RECEIVE_PAY_PERIOD` int(11) NOT NULL DEFAULT '0';

ALTER TABLE import_package_payment ADD COLUMN `PAYMENT_TYPE`  INT(11) NOT NULL ;
ALTER TABLE import_package_payment ADD COLUMN `SUPPLIER_ORDER_ID`  INT(11)  NULL ;


-- view name v_payment_bill
SELECT
	ippe.IMPORT_PACKAGE_PAYMENT_ID,
	SUM(soe.PRICE*ippe.AMOUNT*ippe.PAYMENT_PERCENTAGE/100) AS payment_total
FROM
	import_package_payment_element ippe
LEFT JOIN import_package_payment ipp on ipp.ID = ippe.IMPORT_PACKAGE_PAYMENT_ID
LEFT JOIN supplier_order_element soe on soe.ID = ippe.SUPPLIER_ORDER_ELEMENT_ID
GROUP BY
	ippe.IMPORT_PACKAGE_PAYMENT_ID 

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(59,'CERT','TCCA FORM ONR','TCCA FORM ONR',CURRENT_TIMESTAMP);

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(220,'STORE_LOCATIONSTORE_LOCATION','LOCATION_ONE','香港仓库',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(221,'STORE_LOCATION','LOCATION_TWO','美国仓库',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(222,'STORE_LOCATION','LOCATION_THREE','其他',CURRENT_TIMESTAMP);

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(169,'LOGISTICS_WAY','Dropship FedEx','Dropship FedEx',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(180,'LOGISTICS_WAY','Dropship DHL','Dropship DHL',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(181,'LOGISTICS_WAY','Dropship other','Dropship other',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(182,'LOGISTICS_WAY','Forwarder','ForWarder',CURRENT_TIMESTAMP);


INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(59,'CERT','TCCA FORM ONR','TCCA FORM ONR',CURRENT_TIMESTAMP);

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(230,'PAYMENT_STATUS','PAYMENT_STATUS_ONE','未完成',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(231,'PAYMENT_STATUS','PAYMENT_STATUS_TWO','完成',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(232,'PAYMENT_STATUS','PAYMENT_STATUS_THREE','审批中',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(233,'PAYMENT_STATUS','PAYMENT_STATUS_FOUR','审批不通过',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(234,'PAYMENT_STATUS','PAYMENT_STATUS_THREE','未发起审批',CURRENT_TIMESTAMP);

ALTER TABLE import_package_payment ADD COLUMN `SPZT`  INT(11) NOT NULL;
ALTER TABLE supplier_order_element ADD COLUMN `APPROVAL_ID`  INT(11)  NULL ;


ALTER TABLE client_quote ADD COLUMN `FIXED_COST`  double(10,4) DEFAULT NULL ;
ALTER TABLE client ADD COLUMN `FIXED_COST`  double(10,4) DEFAULT NULL ;
ALTER TABLE import_package_payment_element_prepare ADD COLUMN `SUPPLIER_ID`  INT(11) NOT NULL ;

ALTER TABLE import_package_element  ADD COLUMN `CERTIFICATION_STATUS_ID`  INT(11) DEFAULT 0 ;

ALTER TABLE supplier_quote  ADD COLUMN `VALIDITY`  TIMESTAMP  NULL DEFAULT NULL;
ALTER TABLE supplier_quote_element  ADD COLUMN `VALIDITY`  TIMESTAMP  NULL DEFAULT NULL;
ALTER TABLE client_order  ADD COLUMN `CERTIFICATION`  VARCHAR(255) DEFAULT NULL 
ALTER TABLE client_order_element  ADD COLUMN `CERTIFICATION_ID`  INT(11) DEFAULT NULL 

ALTER TABLE import_package_payment_element  ADD COLUMN `PAYMENT_PERCENTAGE`  DOUBLE(4,2) NOT NULL 


CREATE TABLE `warn_message`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_INQUIRY_ID` int(11) NOT NULL ,
  `CLIENT_ID` int(11) NOT NULL ,
  `PART_NUMBER` VARCHAR(255) NOT NULL,
  `READ_STATUS` int(11) NOT NULL,
  `UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
)


-- view v_payment_element_bill
SELECT
	ippe.SUPPLIER_ORDER_ELEMENT_ID,
	SUM(ippe.PAYMENT_SUM) AS PAYMENT_SUM,
	SUM(ippe.AMOUNT*ippe.PAYMENT_PERCENTAGE/100) AS AMOUNT
FROM
	import_package_payment_element ippe
GROUP BY
	ippe.SUPPLIER_ORDER_ELEMENT_ID 

ALTER TABLE `client` ADD COLUMN `PROFIT_MARGIN` double(10,4)  NULL DEFAULT NULL;
ALTER TABLE `client` ADD COLUMN `CERTIFICATION` INT(11)   NULL DEFAULT NULL;

ALTER TABLE `import_package_element` ADD COLUMN `MANUFACTURE_DATE`    TIMESTAMP  NULL DEFAULT NULL;
ALTER TABLE `import_package_element` ADD COLUMN `INSPECTION_DATE`    TIMESTAMP  NULL DEFAULT NULL;

ALTER TABLE supplier_quote_element  ADD COLUMN `VALIDITY`  TIMESTAMP  NULL DEFAULT NULL;
ALTER TABLE `import_package_payment` ADD COLUMN `LEAD_TIME` VARCHAR(255)  NULL DEFAULT NULL;

ALTER TABLE `import_package_payment_element_prepare` ADD COLUMN `import_package_id` INT(11) NOT NULL;

ALTER TABLE `supplier_order_element` ADD COLUMN `ORDER_STATUS_ID` INT(11) NOT NULL DEFAULT 60;
ALTER TABLE `client_order_element` ADD COLUMN `ORDER_STATUS_ID` INT(11) NOT NULL DEFAULT 60;

update system_code set type = "SPZT" WHERE ID IN (232,233,234)
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(235,'SPZT','WC','审批完成',CURRENT_TIMESTAMP);
ALTER TABLE `import_package_payment` ADD COLUMN `PAYMENT_STATUS_ID` INT(11) NOT NULL DEFAULT 230;

CREATE TABLE `supplier_debt`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUPPLIER_ORDER_ID` int(11) NOT NULL ,
  `TOTAL` DOUBLE(10,2) NOT NULL,
  `UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
)

ALTER TABLE `supplier_debt` ADD COLUMN `paid` double(10,4)  NULL DEFAULT NULL;
ALTER TABLE export_package_instructions_element ADD CONSTRAINT IMPORT_PACKAGE_ELEMENT_ID FOREIGN KEY (IMPORT_PACKAGE_ELEMENT_ID) REFERENCES import_package_element (ID)
ALTER TABLE export_package_instructions_element ADD CONSTRAINT EXPORT_PACKAGE_INSTRUCTIONS_ID FOREIGN KEY (EXPORT_PACKAGE_INSTRUCTIONS_ID) REFERENCES export_package_instructions (ID)
ALTER TABLE export_package_instructions_element ADD INDEX IMPORT_PACKAGE_ELEMENT_ID

CREATE TABLE `on_passage_storage`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUPPLIER_ORDER_ELEMENT_ID` int(11) NOT NULL ,
  `CLIENT_ORDER_ELEMENT_ID` int(11) NOT NULL ,
  `AMOUNT` DOUBLE(10,2) NOT NULL,
  `IMPORT_STATUS` int(2) NOT NULL ,
  `UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
)
CREATE TABLE `arrears_use`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `IMPORT_PACKAGE_PAYMENT_ID` int(11) NOT NULL ,
  `SUPPLIER_CODE` VARCHAR(255) NOT NULL,
  `TOTAL` DOUBLE(10,2) NULL,
  `COUNTER_FEE`  DOUBLE(10,2) NULL,
  `UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
)

ALTER TABLE `client_order_element` ADD COLUMN `SPZT` INT(11) NOT NULL DEFAULT 232;

CREATE TABLE `order_approval`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUPPLIER_QUOTE_ELEMENT_ID` int(11)  NULL ,
  `CLIENT_ORDER_ELEMENT_ID` int(11)  NULL ,
	`CLIENT_ORDER_ID` int(11)  NULL ,
  `CLIENT_QUOTE_ELEMENT_ID` int(11)  NULL ,
  `STATE` int(2) NOT NULL ,
  `TYPE` int(2) NOT NULL ,	
  `UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
)




-- 1-16
CREATE TABLE `unexport_element`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `IMPORT_PACKAGE_ELEMENT_ID` int(11) NOT NULL ,
  `USER_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
)

ALTER TABLE `import_package` ADD COLUMN `IMPORT_STATUS` INT(2) DEFAULT 0;
UPDATE import_package SET IMPORT_STATUS = 1

ALTER TABLE `order_approval` ADD COLUMN `PROCESS_INSTANCE_ID` VARCHAR(255)  NULL ;

ALTER TABLE `order_approval` ADD COLUMN `IMPORT_PACKAGE_ELEMENT_ID` INT(11)  NULL ;

ALTER TABLE `order_approval` ADD COLUMN `SUPPLIER_ORDER_ELEMENT_ID` INT(11)  NULL ;

ALTER TABLE `client_order_element` ADD COLUMN `STORAGE_STATUS` INT(2) DEFAULT NULL;

-- 1-17
ALTER TABLE `supplier_order` ADD COLUMN `ALL_PREPAY_NOT_IMPORT_STATUS` INT(2) DEFAULT 0;

INSERT into import_storage_location_list (id,location) values(110,'YC000');


--1-19
ALTER TABLE `jbpm4_task` ADD COLUMN `YW_TABLE_ID` INT(11)  NULL ;
ALTER TABLE `jbpm4_task` ADD COLUMN `YW_TABLE_ELEMENT_ID` INT(11)  NULL ;
ALTER TABLE `jbpm4_hist_task` ADD COLUMN `YW_TABLE_ID` INT(11)  NULL ;
ALTER TABLE `jbpm4_hist_task` ADD COLUMN `YW_TABLE_ELEMENT_ID` INT(11)  NULL ;
ALTER TABLE `jbpm4_hist_actinst` ADD COLUMN `YW_TABLE_ID` INT(11)  NULL ;
ALTER TABLE `jbpm4_hist_actinst` ADD COLUMN `YW_TABLE_ELEMENT_ID` INT(11)  NULL ;
ALTER TABLE `import_package_payment_element` ADD COLUMN `SPZT` INT(11)  NULL DEFAULT 234;

-- 1-18
INSERT into system_code (id,type,code,value,update_timestamp) values(74,'SQ_STATUS','LACK','没货',CURRENT_TIMESTAMP);




-- 2-4
ALTER TABLE `supplier` ADD COLUMN `APTITUDE` datetime DEFAULT NULL ;

-- 2-5
ALTER TABLE `supplier` ADD COLUMN `LAST_WARN_TIME` datetime DEFAULT NULL ;
--2-7
ALTER TABLE `import_package_element` ADD COLUMN `IMPORT_PACKAGE_SIGN` INT(11)  NULL DEFAULT 1;
ALTER TABLE `import_package_element` ADD COLUMN `SPZT` INT(11)  NULL ;


--2-7
ALTER TABLE `import_package_element` ADD COLUMN `IMPORT_PACKAGE_SIGN` INT(11)  NULL DEFAULT 1;

ALTER TABLE `supplier_order_element` ADD COLUMN `INVOICE_DATE` date DEFAULT null;

-- 2-11
ALTER TABLE `supplier_order_element` ADD COLUMN `SHIP_LEAD_TIME` int NULL DEFAULT 0;

-- 2-16
ALTER TABLE `client_quote_element` ADD COLUMN `FIXED_COST` DOUBLE(6,2) NULL DEFAULT 0;

CREATE TABLE `storehouse_address`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
	`NAME` VARCHAR(255) NOT NULL ,
  `ADDRESS` VARCHAR(512) default NULL ,
  PRIMARY KEY (`ID`)
)

ALTER TABLE authority_relation ADD COLUMN `STOREHOUSE_ADDRESS_ID` INT(11) default NULL ;

ALTER TABLE storehouse_address ADD COLUMN UPDATE_TIMESTAMP TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP


-- 2-17

ALTER TABLE import_storage_location_list ADD COLUMN `STOREHOUSE_ADDRESS_ID` INT(11) NOT NULL ;

-- 2-20
CREATE TABLE `supplier_commission_sale`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUPPLIER_ID` int(11) NOT NULL ,
  `COMMISSION_DATE` date NOT NULL ,
	`REMARK` varchar(512)  NULL ,
  `UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
)

CREATE TABLE `supplier_commission_sale_element`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUPPLIER_COMMISSION_SALE_ID` int(11) NOT NULL ,
	`REMARK` varchar(512)  NULL ,
  `UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
)

ALTER TABLE supplier_commission_sale_element ADD COLUMN `PART_NUMBER` VARCHAR(256) NOT NULL ;

ALTER TABLE supplier_commission_sale_element ADD COLUMN `AMOUNT` DOUBLE(10,2) NOT NULL ;

-- 2-20
ALTER TABLE import_package_payment ADD COLUMN `COUNTER_FEE` DOUBLE(10,2)  NULL ;
ALTER TABLE arrears_use ADD COLUMN `IMPORT_PACKAGE_PAYMENT_ELEMENT_ID` int(11)  NULL ;

-- 2-23
ALTER TABLE client_inquiry_element ADD COLUMN `BSN` VARCHAR(127) NULL ;

-- 2-22
ALTER TABLE `client_order_element` ADD COLUMN `FIXED_COST` DOUBLE(6,2) NULL DEFAULT 0;

-- 2-24
ALTER TABLE client_inquiry_element ADD COLUMN EMAIL_SEND INT(2) NULL DEFAULT 0;

-- 2-22
ALTER TABLE `client_order_element` ADD COLUMN `FIXED_COST` DOUBLE(6,2) NULL DEFAULT 0;

-- 2-27
INSERT into import_storage_location_list (id,location) values(111,'YC地面');

CREATE table historical_quotation(
BSN VARCHAR(127) NOT NULL ,
CLIENT_ORDER_ELEMENT_ID INT(11) NULL,
PREVIOUS_CLIENT_ORDER_ELEMENT_ID INT(11) NULL,
SUPPLIER_ORDER_ELEMENT_ID INT(11) NULL,
PREVIOUS_SUPPLIER_ORDER_ELEMENT_ID INT(11) NULL,
UPDATE_TIMESTAMP  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- 3-2
CREATE TABLE `historical_order_price`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BSN` VARCHAR(127) NOT NULL ,
  `CLIENT_ID` int(11) NULL,
	`SUPPLIER_ID` int(11) NULL,
	`AMOUNT` DOUBLE(8,2) NOT NULL,
	`PRICE` DOUBLE(8,2) NOT NULL,
	`YEAR` int(11) NOT NULL,
  `UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
)

-- 3-3
ALTER TABLE import_package_element add COLUMN SUPPLIER_ORDER_ELEMENT_ID INT(11) NULL


-- 3-6
CREATE TABLE `static_client_quote_price`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_ID` int(11) NULL,
	`PART_NUMBER` VARCHAR(256) NOT NULL,
	`PRICE` DOUBLE(8,2) NOT NULL,
	`YEAR` int(11) NOT NULL,
  `UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
)

CREATE TABLE `static_supplier_quote_price`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUPPLIER_ID` int(11) NULL,
	`SUPPLIER_QUOTE_ELEMENT_ID` int(11) NULL,
	`PART_NUMBER` VARCHAR(256) NOT NULL,
	`PRICE` DOUBLE(8,2) NOT NULL,
	`YEAR` int(11) NOT NULL,
  `UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
)	

-- 3-7
ALTER TABLE static_client_quote_price add COLUMN CURRENCY_ID INT(11) NOT NULL

ALTER TABLE static_supplier_quote_price add COLUMN CURRENCY_ID INT(11) NOT NULL


-- 3-8
ALTER TABLE static_supplier_quote_price add COLUMN CONDITION_ID INT(11) NOT NULL;

ALTER TABLE static_supplier_quote_price add COLUMN CERTIFICATION_ID INT(11) NOT NULL;


-- 3-9 
ALTER TABLE `supplier_quote_element` ADD COLUMN `WARRANTY` VARCHAR(255)  NULL ;
alter table supplier_quote_element add COLUMN SERIAL_NUMBER  VARCHAR(256)  NULL ;
alter table supplier_quote_element add COLUMN TAG_SRC  VARCHAR(256)  NULL ;
alter table supplier_quote_element add COLUMN TAG_DATE  VARCHAR(256) null;
alter table supplier_quote_element add COLUMN TRACE  VARCHAR(256)  NULL ;
INSERT into system_code (id,type,code,value,update_timestamp) values(47,'COND','Exchange','Exchange',CURRENT_TIMESTAMP);

--3-10
ALTER TABLE  supplier_quote_element       MODIFY COLUMN  `TAG_DATE` VARCHAR(256) NULL ;

ALTER TABLE supplier_order_element ADD COLUMN TAX_REIMBURSEMENT_ID INT(11) NOT NULL;

ALTER TABLE static_supplier_quote_price ADD COLUMN LEAD_TIME INT(11) NOT NULL DEFAULT 0;

INSERT into system_code (id,type,code,value,update_timestamp) values(65,'CERT','FAA Dual Release','FAA Dual Release',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(66,'CERT','EASA Dual Release','EASA Dual Release',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(67,'CERT','FAA+EASA','FAA+EASA',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(68,'CERT','Dual Release','Dual Release',CURRENT_TIMESTAMP);

--3-20
CREATE TABLE `supplier_weather_order_element`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_ORDER_ELEMENT_ID` int(11) NULL,
	`SUPPLIER_QUOTE_ELEMENT_ID` int(11) NULL,
	`AMOUNT` DOUBLE(8,2) NOT NULL,
	`PRICE` DOUBLE(8,2) NOT NULL,
	`LEAD_TIME` VARCHAR(255) NULL,
	`DEADLINE` TIMESTAMP NULL,
	`SHIP_WAY_ID` int(11) NULL,
	`DESTINATION` VARCHAR(255) NULL,
  `UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
)

--3-22
CREATE TABLE `client_order_element_final`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_ORDER_ELEMENT_ID` int(11) NULL,
	`AMOUNT` DOUBLE(8,2) NOT NULL,
	`PRICE` DOUBLE(8,2) NOT NULL,
	`LEAD_TIME` VARCHAR(255) NULL,
	`FIXED_COST` DOUBLE(6,2) NULL DEFAULT 0,
	`CERTIFICATION_ID`  INT(11) DEFAULT NULL,
  `ORDER_STATUS_ID` INT(11) NOT NULL DEFAULT 60,
  `UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
)

ALTER TABLE supplier_order ADD COLUMN ORDER_TYPE INT(11)  NULL DEFAULT null;

--3-32
ALTER table supplier add COLUMN COUNTER_FEE DOUBLE(6,2) NULL 

--3-24
ALTER TABLE  supplier_quote_element       MODIFY COLUMN  `FREIGHT` DOUBLE(6,2) NULL ;
ALTER table client_order_element_final add COLUMN DEADLINE  date DEFAULT NULL
ALTER TABLE order_approval ADD COLUMN `SPZT`  INT(11) NOT NULL;
ALTER TABLE  order_approval       ADD COLUMN  `AMOUNT` DOUBLE(6,2) NULL ;
ALTER TABLE  order_approval       ADD COLUMN  `TASK_ID` VARCHAR(255) NULL ;
ALTER table jbpm4_task add COLUMN RELATION_ID INT(11) DEFAULT NULL
ALTER TABLE `jbpm4_hist_task` ADD COLUMN `RELATION_ID` INT(11)  NULL ;
ALTER TABLE `jbpm4_hist_actinst` ADD COLUMN `RELATION_ID` INT(11)  NULL ;
ALTER table order_approval add COLUMN SUPPLIER_WEATHER_ORDER_ELEMENT_ID INT(11) DEFAULT NULL
ALTER TABLE order_approval ADD COLUMN `PRICE`  DOUBLE(6,2)  NOT NULL;

--4-10
INSERT into system_code (id,type,code,value,update_timestamp) values(14,'CURRENCY','HK','港币',CURRENT_TIMESTAMP);
INSERT INTO `crm`.`exchange_rate` (`CURRENCY_ID`, `RATE`, `UPDATE_TIMESTAMP`, `TRANSFER_RANGE`) VALUES ('14', '0.0000', '2017-04-10 12:43:53', '0.0000');

-- 4-13
ALTER TABLE client_inquiry_element ADD COLUMN `ELEMENT_STATUS_ID`  INT(11) NULL DEFAULT NULL;

INSERT into system_code (id,type,code,value,update_timestamp) values(700,'ELEMENT_STATUS','1','供应商未询价',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(701,'ELEMENT_STATUS','2','供应商未报价',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(702,'ELEMENT_STATUS','3','客户未报价',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(703,'ELEMENT_STATUS','4','客户未确认',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(704,'ELEMENT_STATUS','5','供应商未订货',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(705,'ELEMENT_STATUS','6','供应商未发货',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(706,'ELEMENT_STATUS','7','供应商未付款',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(707,'ELEMENT_STATUS','8','未入库',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(708,'ELEMENT_STATUS','9','未出库',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(709,'ELEMENT_STATUS','10','客户未收款',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(710,'ELEMENT_STATUS','11','关闭',CURRENT_TIMESTAMP);

--4-14
ALTER TABLE order_approval MODIFY COLUMN `SPZT` INT(11)  NULL;
ALTER TABLE order_approval MODIFY COLUMN `STATE` INT(2)  NULL;
ALTER TABLE order_approval MODIFY COLUMN `TYPE` INT(2)  NULL;
ALTER table order_approval MODIFY COLUMN PRICE DOUBLE(6,2) null

--4-18
ALTER TABLE order_approval ADD COLUMN `OCCUPY`  int(2)  NOT NULL DEFAULT 0;

--5-5
ALTER TABLE supplier_weather_order_element ADD COLUMN `REMARK`  VARCHAR(255) NULL;

CREATE TABLE `satair_quote`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_INQUIRY_ID` int(11) NOT NULL,
	`SUPPLIER_QUOTE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
)
alter TABLE satair_quote ADD COLUMN COMPLETE INT(11) NOT NULL DEFAULT 0
	
alter TABLE satair_quote_element ADD COLUMN SATAIR_QUOTE_ID INT(11) NOT NULL
--5-5
ALTER TABLE supplier_weather_order_element ADD COLUMN `REMARK`  VARCHAR(255) NULL;

--5-11
CREATE TABLE `client_weather_order` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_QUOTE_ID` int(11) NOT NULL,
  `CURRENCY_ID` int(11) NOT NULL,
  `EXCHANGE_RATE` double(10,4) NOT NULL,
  `SOURCE_NUMBER` varchar(255) NOT NULL,
  `ORDER_NUMBER` varchar(255) NOT NULL,
  `SEQ` int(11) NOT NULL DEFAULT '1',
  `ORDER_DATE` date NOT NULL,
  `TERMS` varchar(11) DEFAULT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  `ORDER_STATUS_ID` int(11) NOT NULL,
  `UPDATE_TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `PREPAY_RATE` double(10,4) NOT NULL DEFAULT '0.0000',
  `SHIP_PAY_RATE` double(10,4) NOT NULL DEFAULT '0.0000',
  `SHIP_PAY_PERIOD` int(11) NOT NULL DEFAULT '0',
  `RECEIVE_PAY_RATE` double(10,4) NOT NULL DEFAULT '0.0000',
  `RECEIVE_PAY_PERIOD` int(11) NOT NULL DEFAULT '0',
  `LC` varchar(255) DEFAULT NULL,
  `IMPORTERS_REGISTRATION` varchar(255) DEFAULT NULL,
  `CREATE_USER_ID` int(11) NOT NULL,
  `PURCHASE_APPLY` int(2)  NULL DEFAULT '0',
  `CERTIFICATION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1000755 DEFAULT CHARSET=utf8;

CREATE TABLE `client_weather_order_element` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_WEATHER_ORDER_ID` int(11) NOT NULL,
  `CLIENT_QUOTE_ELEMENT_ID` int(11) NOT NULL,
  `AMOUNT` double(10,2) NOT NULL,
  `PRICE` double(16,2) NOT NULL,
  `LEAD_TIME` varchar(255) DEFAULT NULL,
  `DEADLINE` date DEFAULT NULL,
  `UPDATE_TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `CERTIFICATION_ID` int(11) DEFAULT NULL,
  `FIXED_COST` double(6,2) DEFAULT '0.00',
  PRIMARY KEY (`ID`),
  FOREIGN KEY (`CLIENT_WEATHER_ORDER_ID`) REFERENCES `client_weather_order` (`ID`),
  FOREIGN KEY (`CLIENT_QUOTE_ELEMENT_ID`) REFERENCES `client_quote_element` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1015379 DEFAULT CHARSET=utf8;

alter table client_weather_order_element add COLUMN REMARK VARCHAR(1024) NULL

CREATE TABLE `certifications`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SYSTEM_CODE_ID` int(11) NOT NULL,
	`CODE` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`ID`)
)

-- 5-11
CREATE TABLE `satair_email`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_INQUIRY_ID` int(11) NOT NULL,
	`part_number` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`ID`)
)

-- 5-15
INSERT into system_code (id,type,code,value,update_timestamp) values(711,'ELEMENT_STATUS','12','客户取消合同',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(712,'ELEMENT_STATUS','13','供应商取消合同',CURRENT_TIMESTAMP);

-- 5-16
CREATE TABLE `competitor_supplier`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `COMPETITOR_ID` int(11) NOT NULL ,
  `SUPPLIER_ID` int(11) NOT NULL ,
  `CLIENT_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--5-22
ALTER table client_quote_element add COLUMN BANK_CHARGES DOUBLE(6,2) NULL;
ALTER table client_order_element add COLUMN BANK_CHARGES DOUBLE(6,2) NULL;
alter table client_weather_order_element add COLUMN BANK_CHARGES DOUBLE(6,2) NULL;

CREATE TABLE `quote_bank_charges`(
  `CLIENT_ID` int(11) NOT NULL ,
  `BANK_CHARGES` DOUBLE(6,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `order_bank_charges`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_ID` int(11) NOT NULL ,
	 ORDER_PRICE_ABOVE  DOUBLE(10,2)  NULL,
	 ORDER_PRICE_FOLLOWING  DOUBLE(10,2)  NULL,
  `BANK_CHARGES` DOUBLE(6,2) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table supplier_order add COLUMN CREATE_USER_ID INT(11)  NULL ;
alter table client_order_element add COLUMN REMARK VARCHAR(1024) NULL;
-- 5-18
-- crmstock
ALTER TABLE t_part ADD INDEX CORRELATION_MARK (`CORRELATION_MARK`);

alter table t_part add COLUMN HS_CODE VARCHAR(127) NULL;

alter table t_part add COLUMN CORRELATION_MARK int(11) NULL
ALTER TABLE t_part ADD INDEX CORRELATION_MARK (`CORRELATION_MARK`)  


--5-24
alter table export_package_instructions_element add COLUMN EXPORT_PACKAGE_STATUS INT(11)  NULL DEFAULT 0 -- 0未出库，1已出库

-- 5-27
CREATE TABLE `hs_code_message`(
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `low_rate` VARCHAR(127) NOT NULL ,
  `high_rate` VARCHAR(127) NOT NULL ,
  `out_rate` VARCHAR(127) NOT NULL,
	`back_rate` VARCHAR(127) NOT NULL,
	`control_mark` VARCHAR(127) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table hs_code_message add COLUMN hs_code VARCHAR(127) NOT NULL;

-- 5-31
alter table satair_quote_element add COLUMN if_danger VARCHAR(4) NOT NULL DEFAULT 0;

--6-2
CREATE TABLE `client_order_element_notmatch` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_ORDER_ID` int(11) NOT NULL,
  `CLIENT_QUOTE_ELEMENT_ID` int(11) NOT NULL,
  `AMOUNT` double(10,2) NOT NULL,
  `PRICE` double(16,2) NOT NULL,
  `LEAD_TIME` varchar(255) DEFAULT NULL,
  `DEADLINE` date DEFAULT NULL,
  `CERTIFICATION_ID` int(11) DEFAULT NULL,
  `FIXED_COST` double(6,2) DEFAULT '0.00',
	`REMARK` VARCHAR(1024) NULL,
	`USER_ID` VARCHAR(1024) NULL,
	`PART_NUMBER` VARCHAR(1024) NULL,
	`DESCRIPTION` VARCHAR(1024) NULL,
	`ITEM` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1015379 DEFAULT CHARSET=utf8;
alter table client_order_element_notmatch add COLUMN BSN VARCHAR(255) NULL;
-- 6-2
alter table client add COLUMN SHIP_TEMPLET INT(11) NULL;

INSERT into system_code (id,type,code,value,update_timestamp) values(800,'SHIP_TEMPLET','1','模板一',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(801,'SHIP_TEMPLET','2','模板二',CURRENT_TIMESTAMP);、

UPDATE client set SHIP_TEMPLET = 800


--6-5
CREATE TABLE `hierarchical_relationship` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUPERIOR_USER_ID` int(11) NOT NULL,
  `SUBORDINATE_USER_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1015379 DEFAULT CHARSET=utf8;

--6-6
alter table import_package_element add COLUMN APPROVAL_STATUS INT(11) NULL;
alter table client_order_element ADD COLUMN DESCRIPTION VARCHAR(1024) NULL;

-- 6-7
INSERT into system_code (id,type,code,value,update_timestamp) values(900,'LOCATION_SORT','1','Singapore',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(901,'LOCATION_SORT','2','Copenhagen',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(902,'LOCATION_SORT','3','UK',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(903,'LOCATION_SORT','4','Miami',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(904,'LOCATION_SORT','5','Ashburn',CURRENT_TIMESTAMP);
ALTER TABLE import_package ADD COLUMN WEIGHT DOUBLE(6,4) NULL;

--6-15
alter table client_order_element_final ADD COLUMN DESCRIPTION VARCHAR(1024) NULL;
alter table client_order ADD COLUMN CLIENT_WEATHER_ORDER_ID INT(11) NULL;
alter table client_order_element_final add COLUMN BANK_CHARGES DOUBLE(6,2) NULL;
alter table client_order_element_final add COLUMN CLIENT_WEATHER_ORDER_ID INT(11) NULL;

-- 6-15
ALTER TABLE supplier_order_element ADD COLUMN ITEM INT(11) NULL;

-- 6-16
ALTER TABLE client_order_element ADD COLUMN ELEMENT_STATUS_ID INT(11) NULL;

ALTER TABLE client_order_element ADD COLUMN split_receive INT(11) NULL;
--6-16
update system_code SET `VALUE`='消耗件库存商' where id=85;
update system_code SET `VALUE`='周转件库存商' where id=86;
update system_code SET `VALUE`='拆机公司' where id=84;

--6-17
alter table client_weather_order_element ADD COLUMN DESCRIPTION VARCHAR(1024) NULL;
-- 6-18
ALTER TABLE client_inquiry ADD COLUMN EMAIL_STATUS INT(11) NOT NULL DEFAULT 0;

--6-19
ALTER TABLE t_gy_fj ADD COLUMN FILE_TYPE INT(11) NULL;
CREATE TABLE `supplier_file_relation` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FILE_TYPE` int(11) NOT NULL,
  `ROLE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1015379 DEFAULT CHARSET=utf8;

INSERT into system_code (id,type,code,value,update_timestamp) values(905,'SUPPLIER_FILE','发票','发票',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(906,'SUPPLIER_FILE','箱单','箱单',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,update_timestamp) values(907,'SUPPLIER_FILE','证书','证书',CURRENT_TIMESTAMP);
INSERT INTO `crm`.`supplier_file_relation` (`ID`, `FILE_TYPE`, `ROLE_ID`) VALUES ('1015379', '905', '6');
INSERT INTO `crm`.`supplier_file_relation` (`ID`, `FILE_TYPE`, `ROLE_ID`) VALUES ('1015380', '906', '6');
INSERT INTO `crm`.`supplier_file_relation` (`ID`, `FILE_TYPE`, `ROLE_ID`) VALUES ('1015381', '907', '3');
INSERT INTO `crm`.`supplier_file_relation` (`ID`, `FILE_TYPE`, `ROLE_ID`) VALUES ('1015382', '907', '7');
INSERT INTO `crm`.`supplier_file_relation` (`ID`, `FILE_TYPE`, `ROLE_ID`) VALUES ('1015383', '907', '8');

-- 6-19
CREATE TABLE `exchange_import_package` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_ORDER_ELEMENT_ID` int(11) NOT NULL,
  `LOCATION` VARCHAR(255) NOT NULL,
	`IMPORT_DATE` date NOT NULL,
	`AMOUNT` DOUBLE(6,2) NOT NULL,
	`CERTIFICATION_ID` INT(11) NOT NULL,
	`CONDITION_ID` INT(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `exchange_export_package` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
	`EXCHANGE_IMPORT_PACKAGE_ID` int(11) NOT NULL,
  `SHIP_NUMBER` VARCHAR(255) NULL,
	`EXPORT_DATE` date NOT NULL,
	`SUPPLIER_ID` int(11) NOT NULL,
	`AMOUNT` DOUBLE(6,2) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 6-21
alter table client_order add COLUMN FREIGHT DOUBLE(8,2) NULL;

ALTER TABLE exchange_import_package ADD COLUMN UPDATE_TIMESTAMP timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

ALTER TABLE exchange_export_package ADD COLUMN UPDATE_TIMESTAMP timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

ALTER TABLE exchange_import_package ADD COLUMN REMARK VARCHAR(2048) NULL;

ALTER TABLE exchange_import_package ADD COLUMN DESCRIPTION VARCHAR(2048) NOT NULL;

ALTER TABLE exchange_export_package ADD COLUMN REMARK VARCHAR(2048) NULL;




--6-20
INSERT INTO system_code (id,type,code,value,update_timestamp) VALUES(400,'CLIENT_CLASSIFY_ID','DBS','DBS',CURRENT_TIMESTAMP);
INSERT INTO system_code (id,type,code,value,update_timestamp) VALUES(401,'CLIENT_CLASSIFY_ID','CAL','CAL',CURRENT_TIMESTAMP);
INSERT INTO system_code (id,type,code,value,update_timestamp) VALUES(402,'CLIENT_CLASSIFY_ID','MRO','MRO',CURRENT_TIMESTAMP);
INSERT INTO system_code (id,type,code,value,update_timestamp) VALUES(403,'CLIENT_CLASSIFY_ID','RPS','RPS',CURRENT_TIMESTAMP);

CREATE TABLE `CLIENT_CLASSIFY` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_CLASSIFY_ID` varchar(255) DEFAULT NULL,
  `CLIENT_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--6-21
ALTER TABLE supplier_pn_relation ADD COLUMN ATA VARCHAR(1024);
ALTER TABLE supplier_pn_relation ADD COLUMN AIRCRAFT VARCHAR(1024);
ALTER TABLE supplier_pn_relation ADD COLUMN `CONDITION` VARCHAR(1024);
ALTER TABLE supplier_pn_relation ADD COLUMN QTY DOUBLE(6,2);
ALTER TABLE supplier_pn_relation ADD COLUMN TYPE INT(11);
INSERT INTO system_code(ID,TYPE,CODE,VALUE,UPDATE_TIMESTAMP) VALUES(404,'SUPPLIER_PN_TYPE','供应能力','供应能力',CURRENT_TIMESTAMP);
INSERT INTO system_code(ID,TYPE,CODE,VALUE,UPDATE_TIMESTAMP) VALUES(405,'SUPPLIER_PN_TYPE','维修能力','维修能力',CURRENT_TIMESTAMP);
INSERT INTO system_code(ID,TYPE,CODE,VALUE,UPDATE_TIMESTAMP) VALUES(406,'SUPPLIER_PN_TYPE','库存能力','库存能力',CURRENT_TIMESTAMP);
INSERT INTO system_code(ID,TYPE,CODE,VALUE,UPDATE_TIMESTAMP) VALUES(407,'SUPPLIER_PN_TYPE','交换能力','交换能力',CURRENT_TIMESTAMP);
ALTER TABLE client_order add COLUMN COMPLETE INT(11) NULL; 
UPDATE client_order set COMPLETE = 1 

-- 6-23
ALTER TABLE t_user ADD COLUMN FULL_NAME VARCHAR(2048) NULL;

--6-26
ALTER TABLE supplier_pn_relation ADD COLUMN SN VARCHAR(255) null;
ALTER TABLE supplier_pn_relation ADD COLUMN REPAIR VARCHAR(1024) NULL;
ALTER TABLE supplier_pn_relation ADD COLUMN CERT VARCHAR(255) NULL;
ALTER TABLE supplier_pn_relation ADD COLUMN REMARK VARCHAR(1024) NULL;

--6-27
ALTER TABLE supplier_pn_relation ADD COLUMN SUPPLY_ABILITY INT(11) null;
ALTER TABLE supplier_pn_relation ADD COLUMN STOCK_ABILITY INT(11) NULL;
ALTER TABLE supplier_pn_relation ADD COLUMN REPAIR_ABILITY INT(11) NULL;
ALTER TABLE supplier_pn_relation ADD COLUMN EXCHANGE_ABILITY INT(11) NULL;

-- 6-29
ALTER TABLE t_user ADD COLUMN MOBILE VARCHAR(255) NULL;

ALTER TABLE client_order_element ADD COLUMN ORIGINAL_AMOUNT	DOUBLE(10,2) NULL;

--6-30
ALTER TABLE supplier_weather_order_element ADD COLUMN SUPPLIER_STATUS INT(11) NULL DEFAULT 1

-- 7-6
CREATE TABLE `SUPPLIER_ANNUAL_OFFER` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PART_NUMBER` varchar(255)  NULL,
  `DESC` varchar(255)  NULL,
	`UNIT` varchar(255)  NULL,
	`MSN` varchar(255)  NULL,
	 `AMOUNT` double(10,2) NOT NULL,
  `PRICE` double(16,2) NOT NULL,
  `LEAD_TIME` varchar(255) DEFAULT NULL,
 `CONDITION_ID` int(11)  NULL,
 `CERTIFICATION_ID` int(11)  NULL,
`REMARK` VARCHAR(1024) NULL,
`LOCATION` varchar(255)  NULL,
`MOQ` int(11)  NULL,
`SUPPLIER_ID` int(11)  NULL,
`VALIDITY` DATE NULL,
	`BSN` varchar(255)  NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `storage_address_for_order` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LINE_ONE` VARCHAR(255) NOT NULL,
  `LINE_TWO` VARCHAR(255) NOT NULL,
	`LINE_THREE` VARCHAR(255) NOT NULL,
	`LINE_FOUR` VARCHAR(255) NOT NULL,
	`LINE_FIVE` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1015379 DEFAULT CHARSET=utf8;

INSERT INTO storage_address_for_order (ID,LINE_ONE,LINE_TWO,LINE_THREE,LINE_FOUR,LINE_FIVE) VALUES (220,'Rm A1, Flat A 9/F., Van Fat Factory Bldg., ','20-22A Ng Fong St., San Po kong, Kowloon,','HongKong','Attn: Glory Chen','Tel : +852 2326 0887 ');
INSERT INTO storage_address_for_order (ID,LINE_ONE,LINE_TWO,LINE_THREE,LINE_FOUR,LINE_FIVE) VALUES (222,'Rm A1, Flat A 9/F., Van Fat Factory Bldg., ','20-22A Ng Fong St., San Po kong, Kowloon,','HongKong','Attn: Glory Chen','Tel : +852 2326 0887 ');
INSERT INTO storage_address_for_order (ID,LINE_ONE,LINE_TWO,LINE_THREE,LINE_FOUR,LINE_FIVE) VALUES (221,'1193 N Eastbury Ave Covina,','CA 91722,','USA','Attn：yinglang liang(梁盈朗)','Tel：+1 6265601325');

-- 7-2
ALTER TABLE exchange_import_package ADD COLUMN SHIP_NUMBER VARCHAR(255) NULL;

-- 7-13
ALTER TABLE exchange_import_package ADD COLUMN SN VARCHAR(512) NULL;

-- 7-20
ALTER TABLE exchange_import_package ADD COLUMN part_number VARCHAR(512) NULL;

CREATE TABLE `client_profitmargin` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_ID` VARCHAR(255) NOT NULL,
  `PROFIT_MARGIN` DOUBLE(6,2) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


--7-24
ALTER TABLE jbpm4_hist_task ADD COLUMN MESSAGE VARCHAR(1024) NULL;
ALTER TABLE jbpm4_task ADD COLUMN MESSAGE VARCHAR(1024) NULL;
--8-3

ALTER TABLE supplier_quote ADD COLUMN CREATE_USER INT(11) DEFAULT null;

ALTER TABLE client_inquiry ADD COLUMN CREATE_USER INT(11) DEFAULT null;

-- 8-7
ALTER TABLE client_inquiry_element ADD COLUMN INQUIRY_STATUS INT(2) DEFAULT 0;

ALTER TABLE client_inquiry_element ADD COLUMN CONDITION_ID INT(11) DEFAULT null;

ALTER TABLE supplier_quote ADD COLUMN SOURCE_NUMBER VARCHAR(255) DEFAULT null;

-- 8-9
ALTER TABLE supplier_inquiry ADD COLUMN EMAIL_STATUS INT(2) DEFAULT 0;

-- 8-23
ALTER TABLE client_quote ADD COLUMN BANK_COST DOUBLE(10,2) DEFAULT 0;

-- 8-24
ALTER TABLE client ADD COLUMN LOWEST_FREIGHT DOUBLE(10,2) DEFAULT 0;
ALTER TABLE client ADD COLUMN FREIGHT DOUBLE(10,2) DEFAULT 0;
ALTER TABLE client ADD COLUMN BANK_COST DOUBLE(10,2) DEFAULT 0;

-- 8-25
ALTER TABLE supplier_commission_sale_element ADD COLUMN DESCRIPTION VARCHAR(512) DEFAULT NULL;
ALTER TABLE supplier_commission_sale_element ADD COLUMN CONDITION_ID INT(11) DEFAULT NULL;
ALTER TABLE supplier_commission_sale_element ADD COLUMN CERTIFICATION_ID INT(11) DEFAULT NULL;
ALTER TABLE supplier_commission_sale_element ADD COLUMN PRICE DOUBLE(10,2) DEFAULT NULL;
ALTER TABLE supplier_commission_sale_element ADD COLUMN UNIT VARCHAR(127) DEFAULT NULL;
ALTER TABLE supplier_commission_sale_element ADD COLUMN SERIAL_NUMBER VARCHAR(127) DEFAULT NULL;
ALTER TABLE supplier_commission_sale_element ADD COLUMN TSN INT(11) DEFAULT NULL;
ALTER TABLE supplier_commission_sale_element ADD COLUMN CSN INT(11) DEFAULT NULL;
ALTER TABLE supplier_commission_sale_element ADD COLUMN VALIDITY date DEFAULT NULL;
ALTER TABLE supplier_commission_sale_element ADD COLUMN ALT VARCHAR(127) DEFAULT NULL;

ALTER TABLE supplier_commission_sale ADD COLUMN QUOTE_DATE date DEFAULT NULL;
ALTER TABLE supplier_commission_sale ADD COLUMN CURRENCY_ID INT(11) DEFAULT NULL;
ALTER TABLE supplier_commission_sale ADD COLUMN VALIDITY date DEFAULT NULL;

-- 8-30
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS100');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS101');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS102');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS103');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS104');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS105');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS106');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS107');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS108');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS109');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS110');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS111');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS112');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS113');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS114');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS115');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS116');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS117');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS118');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS119');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS120');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS121');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS122');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS123');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS124');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS125');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS126');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS127');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS128');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS129');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS130');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS131');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS132');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS133');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS134');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS135');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS136');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS137');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS138');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS139');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS140');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS141');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS142');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS143');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS144');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS145');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS146');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS147');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS148');
INSERT INTO `crm`.`import_storage_location_list` (`LOCATION`) VALUES ('TS149');

-- 9-1
CREATE TABLE `supplier_pn_relation_back_up` (
	`ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUPPLIER_ID` int(11) DEFAULT NULL,
  `BSN` varchar(127) DEFAULT NULL,
  `ATA` varchar(1024) DEFAULT NULL,
  `AIRCRAFT` varchar(1024) DEFAULT NULL,
  `CONDITION` varchar(1024) DEFAULT NULL,
  `QTY` double(6,2) DEFAULT NULL,
  `TYPE` int(11) DEFAULT NULL,
  `SUPPLY_ABILITY` int(11) DEFAULT NULL,
  `STOCK_ABILITY` int(11) DEFAULT NULL,
  `REPAIR_ABILITY` int(11) DEFAULT NULL,
  `EXCHANGE_ABILITY` int(11) DEFAULT NULL,
  `SN` varchar(255) DEFAULT NULL,
  `REPAIR` varchar(1024) DEFAULT NULL,
  `CERT` varchar(255) DEFAULT NULL,
  `REMARK` varchar(1024) DEFAULT NULL,
	`USER_ID` int(11) DEFAULT NULL,
	`MSN` VARCHAR(127) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 9-2
ALTER TABLE supplier_pn_relation_back_up ADD COLUMN PART_NUMBER VARCHAR(127) NOT NULL;
ALTER TABLE supplier_pn_relation_back_up ADD COLUMN DESCRIPTION VARCHAR(255) DEFAULT NULL;

-- 9-5
CREATE TABLE `aviall_quote` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_INQUIRY_ID` int(11) NOT NULL,
  `SUPPLIER_QUOTE_ID` int(11) DEFAULT NULL,
  `COMPLETE` int(11) NOT NULL DEFAULT '0',
  `UPDATE_DATETIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `aviall_quote_element` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PART_NUMBER` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `DESCRIPTION` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UNIT_PRICE` varchar(21) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CURRENCY` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `CERTIFICATION` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `LEAD_TIME` varchar(127) COLLATE utf8_unicode_ci DEFAULT NULL,
  `AVIALL_QUOTE_ID` int(11) NOT NULL,
	`CLIENT_INQUIRY_ELEMENT_ID` int(11) NOT NULL,
	`STOCK_MESSAGE` varchar(127) COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATE_DATETIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

-- 9-6
INSERT INTO certifications (system_code_id,CODE) VALUES ('51','PMA');
INSERT INTO certifications (system_code_id,CODE) VALUES ('51','TSO');
INSERT INTO certifications (system_code_id,CODE) VALUES ('52','EASA');
INSERT INTO certifications (system_code_id,CODE) VALUES ('52','N/R');
INSERT INTO certifications (system_code_id,CODE) VALUES ('52','N/A');
INSERT INTO certifications (system_code_id,CODE) VALUES ('50','GSE');
INSERT INTO certifications (system_code_id,CODE) VALUES ('51','PC');


-- 9-11
ALTER TABLE supplier_contact ADD COLUMN EMAIL_PERSON INT(2) NOT NULL DEFAULT 0;

-- 9-16
ALTER TABLE supplier_quote_element ADD COLUMN AVAILABLE_QTY DOUBLE(10,2) DEFAULT NULL;

-- 9-18
ALTER TABLE aviall_quote_element ADD COLUMN MOQ VARCHAR(127) DEFAULT NULL;

-- 9-21
ALTER TABLE client_inquiry_element ADD COLUMN SHORT_PART_NUMBER VARCHAR(255) DEFAULT NULL;

ALTER  TABLE  client_inquiry_element  ADD  INDEX short_part_index (SHORT_PART_NUMBER)

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(49,'COND','INV','库存清单',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(69,'COND','MRO','维修能力清单',CURRENT_TIMESTAMP);

-- 9-22
ALTER TABLE supplier_commission_sale_element ADD COLUMN LEAD_TIME VARCHAR(255) DEFAULT NULL;

ALTER TABLE supplier_commission_sale_element ADD COLUMN LOCATION VARCHAR(255) DEFAULT NULL;

ALTER TABLE client_inquiry_element ADD COLUMN SOURCE VARCHAR(255) NOT NULL DEFAULT "销售";

-- 9-29
ALTER TABLE exchange_import_package ADD COLUMN EMAIL_STATUS INT(2) DEFAULT 0;

-- 10-9
CREATE TABLE `part_and_email` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_INQUIRY_ID` int(11) NOT NULL,
  `CLIENT_INQUIRY_ELEMENT_ID` int(11) NOT NULL,
  `EMAIL` VARCHAR(127) NOT NULL,
  `EMAIL_STATUS` INT(2) NOT NULL DEFAULT 0,
  `UPDATE_DATETIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE part_and_email ADD COLUMN PART_NUMBER VARCHAR(255) NOT NULL;

ALTER TABLE satair_quote_element ADD COLUMN REMARK VARCHAR(255) NULL DEFAULT NULL;

-- 10-17
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(77,'COND','SV-Exchange','SV-Exchange',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(78,'COND','OH-Exchange','OH-Exchange',CURRENT_TIMESTAMP);

-- 10-18
ALTER TABLE supplier_commission_sale ADD COLUMN SALE_STATUS INT(2) NOT NULL DEFAULT 1;

-- 10-20
ALTER TABLE aviall_quote_element ADD COLUMN REMARK VARCHAR(255) NULL DEFAULT NULL;

-- 10-24
ALTER TABLE supplier ADD COLUMN NAME_IN_STOCKMARKET VARCHAR(255) NULL DEFAULT NULL;


-- 10-25
ALTER TABLE part_and_email ADD COLUMN SEND_TIME TIMESTAMP NULL DEFAULT NULL;

-- 10-26
CREATE TABLE `email_message` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUPPLIER_ID` int(11) DEFAULT NULL,
  `TO` VARCHAR(255) NOT NULL,
	`CC` VARCHAR(255) DEFAULT NULL,
	`BCC` VARCHAR(255) DEFAULT NULL,
  `UPDATE_DATETIME` date NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 10-31
ALTER TABLE supplier_inquiry ADD COLUMN AUTO_ADD INT(2) NOT NULL DEFAULT 0;

-- 11-1
ALTER TABLE client_inquiry ADD COLUMN REAL_DEADLINE date NULL DEFAULT NULL;

ALTER TABLE supplier_quote_element ADD COLUMN SHORT_PART_NUMBER VARCHAR(255) DEFAULT NULL;

ALTER  TABLE  supplier_quote_element  ADD  INDEX short_part_index (SHORT_PART_NUMBER);

-- 11-3
ALTER TABLE client_order_element ADD COLUMN UNIT VARCHAR(255) DEFAULT NULL;

CREATE TABLE `klx_quote` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_INQUIRY_ID` int(11) NOT NULL,
  `SUPPLIER_QUOTE_ID` int(11) DEFAULT NULL,
  `COMPLETE` int(11) NOT NULL DEFAULT '0',
  `UPDATE_DATETIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 11-6
ALTER TABLE client_weather_order_element ADD COLUMN UNIT VARCHAR(255) DEFAULT NULL;

-- 11-7
CREATE TABLE `klx_quote_element` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PART_NUMBER` varchar(765) DEFAULT NULL,
  `DESCRIPTION` varchar(1533) DEFAULT NULL,
  `UNIT_PRICE` varchar(63) DEFAULT NULL,
  `UNIT` varchar(63) DEFAULT NULL,
  `CURRENCY` varchar(381) DEFAULT NULL,
  `CAGE_CODE` varchar(381) DEFAULT NULL,
  `CERTIFICATION` varchar(765) DEFAULT NULL,
  `LEAD_TIME` varchar(381) DEFAULT NULL,
  `KLX_QUOTE_ID` int(11) DEFAULT NULL,
  `CLIENT_INQUIRY_ELEMENT_ID` int(11) DEFAULT NULL,
  `STOCK_MESSAGE` varchar(381) DEFAULT NULL,
  `INFORMATION` varchar(765) DEFAULT NULL,
  `UPDATE_DATETIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `AMOUNT` double(6,2) NOT NULL DEFAULT '1.00',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=561 DEFAULT CHARSET=utf8;

-- 11-8
ALTER TABLE supplier_commission_sale ADD COLUMN CLIENT_INQUIRY_ID INT(11) DEFAULT NULL;

ALTER TABLE client_inquiry_element ADD COLUMN AIM_PRICE VARCHAR(127) DEFAULT NULL;

ALTER TABLE supplier_quote_element ADD COLUMN CORE_CHARGE VARCHAR(127) DEFAULT NULL;

-- 11-11
ALTER TABLE part_and_email ADD COLUMN SUPPLIER_INQUIRY_ID INT(11) NULL DEFAULT NULL;

CREATE TABLE `stock_market` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_INQUIRY_ID` int(11) NOT NULL,
  `CLIENT_INQUIRY_ELEMENT_ID` int(11) NOT NULL,
  `NAME` varchar(127) DEFAULT NULL,
  `UPDATE_DATETIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `SEND_STATUS` int(2) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=464 DEFAULT CHARSET=utf8;

-- 11-21
CREATE TABLE `kapco_quote_element` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PART_NUMBER` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `DESCRIPTION` varchar(511) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `UNIT_PRICE` varchar(21) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `UNIT` varchar(21) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `CURRENCY` varchar(127) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `CAGE_CODE` varchar(127) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `CERTIFICATION` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT 'MFR CERT',
  `LEAD_TIME` varchar(127) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT '0',
  `KAPCO_QUOTE_ID` int(11) DEFAULT NULL,
  `CLIENT_INQUIRY_ELEMENT_ID` int(11) DEFAULT NULL,
  `STOCK_MESSAGE` varchar(127) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `INFORMATION` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATE_DATETIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `AMOUNT` int(1) NOT NULL DEFAULT '1',
  `ISREPLACE` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8

CREATE TABLE `kapco_quote` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_INQUIRY_ID` int(11) NOT NULL,
  `SUPPLIER_QUOTE_ID` int(11) DEFAULT NULL,
  `COMPLETE` int(11) NOT NULL DEFAULT '0',
  `UPDATE_DATETIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 11-22
ALTER TABLE part_and_email ADD COLUMN SOURCE VARCHAR(127) DEFAULT NULL;

-- 11-25
ALTER TABLE client_inquiry ADD COLUMN CRAWL_EMAIL INT(2) DEFAULT NULL;

ALTER TABLE part_and_email ADD COLUMN CANCEL INT(2) DEFAULT NULL;

-- 11-27
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('713', 'ELEMENT_STATUS', '14', '报价更新', NULL, CURRENT_TIMESTAMP());

INSERT INTO `crm`.`certifications` (`ID`, `SYSTEM_CODE_ID`, `CODE`) VALUES ('21', '55', 'STD');

-- 11-28
ALTER TABLE aviall_quote_element ADD COLUMN IF_DANGER INT(2) DEFAULT 0;

-- 12-1
ALTER TABLE client_invoice ADD COLUMN CLIENT_SHIP_ID INT(11) DEFAULT NULL;

-- 12-4
CREATE TABLE `txtav_quote_element` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PART_NUMBER` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `DESCRIPTION` varchar(511) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `UNIT_PRICE` varchar(21) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `UNIT` varchar(21) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `CURRENCY` varchar(127) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `CAGE_CODE` varchar(127) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `CERTIFICATION` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `LEAD_TIME` varchar(127) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT '0',
  `TXTAV_QUOTE_ID` int(11) DEFAULT NULL,
  `CLIENT_INQUIRY_ELEMENT_ID` int(11) DEFAULT NULL,
  `STOCK_MESSAGE` varchar(127) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `INFORMATION` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `UPDATE_DATETIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `AMOUNT` int(1) NOT NULL DEFAULT '1',
  `ISREPLACE` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;

CREATE TABLE `txtav_quote` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_INQUIRY_ID` int(11) NOT NULL,
  `SUPPLIER_QUOTE_ID` int(11) DEFAULT NULL,
  `COMPLETE` int(11) NOT NULL DEFAULT '0',
  `UPDATE_DATETIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 12-6
ALTER TABLE client_inquiry ADD COLUMN CREATE_TIMESTAMP timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ;

UPDATE client_inquiry SET CREATE_TIMESTAMP = UPDATE_TIMESTAMP;

-- 12-7
ALTER TABLE t_part ADD COLUMN REMARK VARCHAR(511) DEFAULT NULL;

-- 12-8
ALTER TABLE client ADD COLUMN LOCATION VARCHAR(127) DEFAULT NULL;

ALTER TABLE supplier ADD COLUMN LOCATION VARCHAR(127) DEFAULT NULL;

ALTER TABLE supplier_quote ADD COLUMN FEE_FOR_EXCHANGE_BILL VARCHAR(127) DEFAULT NULL;

-- 12-11
ALTER  TABLE  client_inquiry_element  ADD  INDEX IDX_MAIN_ID (MAIN_ID);

-- 12-12
ALTER  TABLE  import_package_element  ADD  INDEX IDX_LOCATION (LOCATION);

ALTER TABLE supplier_quote_element ADD COLUMN FEE_FOR_EXCHANGE_BILL DOUBLE(6,2) DEFAULT NULL;

-- 12-20
CREATE TABLE `vas_stroge` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PART_NUMBER` VARCHAR(127) NOT NULL,
	`DESCRIPTION` VARCHAR(255) DEFAULT NULL,
  `ABILITY` VARCHAR(127) DEFAULT NULL ,
  `AMOUNT` DOUBLE(6,2) DEFAULT NULL,
	`REMARK` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE vas_stroge ADD COLUMN CREATE_TIMESTAMP timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
-- 12-26
ALTER  TABLE  export_package_instructions_element  ADD  INDEX IDX_IMPORT_PACKAGE_ELEMENT_ID (IMPORT_PACKAGE_ELEMENT_ID);

-- 12-28
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000052', 'BIZ_TYPE', '9', '促销清单', NULL, CURRENT_TIMESTAMP);

ALTER TABLE vas_stroge ADD COLUMN SHORT_PART_NUMBER VARCHAR(127) NOT NULL;

-- 12-29
ALTER TABLE vas_stroge ADD COLUMN FILE_PATH VARCHAR(255) DEFAULT NULL;

ALTER TABLE vas_stroge ADD COLUMN FILE_NAME VARCHAR(255) DEFAULT NULL;


-- 1-9
ALTER TABLE supplier_quote_element ADD COLUMN BANK_COST DOUBLE(6,2) DEFAULT NULL;

-- 1-16
CREATE TABLE `client_weather_order_element_back_up` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
	`USER_ID` int(11) NOT NULL,
  `ITEM` int(11) NOT NULL,
  `PART_NUMBER` VARCHAR(255) NOT NULL,
  `ERROR` VARCHAR(255) NOT NULL,
  `LINE` int(11) NOT NULL,
  `CLIENT_WEATHER_ORDER_ID` int(11) NOT NULL,
  `CLIENT_QUOTE_ELEMENT_ID` int(11) NOT NULL,
  `AMOUNT` double(10,2) NOT NULL,
  `PRICE` double(16,2) NOT NULL,
  `LEAD_TIME` varchar(255) DEFAULT NULL,
  `DEADLINE` date DEFAULT NULL,
  `UPDATE_TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `CERTIFICATION_ID` int(11) DEFAULT NULL,
  `FIXED_COST` double(6,2) DEFAULT '0.00',
  PRIMARY KEY (`ID`),
  FOREIGN KEY (`CLIENT_WEATHER_ORDER_ID`) REFERENCES `client_weather_order` (`ID`),
  FOREIGN KEY (`CLIENT_QUOTE_ELEMENT_ID`) REFERENCES `client_quote_element` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

alter table client_weather_order_element_back_up add COLUMN REMARK VARCHAR(1024) NULL;
alter table client_weather_order_element_back_up add COLUMN BANK_CHARGES DOUBLE(6,2) NULL;
alter table client_weather_order_element_back_up ADD COLUMN DESCRIPTION VARCHAR(1024) NULL;
ALTER TABLE client_weather_order_element_back_up ADD COLUMN UNIT VARCHAR(255) DEFAULT NULL;
ALTER TABLE client_weather_order_element_back_up ADD COLUMN ERROR_FLAG INT(2) NOT NULL COMMENT '错误为1，已处理或者正确为0，不上传为2';


-- 1-20
ALTER TABLE client_inquiry ADD COLUMN QQ_MAIL_SEND INT(2) NOT NULL DEFAULT 0;

-- 1-24
ALTER TABLE supplier_quote ADD COLUMN BANK_COST DOUBLE(6,2) DEFAULT NULL;

-- 1-25
ALTER TABLE supplier_order ADD COLUMN BANK_COST DOUBLE(6,2) DEFAULT NULL;
ALTER TABLE supplier_order ADD COLUMN FEE_FOR_EXCHANGE_BILL DOUBLE(6,2) DEFAULT NULL;
ALTER TABLE supplier_order ADD COLUMN OTHER_FEE DOUBLE(6,2) DEFAULT NULL;

-- 1-26
ALTER TABLE supplier_weather_order_element ADD COLUMN BANK_COST DOUBLE(6,2) DEFAULT NULL;
ALTER TABLE supplier_weather_order_element ADD COLUMN FEE_FOR_EXCHANGE_BILL DOUBLE(6,2) DEFAULT NULL;
ALTER TABLE supplier_weather_order_element ADD COLUMN OTHER_FEE DOUBLE(6,2) DEFAULT NULL;


-- 1-30
CREATE TABLE `supplier_weather_order` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUPPLIER_ID` int(11) NOT NULL,
	`CLIENT_WEATHER_ORDER_ID` int(11) NOT NULL,
  `BANK_COST` DOUBLE(6,2) DEFAULT NULL ,
  `FEE_FOR_EXCHANGE_BILL` DOUBLE(6,2) DEFAULT NULL,
	`OTHER_FEE` DOUBLE(6,2) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 2-3
ALTER TABLE supplier_quote_element ADD COLUMN HAZMAT_FEE DOUBLE(6,2) DEFAULT NULL;

-- 2-5
ALTER TABLE supplier ADD COLUMN CREDIT_FEE DOUBLE(8,2) DEFAULT NULL;

ALTER TABLE supplier ADD COLUMN CREDIT_LIMIT DOUBLE(8,2) DEFAULT NULL;

-- 2-8
ALTER TABLE supplier_order_element ADD COLUMN BANK_COST DOUBLE(6,2) DEFAULT NULL;
ALTER TABLE supplier_order_element ADD COLUMN FEE_FOR_EXCHANGE_BILL DOUBLE(6,2) DEFAULT NULL;
ALTER TABLE supplier_order_element ADD COLUMN OTHER_FEE DOUBLE(6,2) DEFAULT NULL;

-- 2-24
ALTER TABLE txtav_quote_element ADD COLUMN DANGER INT(2) DEFAULT 0;

ALTER TABLE kapco_quote_element ADD COLUMN DANGER INT(2) DEFAULT 0;

-- 2-26
CREATE TABLE `high_price_crawl_quote` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUPPLIER_QUOTE_ID` int(11) NOT NULL,
	`IS_SEND` INT(2) NOT NULL DEFAULT 0,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--2-27
ALTER TABLE high_price_crawl_quote ADD COLUMN PERSON VARCHAR(127) DEFAULT NULL;

ALTER TABLE import_package ADD COLUMN IMPORT_FEE DOUBLE(8,2) DEFAULT 0;

ALTER TABLE import_package ADD COLUMN FREIGHT DOUBLE(8,2) DEFAULT 0;

ALTER TABLE export_package ADD COLUMN EXPORT_FEE DOUBLE(8,2) DEFAULT 0;

ALTER TABLE export_package ADD COLUMN FREIGHT DOUBLE(8,2) DEFAULT 0;

-- 3-2
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000055,'COND','OH-Exchange(flat rate)','OH-Exchange(flat rate)',CURRENT_TIMESTAMP);

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000056,'COND','SV-Exchange(flat rate)','SV-Exchange(flat rate)',CURRENT_TIMESTAMP);

-- 3-3
ALTER TABLE supplier_quote_element ADD COLUMN OTHER_FEE DOUBLE(6,2) DEFAULT 0;

ALTER TABLE supplier_order_element ADD COLUMN PROFIT_OTHER_FEE DOUBLE(6,2) DEFAULT NULL;

-- 3-13
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000057,'REPAIR_TYPE','1','维修',CURRENT_TIMESTAMP);

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000058,'REPAIR_TYPE','2','交换',CURRENT_TIMESTAMP);

ALTER TABLE exchange_import_package ADD COLUMN REPAIR_TYPE INT(11) DEFAULT NULL;

ALTER  TABLE  import_package_element  ADD  INDEX part_index (PART_NUMBER);

ALTER  TABLE  authority_relation  ADD  INDEX INDEX_SUPPLIER_ID (SUPPLIER_ID);


-- 3-15
-- crmstock
CREATE TABLE `system_code` (
  `ID` int(11) NOT NULL,
  `TYPE` varchar(32) NOT NULL,
  `CODE` varchar(32) NOT NULL,
  `VALUE` varchar(32) NOT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  `UPDATE_TIMESTAMP` datetime NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_SYSTEM_CODE_1` (`TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000001,'WEIGHT','1','LBS',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000002,'WEIGHT','2','KG',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000003,'DIMENTIONS','1','INCH',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000004,'DIMENTIONS','2','CM',CURRENT_TIMESTAMP);

alter table t_part add COLUMN WEIGHT_UNIT_ID INT(11) DEFAULT NULL ;
alter table t_part add COLUMN DIMENTIONS_UNIT_ID INT(11) DEFAULT NULL ;

-- 3-16
alter table client_quote_element add COLUMN moq DOUBLE(8,2) DEFAULT NULL ;

-- 3-23
ALTER TABLE client_inquiry_element ADD INDEX BSN_INDEX (`BSN`);
ALTER TABLE client_inquiry_element ADD INDEX STATUS_INDEX (`ELEMENT_STATUS_ID`);
ALTER TABLE authority_relation ADD INDEX CLIENT_INDEX (`CLIENT_ID`);
ALTER TABLE client ADD INDEX code_index (`code`);
ALTER TABLE supplier ADD INDEX code_index (`code`);
alter table export_package add COLUMN REAL_EXPORT_DATE DATE DEFAULT NULL ;

-- 3-26
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000070,'COND','NEW exchange-flat rate','NEW exchange-flat rate',CURRENT_TIMESTAMP);

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000071,'COND','New exchange-plus repair cost','New exchange-plus repair cost',CURRENT_TIMESTAMP);

-- 3-27
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000072,'COND','Modify','Modify',CURRENT_TIMESTAMP);

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000073,'CERT','FAA/EASA','FAA/EASA',CURRENT_TIMESTAMP);

alter table supplier add COLUMN IS_AGENT_ID INT(11) DEFAULT 1000075 COMMENT "是否代理";

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000074,'IS_AGENT','是代理','是',CURRENT_TIMESTAMP);

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000075,'IS_AGENT','不是代理','否',CURRENT_TIMESTAMP);

UPDATE export_package_instructions SET `CODE` = CONCAT(3,`CODE`) WHERE `CODE` in (13,40,70,20,17,21,15,18,12);

UPDATE export_package_instructions SET `CODE` = CONCAT(8,`CODE`) WHERE `CODE` in (85,86);

-- 3-29
alter table supplier_commission_sale_element add COLUMN FEE_FOR_EXCHANGE_BILL DOUBLE(6,2) DEFAULT NULL ;
alter table supplier_commission_sale_element add COLUMN BANK_COST DOUBLE(6,2) DEFAULT NULL ;
alter table supplier_commission_sale_element add COLUMN HAZMAT_FEE DOUBLE(6,2) DEFAULT NULL ;
alter table supplier_commission_sale_element add COLUMN OTHER_FEE DOUBLE(6,2) DEFAULT NULL ;
alter table supplier_commission_sale_element add COLUMN TAG_SRC VARCHAR(255) DEFAULT NULL ;
alter table supplier_commission_sale_element add COLUMN TAG_DATE VARCHAR(255) DEFAULT NULL ;
alter table supplier_commission_sale_element add COLUMN TRACE VARCHAR(255) DEFAULT NULL ;
alter table supplier_commission_sale_element add COLUMN WARRANTY VARCHAR(255) DEFAULT NULL ;
alter table supplier_commission_sale_element add COLUMN SHORT_PART_NUMBER VARCHAR(255) DEFAULT NULL ;
alter table supplier_commission_sale_element add COLUMN CORE_CHARGE VARCHAR(127) DEFAULT NULL ;

-- 4-3
ALTER TABLE `supplier` ADD COLUMN `MOV_PER_LINE`  VARCHAR(255) DEFAULT NULL;
ALTER TABLE `supplier` ADD COLUMN `MOV_PER_ORDER`  VARCHAR(255) DEFAULT NULL;
ALTER TABLE `supplier` ADD COLUMN `MOV_PER_ORDER_CURRENCY_ID`  INT(11) DEFAULT NULL;

ALTER TABLE client_order ADD COLUMN `URGENT_LEVEL_ID`  INT(11) DEFAULT NULL;
ALTER TABLE client_weather_order ADD COLUMN `URGENT_LEVEL_ID`  INT(11) DEFAULT NULL;

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000080,'URGENT_LEVEL','1','Normal',CURRENT_TIMESTAMP);

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000081,'URGENT_LEVEL','2','Critial',CURRENT_TIMESTAMP);

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000082,'URGENT_LEVEL','3','AOG',CURRENT_TIMESTAMP);

-- 4-4
ALTER TABLE supplier_order ADD COLUMN `URGENT_LEVEL_ID`  INT(11) DEFAULT NULL;

-- 4-8
CREATE TABLE `storage_correlation` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `IMPORT_PACKAGE_ELEMENT_ID` int(11) NOT NULL,
  `CORRELATION_IMPORT_PACKAGE_ELEMENT_ID` VARCHAR(255) NOT NULL,
  `UPDATE_TIMESTAMP` datetime DEFAULT CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 4-25
ALTER  TABLE  txtav_quote_element  ADD  INDEX INDEX_TXTAV_QUOTE_ID(TXTAV_QUOTE_ID);
ALTER  TABLE  txtav_quote_element  ADD  INDEX INDEX_CLIENT_INQUIRY_ELEMENT_ID(CLIENT_INQUIRY_ELEMENT_ID);
ALTER  TABLE  txtav_quote  ADD  INDEX INDEX_CLIENT_INQUIRY_ID(CLIENT_INQUIRY_ID);

ALTER  TABLE  kapco_quote_element  ADD  INDEX INDEX_KAPCO_QUOTE_ID(KAPCO_QUOTE_ID);
ALTER  TABLE  kapco_quote_element  ADD  INDEX INDEX_CLIENT_INQUIRY_ELEMENT_ID(CLIENT_INQUIRY_ELEMENT_ID);
ALTER  TABLE  kapco_quote  ADD  INDEX INDEX_CLIENT_INQUIRY_ID(CLIENT_INQUIRY_ID);

ALTER  TABLE  klx_quote_element  ADD  INDEX INDEX_KLX_QUOTE_ID(KLX_QUOTE_ID);
ALTER  TABLE  klx_quote_element  ADD  INDEX INDEX_CLIENT_INQUIRY_ELEMENT_ID(CLIENT_INQUIRY_ELEMENT_ID);
ALTER  TABLE  klx_quote  ADD  INDEX INDEX_CLIENT_INQUIRY_ID(CLIENT_INQUIRY_ID);

ALTER  TABLE  aviall_quote_element  ADD  INDEX INDEX_AVIALL_QUOTE_ID(AVIALL_QUOTE_ID);
ALTER  TABLE  aviall_quote_element  ADD  INDEX INDEX_CLIENT_INQUIRY_ELEMENT_ID(CLIENT_INQUIRY_ELEMENT_ID);
ALTER  TABLE  aviall_quote  ADD  INDEX INDEX_CLIENT_INQUIRY_ID(CLIENT_INQUIRY_ID);

ALTER  TABLE  satair_quote_element  ADD  INDEX INDEX_SATAIR_QUOTE_ID(SATAIR_QUOTE_ID);
ALTER  TABLE  satair_quote  ADD  INDEX INDEX_CLIENT_INQUIRY_ID(CLIENT_INQUIRY_ID);

-- 4-27
CREATE TABLE `mpi_message` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(127) DEFAULT NULL,
	`CODE` VARCHAR(127) DEFAULT NULL,
  `ADDRESS` VARCHAR(255) DEFAULT NULL,
  `TEL` VARCHAR(127) DEFAULT NULL,
	`EMAIL` VARCHAR(127) DEFAULT NULL,
	`FAX` VARCHAR(127) DEFAULT NULL,
	`AOG_DESK_EMAIL` VARCHAR(127) DEFAULT NULL,
	`AOG_HOTLINE` VARCHAR(127) DEFAULT NULL,
	`CONTACT` VARCHAR(127) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO mpi_message (`NAME`,`CODE`,`ADDRESS`,`TEL`,`EMAIL`,`FAX`,`AOG_DESK_EMAIL`,`AOG_HOTLINE`,`CONTACT`)
VALUES ('New York Headquarters office','MPI-New-York','179-15 149th Road,Jamaica, NY 11434','(718) 244-6216','jfk@marketpioneer.com','(718) 244-1341','aog.jfk@marketpioneer.com','(516) 754-9423','Wesley Phoo');
INSERT INTO mpi_message (`NAME`,`CODE`,`ADDRESS`,`TEL`,`EMAIL`,`FAX`,`AOG_DESK_EMAIL`,`AOG_HOTLINE`,`CONTACT`)
VALUES ('Los Angeles Office','MPI-Los-Angeles','18024 Figueroa Street,Gardena, CA 90248','(310) 803-8880','lax@marketpioneer.com','(310) 803-8885',' aog.lax@marketpioneer.com','(310) 989 - 9918','');
INSERT INTO mpi_message (`NAME`,`CODE`,`ADDRESS`,`TEL`,`EMAIL`,`FAX`,`AOG_DESK_EMAIL`,`AOG_HOTLINE`,`CONTACT`)
VALUES ('Seattle Office','MPI-Seattle','835 South 192nd Street, Suite #700SeaTac, WA 98148','(253) 214-0800','sea@marketpioneer.com','(253) 214-0801','aog.sea@marketpioneer.com','(206) 909 - 0426','');
INSERT INTO mpi_message (`NAME`,`CODE`,`ADDRESS`,`TEL`,`EMAIL`,`FAX`,`AOG_DESK_EMAIL`,`AOG_HOTLINE`,`CONTACT`)
VALUES ('Chicago Office','MPI-Chicago','835 Greenleaf Avenue,Elk Grove Village, IL 60007','(224) 366-5399','ord@marketpioneer.com','(224) 366-5389','ord@marketpioneer.com','(224) 805 - 7345,Backup1: (708) 380 - 8522,Backup2: (929) 225 - 0189','Anne Yang/Starsky Evangelista');
INSERT INTO mpi_message (`NAME`,`CODE`,`ADDRESS`,`TEL`,`EMAIL`,`FAX`,`AOG_DESK_EMAIL`,`AOG_HOTLINE`,`CONTACT`)
VALUES ('Dallas Office','MPI-Dallas','8925 Sterling St, #270Irving, TX 75063','(214) 260-3133','dfw@marketpioneer.com',' (214) 260-3134',' aog.dfw@marketpioneer.com','(972) 816-1858','Daniel');
INSERT INTO mpi_message (`NAME`,`CODE`,`ADDRESS`,`TEL`,`EMAIL`,`FAX`,`AOG_DESK_EMAIL`,`AOG_HOTLINE`,`CONTACT`)
VALUES ('Miami Office','MPI-Miami','10807 NW 29TH Street,Doral, FL 33172','(786) 332-4143','mia@marketpioneer.com','','aog.mia@marketpioneer.com','(786) 229-1399','Nathan Wang');

ALTER TABLE mpi_message ADD COLUMN UPDATE_TIMESTAMP TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- 5-3
CREATE TABLE `supplier_order_element_fj` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SUPPLIER_ORDER_ELEMENT_IDS` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE supplier_order_element_fj ADD COLUMN UPDATE_TIMESTAMP TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- 5-4
CREATE TABLE `unknow_storage_detail` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PART_NUMBER` VARCHAR(127) DEFAULT NULL,
  `DESCRIPTION` VARCHAR(255) DEFAULT NULL,
  `AMOUNT` DOUBLE(8,2) DEFAULT NULL,
  `USE_AMOUNT` DOUBLE(8,2) DEFAULT NULL,
  `CERTIFICATION` VARCHAR(127) DEFAULT NULL,
  `SN` VARCHAR(127) DEFAULT NULL,
  `REMARK` VARCHAR(127) DEFAULT NULL,
  `UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `send_back_message` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `EXPORT_PACKAGE_ELEMENT_ID` int(11) NOT NULL,
  `MANAGE_STATUS` int(11) NOT NULL,
  `AMOUNT` DOUBLE(8,2) NOT NULL,
  `REMARK` VARCHAR(127) DEFAULT NULL,
  `UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 5-5
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000086,'SEND_BACK_MANAGE_STATUS','1','已入库',CURRENT_TIMESTAMP);

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000087,'SEND_BACK_MANAGE_STATUS','2','已发供应商',CURRENT_TIMESTAMP);

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000088,'SEND_BACK_MANAGE_STATUS','3','已完成',CURRENT_TIMESTAMP);

-- 5-8
ALTER TABLE unknow_storage_detail ADD COLUMN LOCATION VARCHAR(127) DEFAULT NULL;

-- 5-9
ALTER TABLE import_package ADD COLUMN FEE_CURRENCY_ID INT(11) DEFAULT NULL;

ALTER TABLE export_package ADD COLUMN FEE_CURRENCY_ID INT(11) DEFAULT NULL;

ALTER TABLE export_package ADD COLUMN FEE_RATE DOUBLE(6,2) DEFAULT NULL;

ALTER TABLE import_package ADD COLUMN FEE_RATE DOUBLE(6,2) DEFAULT NULL;

-- 5-10
CREATE TABLE `planespotters` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSN` VARCHAR(127) DEFAULT NULL,
	`LN` VARCHAR(127) DEFAULT NULL,
	`AIRCRAFT_TYPE` VARCHAR(127) DEFAULT NULL,
	`REG` VARCHAR(127) DEFAULT NULL,
	`AIRLINE` VARCHAR(127) DEFAULT NULL,
	`DELIVERED` VARCHAR(127) DEFAULT NULL,
	`STATUS` VARCHAR(127) DEFAULT NULL,
	`PREV_REG` VARCHAR(127) DEFAULT NULL,
	`REMARK` VARCHAR(127) DEFAULT NULL,
  `UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 5-14
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000089', 'LOGISTICS_WAY', 'PICK UP', 'PICK UP', NULL, '2018-05-11 16:40:20');

alter TABLE planespotters ADD COLUMN PAGE_NUM VARCHAR(127) DEFAULT NULL;

-- 5-16
ALTER TABLE supplier ADD COLUMN IBAN VARCHAR(127) DEFAULT NULL;
ALTER TABLE supplier ADD COLUMN SWIFT_CODE VARCHAR(127) DEFAULT NULL;
ALTER TABLE supplier ADD COLUMN ROUTING VARCHAR(127) DEFAULT NULL;

-- 5-28
INSERT INTO `crm`.`system_code` (
	`ID`,
	`TYPE`,
	`CODE`,
	`VALUE`,
	`REMARK`,
	`UPDATE_TIMESTAMP`
)
VALUES
	(
		'1000090',
		'ORDER_STATUS',
		'TO_BE_CONFIRMED',
		'待确认',
		NULL,
		CURRENT_TIMESTAMP
	);
INSERT INTO `crm`.`system_code` (
	`ID`,
	`TYPE`,
	`CODE`,
	`VALUE`,
	`REMARK`,
	`UPDATE_TIMESTAMP`
)
VALUES
	(
		'1000091',
		'ORDER_STATUS',
		'CONFIRMED',
		'已确认',
		NULL,
		CURRENT_TIMESTAMP
	);
INSERT INTO `crm`.`system_code` (
	`ID`,
	`TYPE`,
	`CODE`,
	`VALUE`,
	`REMARK`,
	`UPDATE_TIMESTAMP`
)
VALUES
	(
		'1000092',
		'ORDER_STATUS',
		'ALREADY_PAID',
		'已付款',
		NULL,
		CURRENT_TIMESTAMP
	);

-- 5-29
ALTER TABLE jbpm4_task ADD INDEX INDEX_RELATION_ID (`RELATION_ID`);

-- 5-30
ALTER TABLE import_package_element ADD COLUMN HAS_LIFE INT(2) DEFAULT 0;

ALTER TABLE import_package_element ADD COLUMN EXPIRE_DATE date DEFAULT NULL;


ALTER TABLE import_package_element ADD COLUMN REST_LIFE INT(3) DEFAULT NULL;

ALTER TABLE import_package_element ADD COLUMN REST_LIFE_EMAIL INT(2) NOT NULL DEFAULT 0;

-- 6-5
INSERT INTO `crm`.`system_code` (
	`ID`,
	`TYPE`,
	`CODE`,
	`VALUE`,
	`REMARK`,
	`UPDATE_TIMESTAMP`
)
VALUES
	(
		'1000093',
		'ORDER_STATUS',
		'CANCEL ORDER',
		'取消订单',
		NULL,
		CURRENT_TIMESTAMP
	);
	
	
-- 6-6
ALTER TABLE import_package ADD COLUMN CREATE_USER INT(11) DEFAULT NULL;

ALTER TABLE import_package ADD COLUMN LAST_UPDATE_USER INT(11) DEFAULT NULL;

ALTER TABLE export_package ADD COLUMN CREATE_USER INT(11) DEFAULT NULL;

ALTER TABLE export_package ADD COLUMN LAST_UPDATE_USER INT(11) DEFAULT NULL;

ALTER TABLE import_package_element ADD COLUMN CERTIFICATION_NUMBER VARCHAR(127) DEFAULT NULL;

CREATE TABLE `supplier_aptitude` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(127) NOT NULL,
	`EXPIRE_DATE` date DEFAULT NULL,
	`LAST_UPDATE_USER` INT(11) DEFAULT NULL,
  `UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE supplier_aptitude ADD COLUMN SUPPLIER_ID INT(11) NOT NULL;

INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000094,'APTITUDE_CODE','1','ASA',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000095,'APTITUDE_CODE','2','ATA',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000096,'APTITUDE_CODE','3','CASE',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000097,'APTITUDE_CODE','4','ISO series',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000098,'APTITUDE_CODE','5','CAAC',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000099,'APTITUDE_CODE','6','FAA',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000100,'APTITUDE_CODE','7','EASA',CURRENT_TIMESTAMP);
INSERT into system_code (id,type,code,value,UPDATE_timestamp) values(1000101,'APTITUDE_CODE','8','Authorization letter of OME',CURRENT_TIMESTAMP);

-- 6-6
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','UK',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','USA',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Ireland',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Germany',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','France',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Israel',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Thailand',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Netherland',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Italty',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','lithuania',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Monaco',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Norway',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Switzerland',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Spain',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Philippines',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Japan',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Singapore',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Canada',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','China',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Mexico',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','New-zealand',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Austria',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Poland',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Belgium',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Denmark',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Finland',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Czech',CURRENT_TIMESTAMP);
INSERT into system_code (type,value,UPDATE_timestamp) values('COUNTRY','Sweden',CURRENT_TIMESTAMP);

INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000102', 'COUNTRY', 'UK', 'UK', NULL, '2018-06-07 14:19:28');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000103', 'COUNTRY', 'USA', 'USA', NULL, '2018-06-07 14:19:28');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000104', 'COUNTRY', 'Ireland', 'Ireland', NULL, '2018-06-07 14:19:28');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000105', 'COUNTRY', 'Germany', 'Germany', NULL, '2018-06-07 14:19:28');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000106', 'COUNTRY', 'France', 'France', NULL, '2018-06-07 14:19:28');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000107', 'COUNTRY', 'Israel', 'Israel', NULL, '2018-06-07 14:19:28');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000108', 'COUNTRY', 'Thailand', 'Thailand', NULL, '2018-06-07 14:19:28');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000109', 'COUNTRY', 'Netherland', 'Netherland', NULL, '2018-06-07 14:19:28');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000110', 'COUNTRY', 'Italty', 'Italty', NULL, '2018-06-07 14:19:28');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000111', 'COUNTRY', 'lithuania', 'lithuania', NULL, '2018-06-07 14:19:28');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000112', 'COUNTRY', 'Monaco', 'Monaco', NULL, '2018-06-07 14:19:28');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000113', 'COUNTRY', 'Norway', 'Norway', NULL, '2018-06-07 14:19:28');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000114', 'COUNTRY', 'Switzerland', 'Switzerland', NULL, '2018-06-07 14:19:29');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000115', 'COUNTRY', 'Spain', 'Spain', NULL, '2018-06-07 14:19:29');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000116', 'COUNTRY', 'Philippines', 'Philippines', NULL, '2018-06-07 14:19:29');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000117', 'COUNTRY', 'Japan', 'Japan', NULL, '2018-06-07 14:19:29');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000118', 'COUNTRY', 'Singapore', 'Singapore', NULL, '2018-06-07 14:19:29');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000119', 'COUNTRY', 'Canada', 'Canada', NULL, '2018-06-07 14:19:29');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000120', 'COUNTRY', 'China', 'China', NULL, '2018-06-07 14:19:29');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000121', 'COUNTRY', 'Mexico', 'Mexico', NULL, '2018-06-07 14:19:29');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000122', 'COUNTRY', 'New-zealand', 'New-zealand', NULL, '2018-06-07 14:19:29');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000123', 'COUNTRY', 'Austria', 'Austria', NULL, '2018-06-07 14:19:29');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000124', 'COUNTRY', 'Poland', 'Poland', NULL, '2018-06-07 14:19:29');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000125', 'COUNTRY', 'Belgium', 'Belgium', NULL, '2018-06-07 14:19:29');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000126', 'COUNTRY', 'Denmark', 'Denmark', NULL, '2018-06-07 14:19:29');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000127', 'COUNTRY', 'Finland', 'Finland', NULL, '2018-06-07 14:19:29');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000128', 'COUNTRY', 'Czech', 'Czech', NULL, '2018-06-07 14:19:29');
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000129', 'COUNTRY', 'Sweden', 'Sweden', NULL, '2018-06-07 14:19:29');

ALTER TABLE supplier ADD COLUMN COUNTRY_ID INT(11) DEFAULT NULL;

-- 6-12
ALTER TABLE supplier_quote ADD COLUMN LAST_UPDATE_USER INT(11) DEFAULT NULL;

-- 6-13
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC001');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC002');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC003');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC004');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC005');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC006');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC007');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC008');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC009');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC010');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC011');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC012');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC013');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC014');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC015');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC016');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC017');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC018');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC019');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC020');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC021');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC022');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC023');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC024');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC025');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC026');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC027');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC028');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC029');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC030');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC031');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC032');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC033');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC034');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC035');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC036');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC037');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC038');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC039');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC040');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC041');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC042');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC043');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC044');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC045');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC046');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC047');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC048');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC049');
INSERT INTO `import_storage_location_list` (`LOCATION`) VALUES ('KC050');

-- 6-14
CREATE TABLE `storage_to_order_email` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CLIENT_ID` INT(11) NOT NULL,
	`NOW_IMPORTPACK_NUMBER` VARCHAR(127) NOT NULL,
	`OLD_IMPORTPACK_NUMBER` VARCHAR(127) NOT NULL,
	`PART_NUMBER` VARCHAR(127) NOT NULL,
	`DESCRIPTION` VARCHAR(127) DEFAULT NULL,
	`ORDER_NUMBER` VARCHAR(127) NOT NULL,
	`SUPPLIER_ID` INT(11) DEFAULT NULL,
	`USER_ID` INT(11) NOT NULL,
	`EMAIL_STATUS` INT(11) NOT NULL DEFAULT 0,
  `UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 6-15
CREATE TABLE `check_storage_by_location` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `IMPORT_PACKAGE_ELEMENT_ID` INT(11) NOT NULL,
	`LOCATION` VARCHAR(127) NOT NULL,
  PRIMARY KEY (`ID`),
	FOREIGN KEY(IMPORT_PACKAGE_ELEMENT_ID) references import_package_element(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 6-19
ALTER TABLE client_order_element ADD COLUMN PART_NUMBER VARCHAR(127) DEFAULT NULL;

ALTER TABLE client_weather_order_element ADD COLUMN PART_NUMBER VARCHAR(127) DEFAULT NULL;

-- 6-20
ALTER TABLE supplier_order_element ADD COLUMN ELEMENT_STATUS_ID INT(11) DEFAULT NULL;

-- 6-27
ALTER TABLE authority_relation ADD UNIQUE INDEX save_unique_index_client(USER_ID,CLIENT_ID);

ALTER TABLE authority_relation ADD UNIQUE INDEX save_unique_index_supplier(USER_ID,SUPPLIER_ID);

-- 7-3
create view v_client_order_element_import as 
SELECT
	`coe`.`ID` AS `ID`,
	sum(`ipe`.`AMOUNT`) AS AMOUNT
FROM
	(
		(
			(
				`client_order_element` `coe`
				LEFT JOIN `supplier_order_element` `soe` ON (
					(
						`soe`.`CLIENT_ORDER_ELEMENT_ID` = `coe`.`ID`
					)
				)
			)
			LEFT JOIN `supplier_import_element` `sie` ON (
				(
					`sie`.`SUPPLIER_ORDER_ELEMENT_ID` = `soe`.`ID`
				)
			)
		)
		LEFT JOIN `import_package_element` `ipe` ON (
			(
				`ipe`.`ID` = `sie`.`IMPORT_PACKAGE_ELEMENT_ID`
			)
		)
	)
GROUP BY
	`coe`.`ID` 
;

-- 7-4
ALTER TABLE exchange_rate ADD COLUMN RELATIVE_RATE DOUBLE(4,2) DEFAULT NULL;

-- 7-6
ALTER TABLE client_quote_element ADD COLUMN RELATIVE_RATE DOUBLE(4,2) DEFAULT NULL;

-- 7-12
ALTER TABLE supplier ADD COLUMN HAS_CAAC_ABILITY INT(11) DEFAULT NULL;


INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000133', 'HAS_CAAC_ABILITY', '1', '是', NULL, CURRENT_TIMESTAMP);

INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000134', 'HAS_CAAC_ABILITY', '2', '否', NULL, CURRENT_TIMESTAMP);


-- 7-16
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000136', 'COND', 'SV-OUTRIGHT', 'SV-OUTRIGHT', NULL, CURRENT_TIMESTAMP);

INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000137', 'COND', 'OH-OUTRIGHT', 'OH-OUTRIGHT', NULL, CURRENT_TIMESTAMP);

INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000138', 'COND', 'MRO-Overhaul', 'MRO-Overhaul', NULL, CURRENT_TIMESTAMP);

INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000139', 'COND', 'MRO-Repair', 'MRO-Repair', NULL, CURRENT_TIMESTAMP);

-- 7-17
ALTER TABLE supplier_commission_sale ADD COLUMN CRAWL_CLIENT_INQUIRY_ID INT(11) DEFAULT NULL;

ALTER TABLE supplier_commission_sale ADD COLUMN CRAWL_STATUS INT(2) DEFAULT 0;

ALTER TABLE supplier_commission_sale_element ADD COLUMN MOQ INT(8) DEFAULT 0;

-- 7-20
INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000141', 'COND', 'Tested/Inspected-Outright', 'Tested/Inspected-Outright', NULL, CURRENT_TIMESTAMP);

INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000142', 'COND', 'TESTED/INSPECTED-Flat Rate Exchange', 'TESTED/INSPECTED-Flat Rate Exchange', NULL, CURRENT_TIMESTAMP);

INSERT INTO `crm`.`system_code` (`ID`, `TYPE`, `CODE`, `VALUE`, `REMARK`, `UPDATE_TIMESTAMP`) VALUES ('1000143', 'COND', 'Tested/Inspected-exchange plus repair cost', 'Tested/Inspected-exchange plus repair cost', NULL, CURRENT_TIMESTAMP);


-- 7-23
ALTER TABLE supplier_quote_element ADD INDEX IDX_PART_NUMBER(PART_NUMBER);

-- 7-24
ALTER TABLE satair_quote_element ADD COLUMN STORAGE_AMOUNT DOUBLE(8,2) DEFAULT NULL;

-- 7-27
CREATE TABLE `stock_market_crawl_message` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PART_NUMBER` VARCHAR(127) NOT NULL,
	`AMOUNT` DOUBLE(8,2) NOT NULL,
	`CONDITION_VALUE` VARCHAR(127) NOT NULL,
	`SUPPLIER_CODE` VARCHAR(127) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE stock_market_crawl_message ADD COLUMN CRAWL_DATE date NOT NULL;

ALTER TABLE supplier_order_element ADD COLUMN UNIT VARCHAR(32) DEFAULT NULL;

-- 7-30
ALTER TABLE stock_market_crawl_message ADD COLUMN UPDATE_TIMESTAMP TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP; 


-- 7-30
CREATE TABLE `stock_market_supplier_map`(
	`ID` int(11) NOT NULL AUTO_INCREMENT,
	`SUPPLIER_ID` int(11) NOT NULL,
	`AIR_TYPE_ID` int(11) NOT NULL,
	`UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`CREATE_USER` int(11) NOT NULL,
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE supplier_commission_sale ADD COLUMN AIR_TYPE_ID INT(11) DEFAULT NULL;

CREATE TABLE `stock_market_add_supplier`(
	`ID` int(11) NOT NULL AUTO_INCREMENT,
	`SUPPLIER_COMMISSION_SALE_ELEMENT_ID` int(11) NOT NULL,
	`SUPPLIER_ID` int(11) NOT NULL,
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 7-31

CREATE TABLE `stock_market_crawl_register`(
	`ID` int(11) NOT NULL AUTO_INCREMENT,
	`COMPLETE` int(11) NOT NULL,
	`EXCEL_CONPLETE` int(11) NOT NULL,
	`UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE stock_market_crawl_message ADD COLUMN STOCK_MARKET_CRAWL_REGISTER_ID INT(11) NOT NULL;

ALTER TABLE stock_market_add_supplier ADD COLUMN UPDATE_TIMESTAMP TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP; 

-- 8-1
ALTER TABLE stock_market_crawl_message ADD COLUMN SUPPLIER_COMMISSION_SALE_ELEMENT_ID INT(11) NOT NULL;

ALTER TABLE supplier_commission_sale ADD COLUMN CRAWL_STORAGE_STATUS INT(11) NOT NULL DEFAULT 0;

-- 8-4
ALTER TABLE stock_market_crawl ADD COLUMN CRAWL_DATE date NOT NULL;

-- 8-6
CREATE TABLE `dasi`(
	`ID` int(11) NOT NULL AUTO_INCREMENT,
	`CLIENT_INQUIRY_ID` int(11) NOT NULL,
	`COMPLETE` int(11) NOT NULL,
	`SEND_STATUS` int(11) NOT NULL,
	`UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `dasi_message`(
	`ID` int(11) NOT NULL AUTO_INCREMENT,
	`DASI_ID` int(11) NOT NULL,
	`PART_NUMBER` VARCHAR(127) NOT NULL,
	`STORAGE_AMOUNT` VARCHAR(32) NOT NULL,
	`UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE dasi_message ADD COLUMN CLIENT_INQUIRY_ELEMENT_ID INT(11) NOT NULL;

ALTER TABLE send_back_message ADD COLUMN SERIAL_NUMBER VARCHAR(127) DEFAULT NULL;

-- 8-8
ALTER TABLE send_back_message ADD COLUMN LOCATION VARCHAR(127) DEFAULT NULL;

ALTER TABLE stock_market_crawl ADD COLUMN SUPPLIER_COMMISSION_SALE_ID INT (11) NOT NULL;

-- 8-9
ALTER TABLE aviall_quote_element ADD UNIT VARCHAR(32) DEFAULT NULL;

-- 8-15
CREATE TABLE `supplier_commission_for_stockmarket`(
	`ID` int(11) NOT NULL AUTO_INCREMENT,
	`CREATE_DATE` date NOT NULL,
	`NUMBER` VARCHAR(127) NOT NULL,
	`SUPPLIER_ID` INT(11) NOT NULL,
	`AIR_TYPE_ID` INT(11) NOT NULL,
	`REMARK` VARCHAR(127) DEFAULT NULL,
	`CLIENT_INQUIRY_ID` INT(11) DEFAULT NULL,
	`SALE_STATUS` INT(11) NOT NULL DEFAULT 0,
	`CRAWL_STATUS` INT(11) NOT NULL DEFAULT 0,
	`CRAWL_STORAGE_STATUS` INT(11) NOT NULL DEFAULT 0,
	`UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 8-16
CREATE TABLE `supplier_commission_for_stockmarket_element`(
	`ID` int(11) NOT NULL AUTO_INCREMENT,
	`SUPPLIER_COMMISSION_FOR_STOCKMARKET_ID` INT(11) NOT NULL,
	`PART_NUMBER` VARCHAR(127) NOT NULL,
	`DESCRIPTION` VARCHAR(255) DEFAULT NULL,
	`SERIAL_NUMBER` VARCHAR(127) DEFAULT NULL,
	`AMOUNT` DOUBLE(8,2) NOT NULL DEFAULT 0,
	`CONDITION_VALUE` VARCHAR(127) DEFAULT NULL,
	`DOM` VARCHAR(127) DEFAULT NULL,
	`MANUFACTURER` VARCHAR(127) DEFAULT NULL,
	`AR_PRICE` DOUBLE(8,2) DEFAULT NULL,
	`REMARK` VARCHAR(127) DEFAULT NULL,
	`UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 8-20
CREATE TABLE `ar_price_part_mapping`(
	`ID` int(11) NOT NULL AUTO_INCREMENT,
	`PART_NUMBER` VARCHAR(127) NOT NULL,
	`AR_PRICE` DOUBLE(8,2) DEFAULT NULL,
	`UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE ar_price_part_mapping ADD INDEX IDX_PART_NUMBER(PART_NUMBER);


ALTER TABLE ar_price_part_mapping ADD CONSTRAINT UQ_PART_NUMBER UNIQUE (PART_NUMBER);

ALTER TABLE ar_price_part_mapping ADD COLUMN CREATE_USER_ID INT(11) NOT NULL;

-- 8-21
ALTER TABLE supplier_commission_for_stockmarket_element ADD COLUMN ITEM INT(11) DEFAULT NULL;

ALTER TABLE supplier_commission_for_stockmarket_element ADD COLUMN ALT VARCHAR(255) DEFAULT NULL;

ALTER TABLE supplier_commission_for_stockmarket_element ADD COLUMN IS_REPLACE INT(11) DEFAULT 0;

-- 8-22
ALTER TABLE stock_market_crawl_message ADD COLUMN IS_COPY INT(2) NOT NULL DEFAULT 0;

CREATE TABLE ROLE_LEADER(
	`ID` INT(11) NOT NULL AUTO_INCREMENT,
	`ROLE_ID` INT(11) NOT NULL,
	`T_USER_ID` INT(11) NOT NULL,
	`UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`ID`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
-- 8-23
ALTER TABLE satair_quote_element ADD COLUMN CLIENT_INQUIRY_ELEMENT_ID INT(11) DEFAULT NULL;

CREATE TABLE send_back_flow_message(
	`ID` INT(11) NOT NULL AUTO_INCREMENT,
	`ORDER_APPROVAL_ID` INT(11) NOT NULL,
	`READ_STATUS` INT (2) NOT NULL DEFAULT 0,
	`UPDATE_TIMESTAMP` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE send_back_flow_message ADD COLUMN USER_ID INT(11) NOT NULL;

ALTER TABLE send_back_flow_message ADD COLUMN DESCRIPTION VARCHAR(255) NOT NULL;

-- 8-24
ALTER TABLE jbpm4_hist_procinst ADD INDEX INDEX_ID(id_);

ALTER TABLE supplier_commission_sale_element ADD INDEX IDX_CONDITION_ID (condition_id);

-- 8-27
ALTER TABLE supplier_quote_element ADD INDEX IDX_CONDITION_ID(CONDITION_ID);

ALTER TABLE supplier_quote_element ADD INDEX IDX_PRICE(PRICE);

-- 8-29
ALTER TABLE stock_market_crawl_message ADD INDEX IDX_STOCK_MARKET_ID(stock_market_crawl_id);

ALTER TABLE supplier_commission_for_stockmarket_element ADD INDEX IDX_SUPPLIER_COMMISSION_FOR_STOCKMARKET(SUPPLIER_COMMISSION_FOR_STOCKMARKET_ID);

ALTER TABLE supplier_commission_sale ADD INDEX IDX_SALE_STATUS (SALE_STATUS);

ALTER TABLE supplier_commission_sale_element ADD INDEX IDX_SUPPLIER_COMMISSION_SALE_ID (SUPPLIER_COMMISSION_SALE_ID);

-- 9-14
ALTER TABLE client_order_element_final ADD COLUMN ORDER_NUMBER_INDEX INT(2) DEFAULT NULL;

ALTER TABLE client_order_element_final ADD INDEX IDX_CLIENT_ORDER_ELEMENT_ID(CLIENT_ORDER_ELEMENT_ID);

ALTER TABLE client_order_element_final ADD INDEX IDX_CERTIFICATION_ID(CERTIFICATION_ID);

ALTER TABLE order_approval ADD INDEX IDX_SUPPLIER_WEATHER_ORDER_ELEMENT_ID(SUPPLIER_WEATHER_ORDER_ELEMENT_ID);

ALTER TABLE order_approval ADD INDEX IDX_CLIENT_ORDER_ELEMENT_ID(CLIENT_ORDER_ELEMENT_ID);

/**
@Author: Modify by white
@DateTime: 2018/10/11 10:05
@Description: 为数据表添加属性 TSN,CSN，ATA
*/
ALTER TABLE supplier_commission_for_stockmarket_element ADD  COLUMN TSN VARCHAR(255) DEFAULT  NULL;

ALTER TABLE supplier_commission_for_stockmarket_element ADD  COLUMN CSN VARCHAR(255) DEFAULT  NULL;

ALTER TABLE supplier_commission_for_stockmarket_element ADD  COLUMN ATA VARCHAR(255) DEFAULT  NULL;

/**
@Author: Modify by white
@DateTime: 2018/10/12 11:11
@Description: 数据表添加字段 DOM  DESCRITION AR_SALE_PRICE
*/
ALTER TABLE ar_price_part_mapping ADD  COLUMN DOM VARCHAR(255) DEFAULT  NULL;

ALTER TABLE ar_price_part_mapping ADD  COLUMN DESCRITION VARCHAR(255) DEFAULT  NULL;

ALTER TABLE ar_price_part_mapping ADD  COLUMN AR_SALE_PRICE DOUBLE (10) DEFAULT  NULL;

/**
@Author: Modify by white
@DateTime: 2018/10/15 10:13
@Description: 为数据表supplier_commission_for_stockmarket_element添加字段AR_SALE_PRCIE
*/
ALTER TABLE supplier_commission_for_stockmarket_element ADD  COLUMN AR_SALE_PRICE DOUBLE(10) DEFAULT  NULL;

/**
@Author: Modify by white
@DateTime: 2018/10/17 14:35
@Description: 为数据表supplier_commission_sale_element添加字段机型
*/
ALTER TABLE supplier_commission_sale_element ADD  COLUMN AIR_TYPE VARCHAR (255) DEFAULT  NULL;

/**
@Author: Modify by white
@DateTime: 2018/10/24 17:20
@Description: 为数据表supplier_commission_for_stockmarket_element添加字段packagePrice
*/
ALTER TABLE supplier_commission_for_stockmarket_element ADD  COLUMN PACKAGE_PRICE VARCHAR(255) DEFAULT  NULL;

/**
@Author: Modify by white
@DateTime: 2018/10/30 12:55
@Description: 为role_leader插入数据角色为7 用户id为29
*/
INSERT INTO  role_leader(ROLE_ID,T_USER_ID) VALUE (7,29)

/**
@Author: Modify by white
@DateTime: 2018/11/6 17:33
@Description: 修改ar_price_part_mapping中索引IDX_PART_NUMBER为Normal
              修改字段arprice为varchar
              修改字段arSalePrice为varchar
              修改表supplier_commission_for_stockmarket_element中的字段AR_PRICE为varchar
*/
ALTER table ar_price_part_mapping DROP INDEX IDX_PART_NUMBER
ALTER table ar_price_part_mapping ADD  INDEX IDX_PART_NUMBER(PART_NUMBER)

ALTER table ar_price_part_mapping modify COLUMN arprice VARCHAR(255)

ALTER table ar_price_part_mapping modify COLUMN arSalePrice VARCHAR(255)

ALTER table supplier_commission_for_stockmarket_element COLUMN AR_PRICE VARCHAR(255)

/**
@Author: Modify by white
@DateTime: 2018/11/19 11:10
@Description:
删除国外采购 资产包 和StockMarket权限
添加国外采购-01 角色(11)并赋予资产包(30105)和StockMarket(10222)权限 同时分配给Kris(17)和Saidy(26)
*/
DELETE FROM R_ROLE_MENU
WHERE MENU_ID IN (10222,30105)
AND ROLE_ID = 7;

INSERT INTO T_ROLE VALUE (11,"国外采购-01","资产包和StockMarket特殊权限");

INSERT INTO R_ROLE_USER(USER_ID,ROLE_ID) VALUES (17,11),(26,11);

INSERT INTO R_ROLE_MENU(MENU_ID,ROLE_ID) VALUES (30105,11),(10222,11);

/**
@Author: Modify by white
@DateTime: 2018/11/28 22:23
@Description: 新建一个表email_record用于记录邮件发送
*/
CREATE TABLE email_record(
	`ID` INT(11) NOT NULL AUTO_INCREMENT,
	`SENDER` VARCHAR (255) ,
	`RECEIVER` VARCHAR (255),
	`TYPE` VARCHAR (255),
	`CLIENT_INQUIRY_ID` INT (11),
	`CREATE_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;









