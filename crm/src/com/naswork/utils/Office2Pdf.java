package com.naswork.utils;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;

import com.jacob.activeX.ActiveXComponent;  
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;  
import com.jacob.com.Variant;

/**
 * office转换成pdf
 * @author Tanoy
 *
 */
public class Office2Pdf {
	
	private static final int wdFormatPDF = 17;
    private static final int xlTypePDF = 0;
    private static final int ppSaveAsPDF = 32;
	
	/**
	 * excel转pdf
	 * @param els 输入文件路径
	 * 
	 */
	public String excel2pdf(String els){
		synchronized (this) {
			System.out.println("Starting excel...");    
	        long start = System.currentTimeMillis(); 
	        ActiveXComponent app = new ActiveXComponent("Excel.Application"); 
	        /*if (els.toLowerCase().indexOf(".doc") > 0 || els.toLowerCase().indexOf(".docx") > 0) {
	        	app = new ActiveXComponent("Word.Application"); 
			}*/
	        //输出文件路径
	        if (els.toLowerCase().indexOf(".pdf") > 0) {
				return els;
			}
	        String target = els.replace(".xlsx", ".pdf");
	    	target = target.replace(".xls", ".pdf");
	    	target = target.replace(".docx", ".pdf");
	    	target = target.replace(".doc", ".pdf");
	        //校验文件是否存在,存在就删除
	        try {
	        	//String fileName = "D:\\CRM\\Files\\mis\\excel\\sampleoutput\\ORD-12002902_SupplierOrder_testuser_20180331_111020_1.pdf";
	    		File file = new File(target);
	            if (!file.exists()) {
	            	try {    
	            		System.out.print("开始转换PDF");
	                    app.setProperty("Visible",false);    
	                    Dispatch workbooks = app.getProperty("Workbooks").toDispatch();    
	                    System.out.println("opening document:" + els);    
	                    Dispatch workbook = Dispatch.invoke(workbooks, "Open", Dispatch.Method, new Object[]{els, new Variant(false),new Variant(false)}, new int[3]).toDispatch();    
	                    Dispatch.invoke(workbook, "SaveAs", Dispatch.Method, new Object[] {    
	                    target, new Variant(57), new Variant(false),    
	                    new Variant(57), new Variant(57), new Variant(false),    
	                    new Variant(true), new Variant(57), new Variant(true),    
	                    new Variant(true), new Variant(true) }, new int[1]);    
	                    Variant f = new Variant(false);    
	                    System.out.println("to pdf " + target);    
	                    Dispatch.call(workbook, "Close", f);    
	                    long end = System.currentTimeMillis();    
	                    System.out.println("completed..used:" + (end - start)/1000 + " s");    
	                    System.out.print("转换PDF成功");
	                    if (app != null){    
	    	                app.invoke("Quit", new Variant[] {});    
	    	            }
	                    ComThread.Release();
	                    return target;
	                } catch (Exception e) {  
	                	if (app != null){    
	    	                app.invoke("Quit", new Variant[] {});    
	    	            }
	                	ComThread.Release();
	                	e.printStackTrace();
	                    System.out.println("========Error:Operation fail:" + e.getMessage());    
	                    return null;
	                }/*finally {   
	                    if (app != null){    
	                        app.invoke("Quit", new Variant[] {});    
	                    }    
	                    //释放占用的内存空间，因为com的线程回收不由java的垃圾回收器处理
	        			//ComThread.Release();
	                }*/
	            } else {
	            	if (app != null){    
		                app.invoke("Quit", new Variant[] {});    
		            }
	            	ComThread.Release();
	                return target;
	            }
			} catch (Exception e) {
				if (app != null){    
	                app.invoke("Quit", new Variant[] {});    
	            }
				ComThread.Release();
				e.printStackTrace();
				return null;
			}/*finally {   
	            if (app != null){    
	                app.invoke("Quit", new Variant[] {});    
	            }    
	            //释放占用的内存空间，因为com的线程回收不由java的垃圾回收器处理
				//ComThread.Release();
	        }*/
		}
    }  
	
	/**
	 * work转pfd
	 * @param inputFile
	 * @return
	 */
	public String word2Pdf(String inputFile){
		synchronized (this) {
			String target = inputFile.replace(".xls", ".pdf");
	    	target = target.replace(".xlsx", ".pdf");
	    	target = target.replace(".docx", ".pdf");
	    	target = target.replace(".doc", ".pdf");
			File file = new File(target);
			// 打开Word应用程序
	        ActiveXComponent app = new ActiveXComponent("Word.Application");
			try {
	            if (!file.exists()) {
			        System.out.println("开始转化Word为PDF...");
			        long date = new Date().getTime();
			        // 设置Word不可见
			        app.setProperty("Visible", new Variant(false));
			        // 禁用宏
			        app.setProperty("AutomationSecurity", new Variant(3));
			        // 获得Word中所有打开的文档，返回documents对象
			        Dispatch docs = app.getProperty("Documents").toDispatch();
			        // 调用Documents对象中Open方法打开文档，并返回打开的文档对象Document
			        Dispatch doc = Dispatch.call(docs, "Open", inputFile, false, true).toDispatch();
			        /***
			         * 
			         * 调用Document对象的SaveAs方法，将文档保存为pdf格式
			         * 
			         * Dispatch.call(doc, "SaveAs", pdfFile, wdFormatPDF)
			         * word保存为pdf格式宏，值为17 
			         * 为第一种方法，测试知道不可用
			         * 
			         */
			        Dispatch.call(doc, "ExportAsFixedFormat", target, wdFormatPDF);// word保存为pdf格式宏，值为17
			        System.out.println(doc);
			        // 关闭文档
			        long date2 = new Date().getTime();
			        int time = (int) ((date2 - date) / 1000);
		
			        Dispatch.call(doc, "Close", false);
			        // 关闭Word应用程序
			        app.invoke("Quit", 0);
			        ComThread.Release();
			        return target;
	            } else {
	            	ComThread.Release();
	                return target;
	            }
			} catch (Exception e) {
				ComThread.Release();
				e.printStackTrace();
				return null;
			}/*finally{
				if (app != null){    
	                app.invoke("Quit", new Variant[] {});    
	            }
				//释放占用的内存空间，因为com的线程回收不由java的垃圾回收器处理
				//ComThread.Release();
			}*/
		}
	}
	
}
