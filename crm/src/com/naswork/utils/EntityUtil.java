package com.naswork.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

public class EntityUtil  {
	
	
	/**
	 * 根据类型返回符合类型的属性名称
	 * @param entity
	 * @param type
	 * @return
	 * @since 2013-7-23 下午5:59:41
	 * @author konglinghong
	 * @version v1.0
	 */
	public static String[] getName(Class entityClass, Class type){
		PropertyDescriptor[] pd = PropertyUtils.getPropertyDescriptors(entityClass);
		String[] names = {};
		for (PropertyDescriptor propertyDescriptor : pd) {
			if(!propertyDescriptor.getPropertyType().equals(Class.class) 
					&& propertyDescriptor.getPropertyType().equals(type)){
				names = Arrays.copyOf(names, names.length+1);
				names[names.length-1] = propertyDescriptor.getName();
			}
		}
		return names;
	}
	
	/**
	 * 实体内基本类型 和String类型转Map
	 * @param entity
	 * @return
	 * @since 2013-7-23 下午3:14:16
	 * @author konglinghong
	 * @version v1.0
	 */
	public static Map<String,Object> entityToMapSlim(Object entity){
		return entityToMapSlim(entity,null);
	}
	
	/**
	 * 实体内基本类型 和String类型转Map
	 * @param entity
	 * @param prefix 前缀
	 * @return
	 * @since 2013-7-23 下午3:14:16
	 * @author konglinghong
	 * @version v1.0
	 */
	public static Map<String,Object> entityToMapSlim(Object entity,String prefix){
		PropertyDescriptor[] pd = PropertyUtils.getPropertyDescriptors(entity);
		Map<String,Object> map = new HashMap<String, Object>();
		boolean hasPrefix = StringUtils.isNotEmpty(prefix);
		for (PropertyDescriptor propertyDescriptor : pd) {
			Class targetType = propertyDescriptor.getPropertyType();
			if(!targetType.equals(Class.class) && (isWrapClass(targetType) || 
					targetType.getPackage().getName().equals("java.lang")) || targetType.equals(Date.class)){
				try {
					Object obj = propertyDescriptor.getReadMethod().invoke(entity, null);
					Class clazz = PropertyUtils.getPropertyType(entity, propertyDescriptor.getName());
					
					if(clazz.equals(Boolean.class)&& obj != null){
						obj = ((Boolean)obj).booleanValue()?"1":"0";
					}
					if(clazz.equals(Date.class)){
						if(null!=obj){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							obj = sdf.format((Date)obj).replaceAll("\\s00:00:00\\d*$", "");
						}
					}
					if(hasPrefix)map.put(prefix+"."+propertyDescriptor.getName(), obj);
					else
					map.put(propertyDescriptor.getName(), obj);
					//map.put(propertyDescriptor.getName(), propertyDescriptor.getReadMethod().invoke(entity, null));
				}catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}
	
	/**
	 * 实体内基本类型 和String类型转Map
	 * @param entity
	 * @param prefix 前缀
	 * @return
	 * @since 2013-7-23 下午3:14:16
	 * @author konglinghong
	 * @version v1.0
	 */
	public static Map<String,Object> entityToTableMap(Object entity,String prefix){
		if(null == entity) return new HashMap<String, Object>();
		PropertyDescriptor[] pd = PropertyUtils.getPropertyDescriptors(entity);
		Map<String,Object> map = new HashMap<String, Object>();
		boolean hasPrefix = StringUtils.isNotEmpty(prefix);
		for (PropertyDescriptor propertyDescriptor : pd) {
			Class targetType = propertyDescriptor.getPropertyType();
			if(!targetType.equals(Class.class) && (isWrapClass(targetType) || 
					targetType.getPackage().getName().equals("java.lang")) || targetType.equals(Date.class)){
				try {
					
					Object obj = propertyDescriptor.getReadMethod().invoke(entity, null);
					Class clazz = PropertyUtils.getPropertyType(entity, propertyDescriptor.getName());
					if(clazz.equals(Boolean.class)&& obj != null){
						obj = ((Boolean)obj).booleanValue()?"1":"0";
					}
					if(clazz.equals(Date.class)){
						if(null!=obj){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							obj = sdf.format((Date)obj).replaceAll("\\s00:00:00\\d*$", "");
						}
					}
					String name = propertyDescriptor.getName();
//					Method oder = propertyDescriptor.getReadMethod();
//					Column c = oder.getAnnotation(Column.class);
//					String name = c.name();
					if(obj==null || obj.equals("null")){
						obj = "";
					}
					
					if(hasPrefix)map.put(prefix+"."+name, obj);
					else
					map.put(name, obj);
					//map.put(propertyDescriptor.getName(), propertyDescriptor.getReadMethod().invoke(entity, null));
				}catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}
	
	/**
	 * 实体内基本类型 和String类型转Map
	 * @param entity
	 * @param prefix 前缀
	 * @return
	 * @since 2013-7-23 下午3:14:16
	 * @author konglinghong
	 * @version v1.0
	 */
	public static Map<String,Object> entityToTableMap(Object entity){
		return entityToTableMap(entity,null);
	}
	
	
	/**
	 * List转Map
	 * @param list
	 * @return
	 * @since 2013-7-23 下午3:33:32
	 * @author konglinghong
	 * @version v1.0
	 */
	public static List<Map<String,Object>> entitiesToMap(List list){
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		if(null !=list && list.size()>0){
			Object sample = list.get(0);
			PropertyDescriptor[] pd = PropertyUtils.getPropertyDescriptors(sample);
			String []properties = {};
			for (PropertyDescriptor propertyDescriptor : pd) {
				Class targetType = propertyDescriptor.getPropertyType();
				if(!targetType.equals(Class.class) && !targetType.isArray()&&(isWrapClass(targetType) || 
						targetType.getPackage().getName().equals("java.lang"))){
					properties = Arrays.copyOf(properties, properties.length+1);
					properties[properties.length-1] = propertyDescriptor.getName();
				}
			}
			Map<String,Object> map = null;
			for (Object entity : list) {
				map = new HashMap<String, Object>();
				for (String property : properties) {
					try {
						Object obj = PropertyUtils.getProperty(entity, property);
						Class clazz = PropertyUtils.getPropertyType(entity, property);
						if(clazz.equals(Boolean.class)){
							obj = ((Boolean)obj).booleanValue()?"1":"0";
						}
						if(clazz.equals(Date.class)){
							if(null!=obj){
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								obj = sdf.format((Date)obj).replaceAll("\\s00:00:00\\d*$", "");
							}
						}
						map.put(property, obj);
					}catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
				}
				resultList.add(map);
			}
		}
		return resultList;
	}
	
	//是否包装类
	public static boolean isWrapClass(Class clz) {
		try {
			return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
		} catch (Exception e) {
			return false;
		}
	}
	

}
