/**
 * 
 */
package com.naswork.codegenerator;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleClob;
import oracle.sql.TIMESTAMP;

import com.naswork.utils.Freemarker;
/**
 * @since 2016年4月18日 下午3:48:27
 * @author xiaohu
 * @version v1.0
 */
public class CodeGenerator {
	
	public static final String NAME = "NAME";
	public static final String TYPE = "TYPE";
	public static final String SIZE = "SIZE";
	public static final String CLASS = "CLASS";
	public static final String NOW_DATE = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	public static final String DB_NAME = PropertiesHelper.get("conn.url").substring(PropertiesHelper.get("conn.url").lastIndexOf("/")+1,PropertiesHelper.get("conn.url").indexOf("?") == -1? PropertiesHelper.get("conn.url").length():PropertiesHelper.get("conn.url").indexOf("?"));
	String username = PropertiesHelper.get("conn.username");
	public String ROOT_PACKAGE = "";
	public String AUTHOR = "";
	public String FTL_PATH = "";
	public String FILE_PATH =  "";
	public static final List<String> IGNORE_TABLE_PREFIX = new ArrayList<String>();


	
	
	/**
	 * 生成实体类的代码
	 * @param table
	 * @throws Exception
	 * @since 2016年4月18日 下午3:49:35
	 * @author xiaohu
	 * @version v1.0
	 */
	public void createEntityClass(Map<String, Object> root) throws Exception{
		/*生成entity*/
		Freemarker.printFile("entityTemplate.ftl", root, root.get("className")+".java", FILE_PATH+"model/", FTL_PATH);
	}
	
	/** 生成Mapper  */
	public void createMapperClass(Map<String, Object> root) throws Exception{
		/*生成mapper*/
		Freemarker.printFile("mapperTemplate.ftl", root, root.get("className")+".xml",FILE_PATH+ "config/mybatis/mapping/", FTL_PATH);
	}

	/** 生成Dao  */
	public void createDaoClass(Map<String, Object> root) throws Exception{
		/*生成dao*/
		Freemarker.printFile("daoTemplate.ftl", root, root.get("className")+"Dao.java", FILE_PATH+"dao/", FTL_PATH);

	}
	
	/** 生成Service  */
	public void createServiceClass(Map<String, Object> root) throws Exception{
		/*生成service*/
		Freemarker.printFile("serviceTemplate.ftl", root, root.get("className")+"Service.java", FILE_PATH+"service/", FTL_PATH);
	}
	
	/** 生成ServiceImpl  */
	public void createServiceImplClass(Map<String, Object> root) throws Exception{
		/*生成serviceimpl*/
		Freemarker.printFile("serviceimplTemplate.ftl", root, root.get("className")+"ServiceImpl.java", FILE_PATH+"service/impl/", FTL_PATH);
	}
	
