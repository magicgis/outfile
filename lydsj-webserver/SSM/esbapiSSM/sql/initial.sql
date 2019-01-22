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

insert into t_user (user_id, user_name, password, login_name) values (1,'testuser','e10adc3949ba59abbe56e057f20f883e','t1');
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

ALTER TABLE t_user add COLUMN email_password varchar(45) NOT NULL;

CREATE TABLE test_jbpm(
	id int(11) NOT NULL AUTO_INCREMENT,
	message VARCHAR(255) NULL,
	remark  VARCHAR(255) NULL,
	SPZT INT(11),
	PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 2018-09-01

