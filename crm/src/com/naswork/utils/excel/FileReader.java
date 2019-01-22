package com.naswork.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class FileReader {

	private Long rows; // 记录个数

	private String value; // 字段

	public void FileRead() {

	}
	
	public List<Map<String, String>> readXls(InputStream file)
			throws FileNotFoundException, IOException,BiffException {
		
		Map<String, String> map = null;
		
		/***************读取Excel文件****************/
		Workbook workbook = Workbook.getWorkbook(file);
		
		/***************读取Excel表*****************/
		Sheet sheet = workbook.getSheet(0);
		
		/***************读取表头内容*****************/
		Cell[] head = sheet.getRow(0);
		
		/********去掉表头，取得总的数据量************/
		long rows = sheet.getRows() - 1;
		this.setRows(new Long(rows)); 

		List<Map<String, String>> all = new ArrayList<Map<String, String>>(this.getRows().intValue());
		for (int r = 1; r <= rows; r++) {
			/*******读取当前行**********/
			Cell[] cells = sheet.getRow(r);
			map = new LinkedHashMap<String, String>(cells.length);
			if (cells != null) {
				for (short c = 0; c < cells.length; c++) {
					/********读取当前单元格*********/
					Cell cel = cells[c];
					value="";
					if (cel != null) {
						/*****读取当前单元格的值*****/
						value = cel.getContents();
					}else{
						value= "";
					}
					if (value.trim().equals("null")) {
						value = " ";
					}
					/***value存储到类集***/
					map.put(head[c].getContents().trim(), value);
				}
				all.add(map);
			}
		}
		
		workbook.close();
		return all;
	}
	
	
	public List<Map<String, String>> readXls(File file)
			throws FileNotFoundException, IOException,BiffException {
		
		Map<String, String> map = null;
		
		/***************读取Excel文件****************/
		Workbook workbook = Workbook.getWorkbook(new FileInputStream(file));
		
		/***************读取Excel表*****************/
		Sheet sheet = workbook.getSheet(0);
		
		/***************读取表头内容*****************/
		Cell[] head = sheet.getRow(0);
		
		/********去掉表头，取得总的数据量************/
		long rows = sheet.getRows() - 1;
		this.setRows(new Long(rows)); 

		List<Map<String, String>> all = new ArrayList<Map<String, String>>(this.getRows().intValue());
		for (int r = 1; r <= rows; r++) {
			/*******读取当前行**********/
			Cell[] cells = sheet.getRow(r);
			map = new LinkedHashMap<String, String>(cells.length);
			if (cells != null) {
				for (short c = 0; c < cells.length; c++) {
					/********读取当前单元格*********/
					Cell cel = cells[c];
					value="";
					if (cel != null) {
						/*****读取当前单元格的值*****/
						value = cel.getContents();
					}else{
						value= "";
					}
					if (value.trim().equals("null")) {
						value = " ";
					}
					/***value存储到类集***/
					map.put(head[c].getContents().trim(), value);
				}
				all.add(map);
			}
		}
		
		workbook.close();
		return all;
	}

	public Long getRows() {
		return rows;
	}

	public void setRows(Long rows) {
		this.rows = rows;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
