package com.asiainfo.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.asiainfo.bean.AsiainfoHeader;





/**
 * 纯字符串字典结构。
 * 
 * @author carver.gu
 * @since 1.0, Sep 13, 2009
 */
public class AsiainfoHashMap extends HashMap<String, String> {

	private static final long serialVersionUID = -1277791390393392630L;
	
	 /** 默认时间格式 **/
    public static final String DATE_TIME_FORMAT               = "yyyy-MM-dd HH:mm:ss";

    /**  Date默认时区 **/
    public static final String DATE_TIMEZONE                  = "GMT+8";

	public AsiainfoHashMap() {
		super();
	}

	public AsiainfoHashMap(Map<? extends String, ? extends String> m) {
		super(m);
	}

	public String put(String key, Object value) {
		String strValue;

		if (value == null) {
			strValue = null;
		} else if (value instanceof String) {
			strValue = (String) value;
		} else if (value instanceof Integer) {
			strValue = ((Integer) value).toString();
		} else if (value instanceof Long) {
			strValue = ((Long) value).toString();
		} else if (value instanceof Float) {
			strValue = ((Float) value).toString();
		} else if (value instanceof Double) {
			strValue = ((Double) value).toString();
		} else if (value instanceof Boolean) {
			strValue = ((Boolean) value).toString();
		} else if (value instanceof Date) {
            DateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT);
            format.setTimeZone(TimeZone.getTimeZone(DATE_TIMEZONE));
			strValue = format.format((Date) value);
		} else {
			strValue = value.toString();
		}

		return this.put(key, strValue);
	}
//将报文头对象转换为内部使用map。
	public static AsiainfoHashMap toAsiainfoHashMap(AsiainfoHeader header){
		AsiainfoHashMap map=new AsiainfoHashMap();
		
		Field[] fields=header.getClass().getDeclaredFields(); 
		 
		 for(int i=0;i<fields.length;i++){  
    		if(getFieldValueByName(fields[i].getName(), header)!=null){
				 map.put(fields[i].getName(), getFieldValueByName(fields[i].getName(), header));
			 }
 
		    }  
		
		return map;
	}
	
	/** 
	 * 根据属性名获取属性值 
	 * */  
	   private static Object getFieldValueByName(String fieldName, Object o) {  
	       try {    
	           String firstLetter = fieldName.substring(0, 1).toUpperCase();    
	           String getter = "get" + firstLetter + fieldName.substring(1);    
	           Method method = o.getClass().getMethod(getter, new Class[] {});    
	           Object value = method.invoke(o, new Object[] {});    
	           return value;    
	       } catch (Exception e) {    
	           return null;    
	       }    
	   }   
	
	public String put(String key, String value) {
		if (areNotEmpty(key, value)) {
			return super.put(key, value);
		} else {
			return null;
		}
	}

	/**
     * 检查指定的字符串列表是否不为空。
     */
	public static boolean areNotEmpty(String... values) {
		boolean result = true;
		if (values == null || values.length == 0) {
			result = false;
		} else {
			for (String value : values) {
				result &= !isEmpty(value);
			}
		}
		return result;
	}
	
	public static boolean isEmpty(String str) {
		return ((str == null) || (str.length() == 0));
	}
    
}
