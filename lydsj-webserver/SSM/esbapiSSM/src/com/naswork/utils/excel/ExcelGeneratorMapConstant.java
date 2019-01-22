package com.naswork.utils.excel;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component("excelGeneratorMapConstant")
@Lazy(false)
public class ExcelGeneratorMapConstant {
	
	@Resource
	private SampleExcelGenerator sampleExcelGenerator;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public static Map<String,ExcelGeneratorBase> EXCEL_GENERATOR_MAP = new HashMap<String,ExcelGeneratorBase>();
	public void init(){
		logger.info("开始初始化 Excel生成器...");
		initData();
		logger.info("初始化Excel生成器完成");
	}
	
	private void initData() {
		EXCEL_GENERATOR_MAP.clear();
		EXCEL_GENERATOR_MAP.put(sampleExcelGenerator.fetchMappingKey(), sampleExcelGenerator);
	}
		
}
