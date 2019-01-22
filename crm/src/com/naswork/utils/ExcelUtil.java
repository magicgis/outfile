package com.naswork.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.naswork.model.ClientInquiryElement;

import jxl.Sheet;
import jxl.Workbook;

public class ExcelUtil {

	
	public static boolean excel2PhoneList(InputStream inputStream) throws Exception {
	     Map map = new HashMap();
	     Workbook workbook = Workbook.getWorkbook(inputStream);  //处理输入流
	     Sheet sheet = workbook.getSheet(0);// 获取第一个sheet
	     int rows = sheet.getRows();   //获取总行号
	     String[][] curArr = new String[rows][2];     //存放正确心细
	     String[][] errorArr = new String[rows * 2][4];   //存放错误信息
	     int curLines = 0;
	     int errorLines = 0;
	     String error="";
	     Integer flag = 1;
	     ClientInquiryElement clientInquiryElement=new ClientInquiryElement();
	     List<ClientInquiryElement> list=new ArrayList<ClientInquiryElement>();
	  for (int i = 1; i < rows; i++) {// 遍历行获得每行信息
		  
		     Integer item = new Integer(sheet.getCell(0, i).getContents());// 获得第i行第1列信息
		     String partNumber = sheet.getCell(1, i).getContents();
		     String description = sheet.getCell(2, i).getContents();
		     String unit = sheet.getCell(3, i).getContents();
		     Double amount = new Double(sheet.getCell(4, i).getContents());
		     String remark = sheet.getCell(5, i).getContents();
		     clientInquiryElement.setItem(item);
		     clientInquiryElement.setPartNumber(partNumber);
		     clientInquiryElement.setDescription(description);
		     clientInquiryElement.setUnit(unit);
		     clientInquiryElement.setAmount(amount);
		     clientInquiryElement.setRemark(remark);
		     
		     list.add(clientInquiryElement);
		     if (flag!=item) {
		    	 error=String.valueOf(i);
					break;
			}
		     
		     StringBuffer errorMsg = new StringBuffer();
	     i++;
	    /* if (!isRowEmpty(sheet, i)) { //此行不为空
	        //对此行信息进行正误判断           
	     }*/
	  }// 行
	  if ("".equals(error)) {
		return false;
	  }else {
		return true;
	}
	  
	  //正误信息存入map，保存
	 /* map.put("current", current);
	  map.put("error", error);*/
	  /* return map;*/
	 }
	
	 private static boolean isRowEmpty(Sheet sheet, int i) {
		    String phone = sheet.getCell(0, i).getContents();// 集团编号
		    String shortNum = sheet.getCell(1, i).getContents();// 集团名称
		    if (phone.isEmpty() && shortNum.isEmpty())
		        return true;
		    return false;
		   }
}
