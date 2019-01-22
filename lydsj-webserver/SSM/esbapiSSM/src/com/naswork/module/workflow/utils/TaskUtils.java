/**
 * 
 */
package com.naswork.module.workflow.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.naswork.utils.Freemarker;
import com.naswork.utils.GetPinyin;

/**
 * @since 2016年4月21日 下午2:14:21
 * @author chengj<chengj@everygold.com>
 * @version v1.0
 */
public class TaskUtils {
	
	/**
	 * 产生流程图中的节点form及controller方法
	 * 
	 * @param file
	 * @since 2016年4月21日 下午2:16:28
	 * @author chengj<chengj@everygold.com>
	 * @version v1.0
	 * @throws DocumentException
	 */
	public static void formValueAndControllerGenerator(String filePath)
			throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		SAXReader reader = new SAXReader();
		File xmlfile = new File(filePath);
		Document document = reader.read(xmlfile);
		Element root = document.getRootElement();
		String processName = root.attributeValue("name");
		String processKey = root.attributeValue("key");
		paramMap.put("processName", processName);
		String className = processName.replaceFirst(processName.substring(0,1),processName.substring(0, 1).toUpperCase());
		paramMap.put("className", className);
		paramMap.put("processKey", processKey);
		paramMap.put("now", new SimpleDateFormat("yyyy年MM月dd日 上午HH:mm:ss").format(new Date()));
		List<Element> childElements = root.elements();
		Set<String> methodNameSet = new HashSet<String>();
		List<Map<String, Object>> methodlist = new ArrayList<Map<String,Object>>();//存放方法中的参数
		for (Element child : childElements) {
			Map<String, Object> subparamMap = new HashMap<String, Object>();
			String name = child.getName();
			if(!name.equals("start") && !name.equals("end") && !name.equals("end-cancel") && !name.equals("end-error")){
				String nameAttr = child.attributeValue("name");
				subparamMap.put("methodDes", nameAttr);
				String namePinYin = GetPinyin.getPinYinHeadChar(nameAttr);
				subparamMap.put("methodName", namePinYin);
				String formValue = "/task/"+processName+"/"+namePinYin;
				subparamMap.put("methodUrl", "/"+namePinYin);
				if(methodNameSet.contains(formValue)){
					throw new Exception("存在会产生同名的中文节点名称");
				}
				methodNameSet.add(formValue);
				child.addAttribute(new QName("form"), formValue);
				methodlist.add(subparamMap);
			}
		}
		paramMap.put("methodlist", methodlist);
		saveDocument(document,filePath);//保存xml
		
		String javaName = className+"TaskController.java";
		String ftlPath = "com/naswork/module/workflow/utils/ftl";
		String controllerfilePath = xmlfile.getParent()+"\\";
		Freemarker.printFile("taskControllerTemplate.ftl", paramMap, javaName, controllerfilePath, ftlPath);
		System.out.println("over");
	}

	/**
	 * 保存xml
	 * @param doc
	 * @param path
	 * @throws IOException
	 * @since 2016年4月21日 下午2:55:31
	 * @author chengj<chengj@everygold.com>
	 * @version v1.0
	 */
	 public static void saveDocument(Document doc,String path) throws IOException{
		  OutputFormat format = OutputFormat.createPrettyPrint();
		  XMLWriter writer = new XMLWriter(new FileOutputStream(path),format);
		  writer.write(doc);
		  writer.close();
		 }
	
	/**
	 * @param args
	 * @since 2016年4月21日 下午2:14:21
	 * @author chengj<chengj@everygold.com>
	 * @version v1.0
	 */
	public static void main(String[] args) {
		try {
			formValueAndControllerGenerator("D:\\eclipse\\mzqx\\jbpm\\testjbpm.jpdl.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
