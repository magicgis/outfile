/**
 * 
 */
package com.naswork.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import jxl.read.biff.BiffException;

import org.springframework.web.multipart.MultipartFile;

import com.naswork.vo.UploadFileVo;

/**
 * 文件
 * @since 2016年5月5日 上午9:53:14
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public interface FileService {
	
	/**
	 * 导入xls文件
	 * @param filePath
	 * @return
	 * @since 2016年5月5日 上午10:10:00
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 * @throws IOException 
	 * @throws BiffException 
	 * @throws FileNotFoundException 
	 */
	public List<Map<String, String>> importXls(File xlsFile) throws FileNotFoundException, BiffException, IOException;
	
	
	/**
	 * 上传文件
	 * @param multipartFile
	 * @return
	 * @since 2015-4-20 下午3:31:40
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 * @param contextPath 
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws Exception 
	 */
	public UploadFileVo uploadFile(Map<String, String[]> paramap, MultipartFile multipartFile) throws IllegalStateException, IOException, Exception;


	/**
	 * 删除文件
	 * @param filePath
	 * @since 2016年5月10日 下午7:09:08
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void deleteFile(String filePath);
	
}
