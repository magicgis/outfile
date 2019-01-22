package com.naswork.codegenerator;

import java.util.List;
import java.util.Map;

/**
 * 运行生成的main
 * @author xiaohu
 *
 */
public class DoMain {

	public static void main(String[] args) {
		try {
			CodeGenerator creater = new CodeGenerator();
			creater.IGNORE_TABLE_PREFIX.add("");  				//设置忽略项
			creater.ROOT_PACKAGE 	= "com.naswork"; 					//设置包路径
			creater.AUTHOR			= "auto";	//设置作者
			creater.FTL_PATH		= "com/naswork/codegenerator/ftl";//FTL模板路径
			creater.FILE_PATH		= "outcode/com/naswork/";			//生成文件路径
			
			
			List<String> tables = creater.getTables("ask4leave");
			for (String table : tables) {
				Map<String, Object> root = creater.sealData(table);		//通过表名获取封装数据
				creater.createEntityClass(root);						//创建实体
				creater.createMapperClass(root);						//创建Mapper
				creater.createDaoClass(root);							//创建Dao
				creater.createServiceClass(root);						//创建Service
				creater.createServiceImplClass(root);					//创建ServiceImpl
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
