/**
 * 
 */
package com.naswork.common.constants;

/**
 * @since 2016年5月5日 下午3:33:46
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class FileConstant {
	
	//public static String UPLOAD_REALPATH = "C:"; //for windows
	public static String UPLOAD_REALPATH = "/home/lensadmin";  //for linux
	public static final String UPLOAD_FOLDER = "/mis/upload";
	public static final String EXCEL_FOLDER = "/mis/excel";
	public static final String EXCEL_BACKUP = "/mis/backup";
	public static final String EXCEL_EMAIL = "/mis/email";
	
	/**
	 * 下载方法
	 */
	public static final String DOWNLOAD_FUNCTION = "/fj/download/";
	
	
	public static final String YW_TABLE_NAME = "ywTableName";
	public static final String YW_TABLE_PK_NAME = "ywTablePkName";
	public static final String YW_ID = "ywId";
	
	static {
		if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1) {
			UPLOAD_REALPATH = "D:/CRM/Files/";
		}
	}
}
