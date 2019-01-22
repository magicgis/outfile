package com.naswork.utils;

import java.beans.PropertyDescriptor;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 导出word文档帮助类
 * 
 * @author Administrator
 * 
 */
public class ReadWriteAndDownloadDoc {

	// 导出为word文档
	public static void file2Word(String wordName, Object object,
			HttpServletResponse response, HttpServletRequest request) {
		ReadWriteAndDownloadDoc.file2WordAndList(wordName, object, null, response, request);
	}
	
	// 导出为word文档
	@SuppressWarnings("unchecked")
	public static void file2WordAndList(String wordName, Object object, List wordExportList, 
			HttpServletResponse response, HttpServletRequest request) {
		// 定义需要用到的IO流
		ByteArrayOutputStream os = null;
		Writer writer = null;
		ServletOutputStream servletOS = null;
		try {
			// 用Map封装要替换的内容 key值为word文档的要替换内容的标示，value值为要替换的内容
			Map map = new HashMap();
			Configuration configuration = new Configuration();
			configuration.setDefaultEncoding("UTF-8");
			// word模板文件路径
			String modelPath = request.getSession().getServletContext()
					.getRealPath("WEB-INF/classes/config/ftl");
			if(null != object) {
				if (object instanceof Map) {
					map.putAll((Map<String, Object>) object); 
				} else {
					map = beanToMap(object,null);
				}
				// 通过反射加载类
				/*Class cls = Class.forName(object.getClass().getName());
				Field fields[] = cls.getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					String name = fields[i].getName();// 该类的所有变量名称
					if (!"serialVersionUID".equals(name)) {
						PropertyDescriptor pd = new PropertyDescriptor(name, cls);
						Method getMethod = pd.getReadMethod();// 获得get方法
						Object obj = getMethod.invoke(object, null);// 执行get方法返回一个Object
						// 不包括Date类型
						if (obj != null && (fields[i].getType().toString().indexOf("model") != -1 || fields[i].getType().toString().indexOf("vo") != -1) ) {//-- add by Giam 20160514 根据该系统包名判断是实体类，则直接再次循环
							map.putAll(beanToMap(obj));
						}else if (obj != null
								&& fields[i].getType().toString().indexOf("Date") == -1) {
							map.put(name, obj.toString());
	
						} else if (fields[i].getType().toString().indexOf("Date") == -1) {
							if(!map.containsKey(name)) map.put(name, "");
						}
						// Date类型处理
						if (obj != null
								&& fields[i].getType().toString().indexOf("Date") != -1) {
							Date date = (Date) obj;
							map.put(name + "_Y", (date.getYear() + 1900) + "");
							map.put(name + "_M", (date.getMonth() + 1) + "");
							map.put(name + "_D", (date.getDate()) + "");
							map.put(name + "_H", (date.getHours() + ""));
							map.put(name + "_mm", (date.getMinutes()) + "");
							map.put(name + "_CN", formatStr(date));
						} else if (fields[i].getType().toString().indexOf("Date") != -1) {
							map.put(name + "_Y", "");
							map.put(name + "_M", "");
							map.put(name + "_D", "");
							map.put(name + "_H", "");
							map.put(name + "_mm", "");
							map.put(name + "_CN", "");
						}
					}
				}*/
			}
			// 读取word模板文件
			configuration.setDirectoryForTemplateLoading(new File(modelPath));
			// wordName为要装载的模板
			Template template = configuration.getTemplate(wordName + ".ftl");
			// 设置编码
			template.setEncoding("UTF-8");
			os = new ByteArrayOutputStream();
			writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			
			// 输出到内存，并替换内容
			if (null != wordExportList) {
				map.put("wordExportList", wordExportList);
			}
			template.process(map, writer);
			
			// 输出word内容文件流，提供下载
			response.reset();
			response.setContentType("application/x-msdownload");
			// 文件名
			String fileName = wordName + ".xsl";
			String name = "attachment; filename=\"" + fileName + "\"";
			response.addHeader("Content-Disposition",
					new String(name.getBytes("gb2312"), "iso-8859-1"));
			servletOS = response.getOutputStream();
			servletOS.write(os.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {// 释放资源,关闭io流
				if (servletOS != null) {
					servletOS.flush();
					servletOS.close();
				}
				if (writer != null) {
					writer.flush();
					writer.close();
				}
				if (os != null) {
					os.flush();
					os.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * 打印多个word 打包成ZIP
	 * @param ftlName
	 * @param vo
	 * @param response
	 * @param request
	 * @since 2016年5月28日 下午6:16:00
	 * @author xiaohu
	 * @version v1.0
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void file2WordZip(String[] ftlName,Object[] vo,HttpServletResponse response, HttpServletRequest request) throws Exception{
		ZipOutputStream zos;
		response.setContentType("application/zip");
		String fileName = "文书打包.zip";
		response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"),"ISO-8859-1")); 
		if(ftlName.length >0 && vo.length >0){
			zos = new ZipOutputStream(response.getOutputStream());
			
			Configuration configuration = new Configuration();
			Writer writer = new OutputStreamWriter(zos, "UTF-8");
			for(int i = 0;i<ftlName.length;i++){
				String subFileName = ftlName[i]+ ".doc";
				
				// 用Map封装要替换的内容 key值为word文档的要替换内容的标示，value值为要替换的内容
				Map map = new HashMap();
				Object object = vo[i];
				
				configuration.setDefaultEncoding("UTF-8");
				// word模板文件路径
				String modelPath = request.getSession().getServletContext()
						.getRealPath("WEB-INF/classes/config/ftl");
				if(null != object) {
					if (object instanceof Map) {
						map.putAll((Map<String, Object>) object); 
					} else {
						map = beanToMap(object,null);
					}
				}
				
				zos.putNextEntry(new ZipEntry(new String(subFileName)));
				
				// 读取word模板文件
				configuration.setDirectoryForTemplateLoading(new File(modelPath));
				// wordName为要装载的模板
				Template template = configuration.getTemplate(ftlName[i] + ".ftl");
				// 设置编码
				template.setEncoding("UTF-8");
				template.process(map, writer);
								
				zos.setEncoding("UTF-8");
				zos.closeEntry();

			}
			writer.flush();
			writer.close();
            zos.flush();
			zos.close();
		} 
	}
	
	// 导出为word文档new
	public static void file2WordNew(String ftlName,String wordName, Object object,
			HttpServletResponse response, HttpServletRequest request) {
		ReadWriteAndDownloadDoc.file2WordAndListNew(ftlName,wordName, object, null, response, request);
	}
	
	@SuppressWarnings("unchecked")
	public static void file2WordAndListNew(String ftlName,String wordName, Object object, List wordExportList, 
			HttpServletResponse response, HttpServletRequest request) {
		// 定义需要用到的IO流
		ByteArrayOutputStream os = null;
		Writer writer = null;
		ServletOutputStream servletOS = null;
		
		try {
			// 用Map封装要替换的内容 key值为word文档的要替换内容的标示，value值为要替换的内容
			Map map = new HashMap();
			Configuration configuration = new Configuration();
			configuration.setDefaultEncoding("UTF-8");
			// word模板文件路径
			String modelPath = request.getSession().getServletContext()
					.getRealPath("nasmis/model");
			// 通过反射加载类
			Class cls = Class.forName(object.getClass().getName());
			Field fields[] = cls.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				String name = fields[i].getName();// 该类的所有变量名称
				PropertyDescriptor pd = new PropertyDescriptor(name, cls);
				Method getMethod = pd.getReadMethod();// 获得get方法
				Object obj = getMethod.invoke(object, null);// 执行get方法返回一个Object
				// 不包括Date类型
				if (obj != null && (fields[i].getType().toString().indexOf("model") != -1 || fields[i].getType().toString().indexOf("vo") != -1) ) {//-- add by Giam 20160514 根据该系统包名判断是实体类，则直接再次循环
					map.putAll(beanToMap(obj,null));
				}else if (obj != null && fields[i].getType().toString().indexOf("Date") == -1) {
					map.put(name, obj.toString());

				} else if (fields[i].getType().toString().indexOf("Date") == -1) {
					if(!map.containsKey(name)) map.put(name, "");
				}
				// Date类型处理
				if (obj != null
						&& fields[i].getType().toString().indexOf("Date") != -1) {
					Date date = (Date) obj;
					map.put(name + "_Y", (date.getYear() + 1900) + "");
					map.put(name + "_M", (date.getMonth() + 1) + "");
					map.put(name + "_D", (date.getDate()) + "");
					map.put(name + "_CN", formatStr(date));
				} else if (fields[i].getType().toString().indexOf("Date") != -1) {
					map.put(name + "_Y", "");
					map.put(name + "_M", "");
					map.put(name + "_D", "");
					map.put(name + "_CN","");
				}
			}
			// 读取word模板文件
			configuration.setDirectoryForTemplateLoading(new File(modelPath));
			// wordName为要装载的模板
			Template template = configuration.getTemplate(ftlName + ".ftl");
			// 设置编码
			template.setEncoding("UTF-8");
			os = new ByteArrayOutputStream();
			
			// 输出到内存，并替换内容
			if (null != wordExportList) {
				map.put("wordExportList", wordExportList);
			}
			File file=null;
			String path = request.getSession().getServletContext().getRealPath("nasmis/jc/dbajgdDown");
			if(null != wordName && "" != wordName){
				file=new File(path+"/"+wordName+".doc");
			}else{
				file=new File(path+"/"+ftlName+".doc");
			}
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			template.process(map, writer);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {// 释放资源,关闭io流
				if (servletOS != null) {
					servletOS.flush();
					servletOS.close();
				}
				if (writer != null) {
					writer.flush();
					writer.close();
				}
				if (os != null) {
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/******************************************日期转换为中文汉字*********************************************/
	/**
	  * create date:2010-5-22下午04:29:37
	  * 描述：将日期转换为指定格式字符串
	  * @param date  日期
	  * @return
	  */
	  public static String getDateStr(Date date)
	  {
	  SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
	  String datestr = sdf.format(date);
	  return datestr;
	  }
	  /**
	  * create date:2010-5-22下午03:40:44
	  * 描述：取出日期字符串中的年份字符串
	  * @param str 日期字符串
	  * @return
	  */
	  public static String getYearStr(String str)
	  {
	  String yearStr = "";
	  yearStr = str.substring(0,4);
	  return yearStr;
	  }
	   
	  /**
	  * create date:2010-5-22下午03:40:47
	  * 描述：取出日期字符串中的月份字符串
	  * @param str日期字符串
	  * @return
	  */
	  public static String getMonthStr(String str)
	  {
	  String monthStr;
	  int startIndex = str.indexOf("年");
	  int endIndex = str.indexOf("月");
	  monthStr = str.substring(startIndex+1,endIndex);
	  return monthStr;
	  }
	 
	    /**
	      * create date:2010-5-22下午03:32:31
	      * 描述：将源字符串中的阿拉伯数字格式化为汉字
	      * @param sign 源字符串中的字符
	      * @return
	      */
	    public static char formatDigit(char sign){
	        if(sign == '0')
	            sign = '〇';
	        if(sign == '1')
	            sign = '一';
	        if(sign == '2')
	            sign = '二';
	        if(sign == '3')
	            sign = '三';
	        if(sign == '4')
	            sign = '四';
	        if(sign == '5')
	            sign = '五';
	        if(sign == '6')
	            sign = '六';
	        if(sign == '7')
	            sign = '七';
	        if(sign == '8')
	            sign = '八';
	        if(sign == '9')
	            sign = '九';
	        return sign;
	    }
	   
	    /**
	      * create date:2010-5-22下午03:31:51
	      * 描述： 获得月份字符串的长度
	      * @param str  待转换的源字符串
	      * @param pos1 第一个'-'的位置
	      * @param pos2 第二个'-'的位置
	      * @return
	      */
	    public static int getMidLen(String str,int pos1,int pos2){
	        return str.substring(pos1+1, pos2).length();
	    }
	    /**
	      * create date:2010-5-22下午03:32:17
	      * 描述：获得日期字符串的长度
	      * @param str  待转换的源字符串
	      * @param pos2 第二个'-'的位置
	      * @return
	      */
	    public static int getLastLen(String str,int pos2){
	        return str.substring(pos2+1).length();
	    }
	   
	  /**
	  * create date:2010-5-22下午03:40:50
	  * 描述：取出日期字符串中的日字符串
	  * @param str 日期字符串
	  * @return
	  */
	  public static String getDayStr(String str)
	  {
	  String dayStr = "";
	  int startIndex = str.indexOf("月");
	  int endIndex = str.indexOf("日");
	  dayStr = str.substring(startIndex+1,endIndex);
	  return dayStr;
	  }
	    /**
	      * create date:2010-5-22下午03:32:46
	      * 描述：格式化日期
	      * @param str 源字符串中的字符
	      * @return
	      */
	    public static String formatStr(Date date){
	        StringBuffer sb = new StringBuffer();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        String str = sdf.format(date);
	        int pos1 = str.indexOf("-");
	        int pos2 = str.lastIndexOf("-");
	        for(int i = 0; i < 4; i++){
	            sb.append(formatDigit(str.charAt(i)));
	        }
	        sb.append('年');
	        if(getMidLen(str,pos1,pos2) == 1){
	            sb.append(formatDigit(str.charAt(5))+"月");
	            if(str.charAt(7) != '0'){
	                if(getLastLen(str, pos2) == 1){
	                    sb.append(formatDigit(str.charAt(7))+"日");
	                }
	                if(getLastLen(str, pos2) == 2){
	                    if(str.charAt(7) != '1' && str.charAt(8) != '0'){
	                        sb.append(formatDigit(str.charAt(7))+"十"+formatDigit(str.charAt(8))+"日");
	                    }
	                    else if(str.charAt(7) != '1' && str.charAt(8) == '0'){
	                        sb.append(formatDigit(str.charAt(7))+"十日");
	                    }
	                    else if(str.charAt(7) == '1' && str.charAt(8) != '0'){
	                        sb.append("十"+formatDigit(str.charAt(8))+"日");
	                    }
	                    else{
	                        sb.append("十日");
	                    }
	                }
	            }
	            else{
	                sb.append(formatDigit(str.charAt(8))+"日");
	            }
	        }
	        if(getMidLen(str,pos1,pos2) == 2){
	            if(str.charAt(5) != '0' && str.charAt(6) != '0'){
	                sb.append("十"+formatDigit(str.charAt(6))+"月");
	                if(getLastLen(str, pos2) == 1){
	                    sb.append(formatDigit(str.charAt(8))+"日");
	                }
	                if(getLastLen(str, pos2) == 2){
	                    if(str.charAt(8) != '0'){
	                        if(str.charAt(8) != '1' && str.charAt(9) != '0'){
	                            sb.append(formatDigit(str.charAt(8))+"十"+formatDigit(str.charAt(9))+"日");
	                        }
	                        else if(str.charAt(8) != '1' && str.charAt(9) == '0'){
	                            sb.append(formatDigit(str.charAt(8))+"十日");
	                        }
	                        else if(str.charAt(8) == '1' && str.charAt(9) != '0'){
	                            sb.append("十"+formatDigit(str.charAt(9))+"日");
	                        }
	                        else{
	                            sb.append("十日");
	                        }
	                    }
	                    else{
	                        sb.append(formatDigit(str.charAt(9))+"日");
	                    }
	                }
	            }
	            else if(str.charAt(5) != '0' && str.charAt(6) == '0'){
	                sb.append("十月");
	                if(getLastLen(str, pos2) == 1){
	                    sb.append(formatDigit(str.charAt(8))+"日");
	                }
	                if(getLastLen(str, pos2) == 2){
	                    if(str.charAt(8) != '0'){
	                        if(str.charAt(8) != '1' && str.charAt(9) != '0'){
	                            sb.append(formatDigit(str.charAt(8))+"十"+formatDigit(str.charAt(9))+"日");
	                        }
	                        else if(str.charAt(8) != '1' && str.charAt(9) == '0'){
	                            sb.append(formatDigit(str.charAt(8))+"十日");
	                        }
	                        else if(str.charAt(8) == '1' && str.charAt(9) != '0'){
	                            sb.append("十"+formatDigit(str.charAt(9))+"日");
	                        }
	                        else{
	                            sb.append("十日");
	                        }
	                    }
	                    else{
	                        sb.append(formatDigit(str.charAt(9))+"日");
	                    }
	                }
	            }
	            else{
	                sb.append(formatDigit(str.charAt(6))+"月");
	                if(getLastLen(str, pos2) == 1){
	                    sb.append(formatDigit(str.charAt(8))+"日");
	                }
	                if(getLastLen(str, pos2) == 2){
	                    if(str.charAt(8) != '0'){
	                        if(str.charAt(8) != '1' && str.charAt(9) != '0'){
	                            sb.append(formatDigit(str.charAt(8))+"十"+formatDigit(str.charAt(9))+"日");
	                        }
	                        else if(str.charAt(8) != '1' && str.charAt(9) == '0'){
	                            sb.append(formatDigit(str.charAt(8))+"十日");
	                        }
	                        else if(str.charAt(8) == '1' && str.charAt(9) != '0'){
	                            sb.append("十"+formatDigit(str.charAt(9))+"日");
	                        }
	                        else{
	                            sb.append("十日");
	                        }
	                    }
	                    else{
	                        sb.append(formatDigit(str.charAt(9))+"日");
	                    }
	                }
	            }
	        }
	        return sb.toString();
	    }
	    
    /**
	 * 将实体类（包括里面的实体类）转为map类型，然后返回一个map类型的值
	 * Giam 20160514
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> beanToMap(Object obj,Map<String, Object> parent) { 
		Map<String, Object> params = new HashMap<String, Object>(0);
		try { 
			PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean(); 
			PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj); 
			for (int i = 0; i < descriptors.length; i++) { 
				String name = descriptors[i].getName();//-- 变量名
				String type = descriptors[i].getPropertyType().getName();//-- 变量类型
				if (!StringUtils.equals(name, "class")) {
					Object o = propertyUtilsBean.getNestedProperty(obj, name);

					if (o != null && (type.indexOf("model") != -1 || type.indexOf("vo") != -1) ) {
						params.putAll(beanToMap(o,params));
					}else if (o != null && type.indexOf("Date") == -1) {
						if(o.toString()!="") params.put(name, o.toString());
					} else if (o != null && type.indexOf("Date") != -1) {
						Date date = (Date) o;
						Calendar c = Calendar.getInstance();
						c.setTime(date);
						
						params.put(name + "_Y", c.get(Calendar.YEAR)+"");
						params.put(name + "_M", c.get(Calendar.MONTH)+1+"");
						params.put(name + "_D", c.get(Calendar.DATE)+"");
						params.put(name + "_H", c.get(Calendar.HOUR_OF_DAY)+"");
						params.put(name + "_mm", c.get(Calendar.MINUTE)+"");
						params.put(name + "_CN", formatStr(date));
						
						/*
						params.put(name + "_Y", (date.getYear() + 1900) + "");
						params.put(name + "_M", (date.getMonth() + 1) + "");
						params.put(name + "_D", (date.getDate()) + "");
						params.put(name + "_H", (date.getHours() + ""));
						params.put(name + "_mm", (date.getMinutes()) + "");
						params.put(name + "_CN", formatStr(date));
						*/
					} else if(type.indexOf("Date") != -1) {
						params.put(name + "_Y", "");
						params.put(name + "_M", "");
						params.put(name + "_D", "");
						params.put(name + "_H", "");
						params.put(name + "_mm", "");
						params.put(name + "_CN", "");
					}else {
						if(parent != null) {
							if(!parent.containsKey(name)) params.put(name, "");
						}else{
							params.put(name, "");
						}
					}
					
					//params.put(name, propertyUtilsBean.getNestedProperty(obj, name)); 
				} 
			} 
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
		return params; 
	}
	

}
