package com.naswork.utils.excel.poi;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;

public class POIExcelWorkBook {
	private XSSFWorkbook wb2007 = null;
	private HSSFWorkbook wb2003 = null;
	public POIExcelWorkBook(XSSFWorkbook wb){
		this.wb2007 = wb;
		this.wb2003 = null;
	}
	public POIExcelWorkBook(HSSFWorkbook wb){
		this.wb2003 = wb;
		this.wb2007 = null;
	}
	
	public Sheet getSheetAt(int index){
		if(this.wb2003!=null){
			return this.wb2003.getSheetAt(index);
		}else if(this.wb2007!=null){
			return this.wb2007.getSheetAt(index);
		}else{
			return null;
		}
	}
	
	public int getActiveSheetIndex(){
		if(this.wb2003!=null){
			return this.wb2003.getActiveSheetIndex();
		}else if(this.wb2007!=null){
			return this.wb2007.getActiveSheetIndex();
		}else{
			return -1;
		}
	}
	
	public void write(OutputStream stream) throws IOException{
		if(this.wb2003!=null){
			this.wb2003.write(stream);
		}else if(this.wb2007!=null){
			this.wb2007.write(stream);
		}
	}
	public void removeSheetAt(int index){
		if(this.wb2003!=null){
			 this.wb2003.removeSheetAt(index);
		}else if(this.wb2007!=null){
			 this.wb2007.removeSheetAt(index);
		}
	}
	
	public void setActiveSheet(int arg0){
		
		if(this.wb2003!=null){
			this.wb2003.setActiveSheet(arg0);
		}else if(this.wb2007!=null){
			this.wb2007.setActiveSheet(arg0);
		}
	}
	
    public void setSheetHidden(int arg0, boolean arg1){
		
		if(this.wb2003!=null){
			this.wb2003.setSheetHidden(arg0, arg1);
		}else if(this.wb2007!=null){
			this.wb2007.setSheetHidden(arg0, arg1);
		}
	}
    
    public CellStyle createCellStyle(){
		
 		if(this.wb2003!=null){
 			return	this.wb2003.createCellStyle();
 		}else if(this.wb2007!=null){
 			return	this.wb2007.createCellStyle();
 		}
		return null;
 	}
    
    public Font createFont(){
		
 		if(this.wb2003!=null){
 			return	this.wb2003.createFont();
 		}else if(this.wb2007!=null){
 			return	this.wb2007.createFont();
 		}
		return null;
 	}
    public void setPrintArea(int arg0, String arg1){
    	if(this.wb2003!=null){
			this.wb2003.setPrintArea(arg0, arg1);
		}else if(this.wb2007!=null){
			this.wb2007.setPrintArea(arg0, arg1);
		}
    } 
    
    public DataFormat createDataFormat(){
    	if(this.wb2003!=null){
    		return	this.wb2003.createDataFormat();
		}else if(this.wb2007!=null){
			return	this.wb2007.createDataFormat();
		}
		return null;
    }
}
