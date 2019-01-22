package com.naswork.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.StringTokenizer;

public class ReflectTools {
	
	/**
	 * 返回属性的get方法名。
	 * @param fieldName 属性名。
	 * @return get方法名。
	 */
	public static String getMethodName(String fieldName)
	{
		return "get" 
				+ fieldName.substring(0,1).toUpperCase() 
				+ fieldName.substring(1);
	}
	
	/**
	 * 返回属性的set方法名。
	 * @param fieldName 属性名。
	 * @return set方法名。
	 * @author wsf
	 */
	public static String setMethodName(String fieldName)
	{
		return "set" 
				+ fieldName.substring(0,1).toUpperCase() 
				+ fieldName.substring(1);
	}
	
	/**
	 * 根据属性返回该类不带参数的get方法。
	 * @param cl 类。
	 * @param fieldNames 属性数组。
	 * @return get方法的数组。
	 * @throws NoSuchMethodException 
	 * @throws IllegalArgumentException 
	 */
	public static Method[] getMethods(Class cl,String[] fieldNames) throws IllegalArgumentException,NoSuchMethodException
	{
		Method[] getMethods = new Method[fieldNames.length];
		
		for (int i =0; i < fieldNames.length; i++) {
			getMethods[i] = cl.getMethod(getMethodName(fieldNames[i]),new Class[0]);
		}
		return getMethods;
	}
	
	/**
	 * 调用getter方法
	 * @param target
	 * @param propertyName
	 * @return
	 * @author wsf
	 */
	public  static Object invokeGetterMethod(Object target, String propertyName){
		
		String getterMethodName = getMethodName(propertyName);
		
		return invokeMethod(target, getterMethodName, new Class<?>[]{}, new Object[]{});
		
	}
	
	/**
	 * 根据属性调用set方法
	 * @param c1 类
	 * @param fieldName 属性名
	 * @param value  参数值
	 * @author wsf
	 */
	public static void invokeSetterMethod(Object c1 ,String fieldName, Object value ){
		
		invokeSetterMethod(c1, fieldName, value ,null );
		
	}
	/**
	 * 根据属性调用set方法
	 * @param c1 类
	 * @param fieldName 属性名
	 * @param value  参数值
	 * @param propertyType  参数类型
	 * @return
	 * @author wsf
	 */
	public static void invokeSetterMethod(Object c1 ,String fieldName, Object value , Class<?> propertyType ){
		
		Class<?> type = propertyType != null ? propertyType : value.getClass();
		String setterMethodName = setMethodName(fieldName);
		invokeMethod(c1 , setterMethodName , new Class[]{type},new Object[]{value} );
	}
	
	/**
	 * 调用指定方法
	 * 
	 * @param c1 类
	 * @param methodName   方法名
	 * @param parameterTypes 参数类型
	 * @param parameters  参数
	 * @return   调用该方法返回的值
	 * @author wsf
	 */
	public static Object invokeMethod(final Object c1,final String methodName , 
			final Class<?>[] parameterTypes ,final Object[] parameters){
		
		 
		 Method   method = getDeclaredMethod(c1, methodName, parameterTypes);
		 
		 if(method == null){
			 throw new IllegalArgumentException("不能找到"+methodName+"方法"); 
		 }
		 
		 method.setAccessible(true);
		 
		 try {
			 
			return method.invoke(c1, parameters);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	} 
	
	
	
	/**
	 * 
	 * @param c1
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 * @author wsf
	 */
	public static Method getDeclaredMethod(Object c1 , String methodName ,
				 Class<?>[] parameterTypes){
		
		if(c1 == null){
			throw new IllegalArgumentException("参数为空！");
		}
		
		for(Class<?> superClass = c1.getClass();superClass != Object.class ; 
				superClass = superClass.getSuperclass()){
			
			try {
				
				return superClass.getDeclaredMethod(methodName, parameterTypes);
			
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		return null;
		
	}
	
	
	/**
	 * 根据属性返回该类不带参数的get方法。
	 * @param cl 类。
	 * @param fieldName 属性名称。
	 * @return fieldName属性的get方法。
	 * @throws NoSuchMethodException 
	 * @throws IllegalArgumentException 
	 */
	public static Method getMethod(Class cl,String fieldName) throws IllegalArgumentException,NoSuchMethodException
	{
		return cl.getMethod(getMethodName(fieldName),new Class[0]);
	}
	
	/**
	 * 获得一个实例的属性值。
	 * @param instance 实例，必需实现该属性的getter方法。
	 * @param fieldName 属性名称。
	 * @return 该属性在实例中的值。
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InvocationTargetException 
	 */
	public static Object getFieldValue(Object instance, String fieldName) throws IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		Method getMethod = getMethod(instance.getClass(),fieldName);
		getMethod.setAccessible(true);
		return getMethod.invoke(instance,new Object[0]);
	}	
	
	/**
	 * 获得一个实例属性值的扩展方法，若属性名称为xxx.xxxx的形式，则自动取属性的属性值。
	 * @param instance 实例，必需实现该属性的getter方法。
	 * @param fieldName 属性名称，可以用xxx.xxxx的形式。
	 * @return 该属性在实例中的值。
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InvocationTargetException 
	 */
	public static Object getFieldExtend(Object instance, String fieldName) throws IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		Method getMethod = null;
		Object oFieldValue = instance;
		StringTokenizer st = new StringTokenizer(fieldName,".");
		while (st.hasMoreElements()) {
			getMethod = getMethod(oFieldValue.getClass(),st.nextToken());
			getMethod.setAccessible(true);
			oFieldValue = getMethod.invoke(oFieldValue,new Object[0]);
		}
		return oFieldValue;
	}
	
	
	/**
	 * 设置一个实例的字符串属性值。
	 * @param instance 实例，必需实现该属性的setter方法。
	 * @param fieldName 属性名称。
	 * @param value 要设置的值。
	 * @return
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @since 2007-9-9
	 * @author zhw
	 * @version 1.00 2007-9-9
	 */
	public static void setFieldValue(Object instance, String fieldName, String value) throws IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		String sMethodName = "set" 
							 + fieldName.substring(0,1).toUpperCase() 
							 + fieldName.substring(1);
		Class[] aClazz = {String.class};
		Object[] aoValues = {value};
		Method setMethod = instance.getClass().getMethod(sMethodName,aClazz);
		setMethod.setAccessible(true);
		setMethod.invoke(instance,aoValues);
	}

	
	public  static void makeAccessible(final Field field){
		
		if (!Modifier.isPublic(field.getModifiers())
			|| !Modifier.isPublic(field.getDeclaringClass().getModifiers())){
			field.setAccessible(true);
		}
	}
	
	
	
}