	/**
	 * 通过表名查询数据库返回数据
	 * @param table
	 * @return
	 * @throws Exception
	 * @since 2016年4月27日 下午3:30:55
	 * @author xiaohu
	 * @version v1.0
	 */
	public Map<String, Object> sealData(String table)throws Exception{
		String tableConstantName = getTableConstantName(table);
		Map<String,Object> root = new HashMap<String,Object>();		//创建数据模型
		
		String className = getClassName(tableConstantName);
		List<Map<String, Object>> fieldList = new ArrayList<Map<String,Object>>();
		boolean hasDate = false;
		Map<String, Object> map = null;
		String pk = getPk(table);
		Map<String, Object> pkMap = new HashMap<String, Object>();
		for (Map<String, Object> col : getCols(table)){
			map = new HashMap<String, Object>();
			//时间类型
			if("VARCHAR2".equals(col.get(TYPE).toString())){
				map.put("type", "VARCHAR");
			}else{
				map.put("type", col.get(TYPE).toString());
			}
			if(Class.forName(col.get(CLASS).toString()).isAssignableFrom(Date.class) || 
					Class.forName(col.get(CLASS).toString()) == Timestamp.class ||
					Class.forName(col.get(CLASS).toString()) == TIMESTAMP.class) {
				map.put("class", "Date");
				hasDate = true;
			}else if(Class.forName(col.get(CLASS).toString()) == OracleClob.class){
				map.put("class", "String");
			}else if(Class.forName(col.get(CLASS).toString()) == BigDecimal.class){
				map.put("class", col.get(TYPE).toString());
			}else{
				map.put("class", col.get(CLASS).toString().substring(col.get(CLASS).toString().lastIndexOf(".")+1));
			}
			map.put("column", col.get(NAME));
			String name = getClassName(col.get(NAME).toString());
			name = name.replaceFirst(name.substring(0,1),name.substring(0, 1).toLowerCase());
			map.put("name", name);
			map.put("comments", col.get("COMMENTS"));
			if(pk.equals(col.get(NAME).toString())){
				pkMap.put("name", name);
				pkMap.put("column", col.get(NAME));
			}
			fieldList.add(map);
		}
		
		
		String d = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date());
		root.put("tableName", table.toUpperCase());
		root.put("author", AUTHOR);
		root.put("rootPackage", ROOT_PACKAGE);
		root.put("className", className);
		root.put("fieldList", fieldList);
		root.put("hasDate", hasDate);
		root.put("date", d);
		root.put("pkMap", pkMap);
		return root;
	}
	
	
	/**
	 * 获取表的常量名，一般是在数据库建表的时候，写的注释..
	 * @param table
	 * @return
	 * @since 2016年4月18日 下午3:52:13
	 * @author xiaohu
	 * @version v1.0
	 */
	private String getTableConstantName(String table) {
		String tableConstantName = table.toUpperCase();
		for (String item : IGNORE_TABLE_PREFIX) {
			tableConstantName = tableConstantName.replaceAll("^" + item.toUpperCase(), "");
		}
		return tableConstantName;
	}
	
	
	/**
	 * 获取类的名
	 * @param name
	 * @return
	 * @since 2016年4月18日 下午3:51:54
	 * @author xiaohu
	 * @version v1.0
	 */
	private String getClassName(String name) {
		String[] names = name.split("_");
		StringBuilder sb = new StringBuilder();
		for (String n : names) {
			if(n.length() == 0) {
				sb.append("_");
			} else {
				sb.append(n.substring(0, 1).toUpperCase());
				if(n.length() > 1) {
					sb.append(n.substring(1).toLowerCase());
				}
			}
		}
		return sb.toString();
	}
	
	
	/**
	 * 获取表的列
	 * @param table
	 * @return
	 * @throws Exception
	 * @since 2016年4月18日 下午3:53:36
	 * @author xiaohu
	 * @version v1.0
	 */
	private List<Map<String, Object>> getCols(String table) throws Exception {
		List<Map<String, Object>> cols = new ArrayList<Map<String,Object>>();
		ResultSetMetaData md = (ResultSetMetaData) DBHelperUtils.query("select * from " + table + " where 1 = 2", null).getMetaData();
		String sql = "select a.COLUMN_NAME, b.COMMENTS  "
				+ " from all_tab_columns a left join all_col_comments b on a.table_name=b.table_name"
				+ " and a.COLUMN_NAME=b.COLUMN_NAME where a.table_name = '"+table.toUpperCase()+"'";
		Map<String, String> comments = new HashMap<String, String>();
		ResultSet rs = DBHelperUtils.query(sql, null);
		while(rs.next()){
			comments.put(rs.getString(1), rs.getString(2));
		}
		 for (int i = 1; i <= md.getColumnCount(); i++) {
			 Map<String, Object> col = new HashMap<String, Object>();
			 cols.add(col);
			 col.put(NAME, md.getColumnName(i));
			 col.put(CLASS, md.getColumnClassName(i));
			 col.put(SIZE, md.getColumnDisplaySize(i));
			 col.put("COMMENTS", comments.get(md.getColumnName(i)));
			 String _type = null;
			 String type = md.getColumnTypeName(i);
			 if(type.equals("NUMBER")) {
				
				if(md.getScale(i) == 2){
					col.put(CLASS, "java.lang.Double");
					 _type = "DOUBLE";
				}else{
					col.put(CLASS, "java.lang.Integer");
					_type = "INTEGER";
				}
				 
//			 }else if(type.equals("CLOB")){
				 
			 } else if(type.equals("DATETIME")) {
				 _type = "TIMESTAMP";
			 } else {
				 _type = type;
			 }
			 col.put(TYPE, _type);
		}
		return cols;
	}
	
	
	public String getPk(String tableName)throws Exception{
		List<Object> params = new ArrayList<Object>();
		String sql = "select COLUMN_NAME   from   user_cons_columns"
				+ " where constraint_name = (select constraint_name from user_constraints "
				+ " where table_name = ? and constraint_type ='P')";
		params.add(tableName.toUpperCase());
		ResultSet rs = DBHelperUtils.query(sql, params);
		String pk = "";
		while (rs.next()) {
			pk =rs.getString(1);
		}
		return pk;
	}
	
	
	/**
	 * 获取数据库表名
	 * @param tableName
	 * @return
	 * @throws Exception
	 * @since 2016年4月18日 下午4:07:09
	 * @author xiaohu
	 * @version v1.0
	 */
	public List<String> getTables(String tableName) throws Exception {
		List<Object> params = new ArrayList<Object>();
//		System.out.println("==========="+username);
		params.add(username.toUpperCase());
		params.add(tableName.toUpperCase());
		System.out.println("==========="+username);
		System.out.println("==========="+tableName);
		//ResultSet rs = JdbcUtils.query("select table_name from information_schema.tables where table_schema = ? and table_name =? order by table_name", params);
		ResultSet rs = DBHelperUtils.query("select TABLE_NAME  from all_tab_comments where owner =  ? and table_name =?", params);
		List<String> tables = new ArrayList<String>();

		while (rs.next()) {
			tables.add(rs.getString(1));		
		}
		if(tables.size() ==0){
			throw new Exception("你要映射的表没有找到！！！");
		}
		return tables;
	}
}
