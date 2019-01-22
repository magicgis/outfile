package com.naswork.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import oracle.sql.TIMESTAMP;

import org.apache.commons.lang.StringUtils;

import com.naswork.vo.GridSort;

public class ConvertUtil {

	/**
	 * 将一个MAP按key顺序转换为Object数组
	 * 
	 * @param m
	 *            被转换的MAP
	 * @param key
	 *            Map中的KEY转成数组的顺序
	 * @return
	 * @throws Exception
	 */
	public static Object[] mapToObjectArray(Map<String, Object> m, String[] key)
			throws Exception {
		if (m == null) {
			throw new IllegalArgumentException("MAP为空,无法转成数组！");
		}

		if (key == null) {
			throw new IllegalArgumentException("KEY属性为空，无法按顺序转成数组！");
		}

		Object[] result = new Object[key.length];

		int i = 0;
		for (String k : key) {
			// 判断列名在MAP中是否存在，匹配顺序为 原名匹配->全大写匹配->全小写匹配。无法匹配时KEY为空
			String nk = "";
			if (m.containsKey(k))
				nk = k;
			else if (m.containsKey(k.toUpperCase()))
				nk = k.toUpperCase();
			else if (m.containsKey(k.toLowerCase()))
				nk = k.toLowerCase();
			else
				nk = "";
			// 获取map中每个显示列对应属性值，添加到result一列中
			if (!"".equals(nk)) {
				if (m.get(nk) == null)
					result[i] = "";
				else if (m.get(nk).getClass().equals(TIMESTAMP.class))
					result[i] = convertToTimestampStr((TIMESTAMP) m.get(nk));
				else
					result[i] = m.get(nk);
			} else
				result[i] = "";

			i++;
		}

		return result;
	}

	/**
	 * 将一个bean按key顺序转换为Object数组
	 * 
	 * @param bean
	 *            被转换的bean
	 * @param beanClass
	 *            bean的Class
	 * @param key
	 *            Map中的KEY转成数组的顺序
	 * @return
	 * @throws Exception
	 */
	public static Object[] beanToObjectArray(Object bean, Class beanClass,
			String[] key) throws Exception {
		if (bean == null) {
			throw new IllegalArgumentException("被转换的BEAN对象为空，无法转换为数组！");
		}
		if (beanClass == null) {
			throw new IllegalArgumentException("JavaBean类型为空，无法转换为数组！");
		}
		if (key == null) {
			throw new IllegalArgumentException("KEY属性为空，无法按顺序转成数组！");
		}
		Object[] result = new Object[key.length];

		Method[] getMethods = ReflectTools.getMethods(beanClass, key);
		// 将bean的属性get到Object数组
		for (int j = 0; j < getMethods.length; j++) {
			// 调用get的方法
			result[j] = getMethods[j].invoke(bean, new Object[0]);
		}
		return result;
	}

