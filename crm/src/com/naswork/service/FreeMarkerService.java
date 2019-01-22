/**
 * 
 */
package com.naswork.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import freemarker.template.TemplateException;

/**
 * @since 2015-4-17 下午2:24:13
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public interface FreeMarkerService {

	/**
	 * 读取模板为字符串
	 * 
	 * @param templatePath
	 * @param model
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 * @since 2015-4-17 下午2:26:04
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public String generateStringFromTemplate(String templatePath,
			Map<String, String> model) throws IOException, TemplateException;

	/**
	 * 从模板导出word文档
	 * 
	 * @param fileName
	 * @param templatePath
	 * @param model
	 * @param response
	 * @since 2015-4-17 下午2:28:15
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 * @throws IOException
	 * @throws TemplateException
	 */
	public void exportWordFromModel(String fileName, String templatePath,
			Map<String, String> model, HttpServletResponse response)
			throws IOException, TemplateException;

	/**
	 * 根据模板生成文档
	 * @param fileName
	 * @param templatePath
	 * @param targetPath 为null时生成到tomcat临时目录
	 * @param model
	 * @throws IOException
	 * @throws TemplateException
	 * @since 2015-4-17 下午5:13:43
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void createWordFromModel(String fileName, String templatePath,
			String targetPath, Map<String, String> model) throws IOException,
			TemplateException;

	/**
	 * 根据模板生成Excel
	 * @param fileName
	 * @param templatePath
	 * @param model
	 * @param response
	 * @throws IOException
	 * @throws TemplateException
	 * @since 2015年11月20日 上午10:57:31
	 * @author likeli(mailto:keli.li@kosentech.com.cn)
	 * @version v1.0
	 */
	void exportExcelFromModel(String fileName, String templatePath,
			Map<String, Object> model, HttpServletResponse response)
			throws IOException, TemplateException;
}
