/**
 * 
 */
package com.naswork.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.naswork.service.FreeMarkerService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @since 2015-4-17 下午2:24:33
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
@Service("freeMarkerService")
public class FreeMarkerServiceImpl implements FreeMarkerService {

	@Resource(name = "freeMarkerConfigurer")
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Override
	public String generateStringFromTemplate(String templatePath,
			Map<String, String> model) throws IOException, TemplateException {
		Assert.hasText(templatePath, "模板不能为空");
		Configuration configuration = freeMarkerConfigurer
				.getConfiguration();
		Template template = configuration.getTemplate(templatePath);
		return FreeMarkerTemplateUtils.processTemplateIntoString(template,
				model);
	}

	@Override
	public void exportWordFromModel(String fileName, String templatePath,
			Map<String, String> model,HttpServletResponse response) throws IOException, TemplateException {
		Assert.hasText(templatePath, "模板不能为空");
		Configuration configuration = freeMarkerConfigurer
				.getConfiguration();
		Template template = configuration.getTemplate(templatePath);
		if(StringUtils.isEmpty(fileName)){
			fileName = System.currentTimeMillis()+RandomStringUtils.randomNumeric(5);
		}
		if(!fileName.endsWith(".doc"))fileName = fileName+".doc";
		//fileName=new String(fileName.getBytes("UTF-8"), "iso8859-1");update baif 15.10.26.14.30
		response.setHeader("Content-Disposition","attachment;filename="+ new String(fileName.getBytes("gb2312"), "iso8859-1"));  
		response.setContentType("application/msword");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		template.process(model, out);
		out.flush();
		out.close();
	}
	
	@Override
	public void exportExcelFromModel(String fileName, String templatePath,
			Map<String, Object> model,HttpServletResponse response) throws IOException, TemplateException {
		Assert.hasText(templatePath, "模板不能为空");
		Configuration configuration = freeMarkerConfigurer
				.getConfiguration();
		Template template = configuration.getTemplate(templatePath);
		if(StringUtils.isEmpty(fileName)){
			fileName = System.currentTimeMillis()+RandomStringUtils.randomNumeric(5);
		}
		if(!fileName.endsWith(".xls"))fileName = fileName+".xls";
		response.setHeader("Content-Disposition","attachment;filename="+ new String(fileName.getBytes("gb2312"), "iso8859-1"));  
		response.setContentType("application/msexcel");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		template.process(model, out);
		out.flush();
		out.close();
	}

	@Override
	public void createWordFromModel(String fileName, String templatePath,
			String targetPath, Map<String, String> model) throws IOException,
			TemplateException {
		Assert.hasText(templatePath, "模板不能为空");
		Configuration configuration = freeMarkerConfigurer
				.getConfiguration();
		Template template = configuration.getTemplate(templatePath);
		if(StringUtils.isEmpty(targetPath)){
			targetPath = System.getProperty("java.io.tmpdir")+"/report/"+fileName+".doc";
			File folder = new File(System.getProperty("java.io.tmpdir")+"/report/");
			if(!folder.exists()){
				folder.mkdirs();
			}
		}
		
		File folder = new File(targetPath);
		if(!folder.exists()){
			folder.mkdirs();
		}
		if(!targetPath.endsWith("/"))targetPath = targetPath + "/";
		if(StringUtils.isEmpty(fileName)){
			fileName = System.currentTimeMillis()+RandomStringUtils.randomNumeric(5);
		}
		if(!fileName.endsWith(".doc"))fileName = fileName+".doc";
		File outFile = new File(targetPath+fileName);
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
		template.process(model, out);
		out.flush();
		out.close();
	}
}