	/**
	 * 对象命名格式转数据库命名格式
	 * 
	 * @param <T>
	 * @param o
	 *            被解析的名称
	 * @return 符合数据库名命规范的名称
	 */
	public static String toDBName(String name) {
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (Character.isUpperCase(c)) {
				char nc = Character.toLowerCase(c);
				name = name.replaceAll(c + "", "_" + nc);
				i++;
			}
		}
		if (name.charAt(0) == '_')
			name = name.substring(1);
		return name.trim().toUpperCase();
	}

	/**
	 * 数据库命名格式转对象命名格式
	 * 
	 * @param o
	 *            被解析的数据库命名
	 * @return 符合对象命名规范的名称
	 */
	public static String toObjName(String name) {
		name = name.toLowerCase() + " ";
		int i = name.indexOf("_");
		while (i != -1) {
			i = name.indexOf("_");
			name = name.replaceAll("_" + name.charAt(i + 1),
					Character.toUpperCase(name.charAt(i + 1)) + "");

		}
		return name.trim();
	}

	/**
	 * 将一个 Map 对象转化为一个
	 * JavaBean,只能转换基础类型BigDecimal,Long,Integer,Double,Float,String
	 * ,Boolean,Date,Timestamp 属性名匹配MAP的KEY时，将尝试 原名匹配->全大写匹配->全小写匹配
	 * 
	 * @param type
	 *            要转化的类型
	 * @param map
	 *            包含属性值的 map
	 * @return 转化出来的 JavaBean 对象
	 * @throws Exception
	 */
	public static Object mapToBean(Class type, Map map) throws Exception {
		BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
		Object obj = type.newInstance(); // 创建 JavaBean 对象

		// 给 JavaBean 对象的属性赋值
		PropertyDescriptor[] propertyDescriptors = beanInfo
				.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			propertyName = toDBName(propertyName);//转成数据库字段,modify by chenguojun
			String key = "";
			if (map.containsKey(propertyName))
				key = propertyName;
			else if (map.containsKey(propertyName.toUpperCase()))
				key = propertyName.toUpperCase();
			else if (map.containsKey(propertyName.toLowerCase()))
				key = propertyName.toLowerCase();
			else
				key = "";

			if (!"".equals(key)) {// MAP中存在该属性时，判断该属性是不是被注解需从自身或其它字段替换值的属性，若不需替换则根据属性类型直接赋值
				Object value = map.get(key);

				Object[] args = new Object[1];
				args[0] = value;

				/**
				 * 关联数据取数来源已调整为直接在查询时通过JOIN获取，对象转换不再处理关联值的注解 ConvertDm
				 * convertDm=
				 * type.getDeclaredField(propertyName).getAnnotation(ConvertDm
				 * .class); if (convertDm!=null){//该属性是被注解需从自身或其它字段替换值的属性 String
				 * dmKey=value.toString(); if
				 * (convertDm.sourceColumn().length!=0){ dmKey=""; for(String
				 * keyTemp: convertDm.sourceColumn() ){ if
				 * (map.containsKey(keyTemp)) ; else if
				 * (map.containsKey(keyTemp.toUpperCase()))
				 * keyTemp=keyTemp.toUpperCase(); else if
				 * (map.containsKey(keyTemp.toLowerCase()))
				 * keyTemp=keyTemp.toLowerCase(); else keyTemp=""; if (
				 * !keyTemp.equals("") ){
				 * dmKey+="@"+map.get(keyTemp).toString(); }
				 * 
				 * } dmKey=dmKey.substring(1); } IDmService dmService =
				 * (IDmService)SpringHelper.getBean("dmService");
				 * value=dmService.getDm(convertDm.dmTable(),
				 * convertDm.dmColumn(), convertDm.valColumn()).get(dmKey);
				 * 
				 * descriptor.getWriteMethod().invoke(obj, value); }else
				 */
				{// 判断该属性不是被注解需从自身或其它字段替换值的属性，根据属性类型直接赋值
					String objFullName = descriptor.getPropertyType()
							.toString();
					String objClassName = objFullName.substring(objFullName
							.lastIndexOf(".") + 1);
					String valueFullName = value.getClass().getName()
							.toString();
					String valueClassName = valueFullName
							.substring(valueFullName.lastIndexOf(".") + 1);
					for (DataClassType d : DataClassType.values()) {// 判断是否支持该类型的转换
						if (d.name().equals(objClassName)) {// 为TRUE时支持转换

							DataClassType objClass = DataClassType
									.valueOf(objClassName);
							switch (objClass) {// 根据对象的属性类型,对不同来数据来源类型作处理
							case String:
								args[0] = value + "";
								break;
							case BigDecimal:
								if (valueClassName.equals("BigDecimal")) {
									args[0] = value;
								} else if (NUM_TYPE.indexOf(valueClassName) != -1)// 如果来源类型为数值类,反射数值类型的值转换方法取目标类型的值
									args[0] = new BigDecimal((Long) value
											.getClass().getMethod("longValue")
											.invoke(value));
								else
									args[0] = value;
								break;
							case Long:
								if (NUM_TYPE.indexOf(valueClassName) != -1)// 如果来源类型为数值类,反射数值类型的值转换方法取目标类型的值
									args[0] = value.getClass()
											.getMethod("longValue")
											.invoke(value);
								else
									args[0] = value;
								break;
							case Integer:
								if (NUM_TYPE.indexOf(valueClassName) != -1)// 如果来源类型为数值类,反射数值类型的值转换方法取目标类型的值
									args[0] = value.getClass()
											.getMethod("intValue")
											.invoke(value);
								else
									args[0] = value;
								break;
							case Double:
								if (NUM_TYPE.indexOf(valueClassName) != -1)// 如果来源类型为数值类,反射数值类型的值转换方法取目标类型的值
									args[0] = value.getClass()
											.getMethod("doubleValue")
											.invoke(value);
								else
									args[0] = value;
								break;
							case Float:
								if (NUM_TYPE.indexOf(valueClassName) != -1)// 如果来源类型为数值类,反射数值类型的值转换方法取目标类型的值
									args[0] = value.getClass()
											.getMethod("floatValue")
											.invoke(value);
								else
									args[0] = value;
								break;
							case Boolean:
								args[0] = value;
								if (valueClassName.equals("String")) {// 如果来源类型为字符类,判断0,1值
									args[0] = value.equals("1") ? true : (value
											.equals("0") ? false : null);
								}
								break;
							case Date:
								if (valueClassName.equals("Date")) {// 如果来源类型为日期类
									args[0] = value;
								} else if (valueClassName.equals("String")) {
									args[0] = DateUtil
											.parseDate(
													value.toString(),
													DateUtil.DateFormatType.SIMPLE_DATE_FORMAT_STR);
								} else if (valueClassName.equals("Timestamp")) {
									args[0] = new Date(
											((Timestamp) value).getTime());
								} else if (valueClassName.equals("TIMESTAMP")) {
									args[0] = ((TIMESTAMP) value).dateValue();
								}
								break;
							case TIMESTAMP:
								if (valueClassName.equals("TIMESTAMP")) {// 如果来源类型为日期类
									args[0] = value;
								} else if (valueClassName.equals("Timestamp")) {
									args[0] = ConvertUtil
											.convertToTIMESTAMP((Timestamp) value);
								} else if (valueClassName.equals("Long")) {
									args[0] = ConvertUtil
											.convertToTIMESTAMP(value
													.toString());
								} else {
									args[0] = null;
								}
								break;
							case Timestamp:
								if (valueClassName.equals("TIMESTAMP")) {// 如果来源类型为日期类
									args[0] = ConvertUtil
											.convertToTimestamp((TIMESTAMP) value);
								} else if (valueClassName.equals("Timestamp")) {
									args[0] = value;
								} else if (valueClassName.equals("String")) {
									args[0] = ConvertUtil
											.convertToTIMESTAMP(value
													.toString());
								} else {
									args[0] = null;
								}
								break;
							}
							descriptor.getWriteMethod().invoke(obj, args);
							break;
						}
					}
				}

			}
			/**
			 * 关联数据取数来源已调整为直接在查询时通过JOIN获取，对象转换不再处理关联值的注解
			 * else{//MAP中不存在该属性时，判断该属性是不是被注解需从其它字段替换值的属性 （查询结果中不存在该字段）
			 * 
			 * if (propertyName.equals("class")){ continue; } ConvertDm
			 * convertDm
			 * =type.getDeclaredField(propertyName).getAnnotation(ConvertDm
			 * .class); if (convertDm!=null &&
			 * convertDm.sourceColumn().length!=0){//直接替换代码值 String dmKey="";
			 * String value=""; for(String keyTemp: convertDm.sourceColumn() ){
			 * if (map.containsKey(keyTemp)) ; else if
			 * (map.containsKey(keyTemp.toUpperCase()))
			 * keyTemp=keyTemp.toUpperCase(); else if
			 * (map.containsKey(keyTemp.toLowerCase()))
			 * keyTemp=keyTemp.toLowerCase(); else keyTemp=""; if (
			 * !keyTemp.equals("") ){ dmKey+="@"+map.get(keyTemp).toString(); }
			 * 
			 * } dmKey=dmKey.substring(1); IDmService dmService =
			 * (IDmService)SpringHelper.getBean("dmService");
			 * value=dmService.getDm(convertDm.dmTable(), convertDm.dmColumn(),
			 * convertDm.valColumn()).get(dmKey);
			 * descriptor.getWriteMethod().invoke(obj, value); }
			 * 
			 * 
			 * }
			 */
		}
		return obj;
	}

	// 支持转换的属性类型
	public static enum DataClassType {
		BigDecimal, Long, Integer, Double, Float, String, Boolean, Date, TIMESTAMP, Timestamp, Object
	}

	// 数值类型的对象类型名
	public static String NUM_TYPE = "BigDecimal,Long,Integer,Double,Float";
	// 日期类型的对象类型名
	public static String DATE_TYPE = "Date,TIMESTAMP,Timestamp";

	/**
	 * 将一个 JavaBean 对象转化为一个 Map
	 * 
	 * @param bean
	 *            要转化的JavaBean 对象
	 * @return 转化出来的 Map 对象
	 * @throws IntrospectionException
	 *             如果分析类属性失败
	 * @throws IllegalAccessException
	 *             如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *             如果调用属性的 setter 方法失败
	 */
	public static Map beanToMap(Object bean) throws Exception {
		Class type = bean.getClass();
		Map returnMap = new HashMap();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);

		PropertyDescriptor[] propertyDescriptors = beanInfo
				.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class")) {
				Method readMethod = descriptor.getReadMethod();
				Object result = null;
				try {
					result = readMethod.invoke(bean, new Object[0]);
				} catch (Exception e) {
					result = null;
				}
				if (result != null) {
//					returnMap.put(
//							propertyName,
//							new String(URLDecoder.decode(result.toString(),
//									"iso-8859-1").getBytes("iso-8859-1"),
//									"utf-8"));
					if(result instanceof Date){
						returnMap.put(
								propertyName,result);
					}else{
						returnMap.put(
								propertyName,result.toString());
					}
				} else {
					returnMap.put(propertyName, null);
				}
			}
		}
		return returnMap;
	}

	/**
	 * 将一个 JavaBean 对象转化为一个 Map ，将Null转为特定值
	 * 
	 * @param bean
	 *            要转化的JavaBean 对象
	 * @return 转化出来的 Map 对象
	 * @throws IntrospectionException
	 *             如果分析类属性失败
	 * @throws IllegalAccessException
	 *             如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *             如果调用属性的 setter 方法失败
	 */
	public static Map beanToMapWithNull(Object bean, String nullStr)
			throws IntrospectionException, IllegalAccessException,
			InvocationTargetException {
		Class type = bean.getClass();
		Map returnMap = new HashMap();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);

		PropertyDescriptor[] propertyDescriptors = beanInfo
				.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class")) {
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null) {
					returnMap.put(propertyName, result);
				} else {
					returnMap.put(propertyName, nullStr);
				}
			}
		}
		return returnMap;
	}

	/**
	 * oracle.sql.TIMESTMP转java.sql.Timestamp
	 * 
	 * @param t
	 *            oracle的TIMESTAMP 时间类型
	 * @return java.sql.Timestamp 时间类型
	 * @throws Exception
	 */
	public static Timestamp convertToTimestamp(TIMESTAMP t) throws Exception {
		return t.timestampValue();
	}

	/**
	 * java.sql.Timestamp转oracle.sql.TIMESTMP
	 * 
	 * @param t
	 *            java的Timestamp 时间类型
	 * @return oracle的TIMESTAMP 时间类型
	 * @throws Exception
	 */
	public static TIMESTAMP convertToTIMESTAMP(Timestamp t) {
		return new TIMESTAMP(t);
	}

	/**
	 * 'yyyy-mm-dd hh24:mi:ss.ff'类型字符串转oracle.sql.TIMESTMP
	 * 
	 * @param t
	 *            'yyyy-mm-dd hh24:mi:ss.ff'格式字符串
	 * @return oracle.sql.TIMESTMP类型时间
	 * @throws Exception
	 */
	public static TIMESTAMP convertToTIMESTAMP(String t) {
		return new TIMESTAMP(t);
	}

	/**
	 * 'yyyy-mm-dd hh24:mi:ss.ff'类型字符串转java.sql.Timestamp
	 * 
	 * @param t
	 *            'yyyy-mm-dd hh24:mi:ss.ff'格式字符串
	 * @return java.sql.Timestamp类型时间
	 * @throws Exception
	 */
	public static Timestamp convertToTimestamp(String t) throws Exception {
		return convertToTimestamp(new TIMESTAMP(t));
	}

	/**
	 * oracle.sql.TIMESTAMP 转'yyyy-mm-dd hh24:mi:ss.ff'
	 * 
	 * @param t
	 *            oracle.sql.TIMESTAMP
	 * @return 'yyyy-mm-dd hh24:mi:ss.ff'格式字符串
	 * @throws Exception
	 */

	public static String convertToTimestampStr(TIMESTAMP t) throws Exception {
		Timestamp timestamp = convertToTimestamp(t);
		return timestamp.toString();
	}

	/**
	 * java.sql.Timestamp 转'yyyy-mm-dd hh24:mi:ss.ff'类型字符串
	 * 
	 * @param t
	 *            java.sql.Timestamp
	 * @return 'yyyy-mm-dd hh24:mi:ss.ff'格式字符串
	 */
	public static String convertToTimestampStr(Timestamp t) {
		return t.toString();
	}

	/**
	 * resultSet转List
	 * 
	 * @param resultSet
	 * @return
	 * @throws Exception
	 * @since 2014-6-5 上午9:38:27
	 * @author konglinghong
	 * @version v1.0
	 */
	public static List<Map<String, Object>> convertToList(ResultSet resultSet)
			throws Exception {
		if (resultSet == null)
			return Collections.emptyList();
		Vector<String> vector = getHead(resultSet);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		while (resultSet.next()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (String colName : vector) {
				if (null != resultSet.getObject(colName)) {
					map.put(colName, resultSet.getObject(colName));
				}
			}
			result.add(map);
		}
		return result;
	}

	/**
	 * 获取列名
	 * 
	 * @param rs
	 * @return
	 * @throws Exception
	 * @since 2014-6-3 下午3:38:25
	 * @author konglinghong
	 * @version v1.0
	 */
	private static Vector<String> getHead(ResultSet rs) throws Exception {
		if (rs == null)
			return null;
		Vector<String> vector = new Vector<String>();
		ResultSetMetaData md = rs.getMetaData();
		int count = md.getColumnCount();
		for (int i = 0; i < count; i++) {
			vector.add(md.getColumnName(i + 1));
		}
		return vector;
	}

	public static String formatStr(Object obj, Integer num) {
		if (null == obj) {
			return "0";
		}

		DecimalFormat fm = new DecimalFormat();
		if (null != num) {
			fm.setMaximumFractionDigits(num);
		}
		fm.setGroupingSize(3);
		return fm.format(obj);
	}
	
	/**
	 * 将map中的有所有key改成objName
	 * @param objmap
	 * @return
	 * @since 2015年10月7日 下午3:12:51
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public static Map<String, Object> toObjName(Map<String, Object> objmap) {
		Map<String,Object> map = new HashMap<String, Object>();
		for (String key : objmap.keySet()) {
			map.put(toObjName(key), objmap.get(key));
		}
		return map;
	}
	
	/**
	 * clob to String
	 * @param clob
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static String ClobToString(Clob clob) throws SQLException, IOException {

		String reString = "";
		Reader is = clob.getCharacterStream();// 得到流
		BufferedReader br = new BufferedReader(is);
		String s = br.readLine();
		StringBuffer sb = new StringBuffer();
		while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
		sb.append(s);
		s = br.readLine();
		}
		reString = sb.toString();
		return reString;
		}

	public static String convertWsh(String wsh) {
		if(wsh.indexOf("-")!=-1){
			wsh = wsh.replaceFirst("-", "〕");
			return "〔"+wsh;
		}
		return wsh;
	}
	
	public static String convertSort(GridSort sort) {
		StringBuffer orderby = new StringBuffer();
		if (null != sort) {
			if (StringUtils.isNotEmpty(sort.getName())) {
				orderby.append(" order by ").append(sort.getName());
			}
			if (StringUtils.isNotEmpty(sort.getOrder())) {
				orderby.append(" ").append(sort.getOrder());
			}
		}
		return orderby.toString();
	}
}
