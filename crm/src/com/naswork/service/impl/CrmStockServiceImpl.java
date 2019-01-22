package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.CrmStockDao;
import com.naswork.model.CrmStock;
import com.naswork.model.TPart;
import com.naswork.service.CrmStockService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
@Service("crmStockService")
public class CrmStockServiceImpl implements CrmStockService {
	@Resource
	private CrmStockDao crmStockDao;
	
	@Override
	public void findStockPage(PageModel<CrmStock> page, String searchString, GridSort sort) {
		String buffer = null;
		if(null!=page.getString("partNumber")&&!"".equals(page.getString("partNumber"))){
			String partNumberCode=getCodeFromPartNumber(page.getString("partNumber"));
			if("cn".equals(page.getString("check"))){
				 buffer =" t.short_part_num LIKE "+"'%"+partNumberCode+"%'"; 
			}else if ("eq".equals(page.getString("check")) || "".equals(page.getString("check"))) {
				buffer =" t.short_part_num = "+"'"+partNumberCode+"'";
			}else if ("rcn".equals(page.getString("check"))) {
				buffer =" t.short_part_num = "+"'"+partNumberCode+"%'";
			}else if ("ecn".equals(page.getString("check"))) {
				buffer =" t.short_part_num = "+"'%"+partNumberCode+"'";
			}
			 if(null!=searchString&&!"".equals(searchString)){
				 searchString=buffer+" and "+searchString;
			 }else{
				 searchString=buffer;
			 }
		}
		
		page.put("where", searchString);
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<CrmStock> stock=crmStockDao.findStockPage(page);
		page.setEntities(stock);
		
	}
	
	@Override
	public void getDataOnce(PageModel<CrmStock> page, String searchString, GridSort sort) {
		String buffer = null;
		if(null!=page.getString("partNumber")&&!"".equals(page.getString("partNumber"))){
			String partNumberCode=getCodeFromPartNumber(page.getString("partNumber"));
			if("cn".equals(page.getString("check"))){
				 buffer =" t.short_part_num LIKE "+"'%"+partNumberCode+"%'"; 
			}else if ("eq".equals(page.getString("check")) || "".equals(page.getString("check"))) {
				buffer =" t.short_part_num = "+"'"+partNumberCode+"'";
			}else if ("rcn".equals(page.getString("check"))) {
				buffer =" t.short_part_num = "+"'"+partNumberCode+"%'";
			}else if ("ecn".equals(page.getString("check"))) {
				buffer =" t.short_part_num = "+"'%"+partNumberCode+"'";
			}
			 if(null!=searchString&&!"".equals(searchString)){
				 searchString=buffer+" and "+searchString;
			 }else{
				 searchString=buffer;
			 }
		}
		
		page.put("where", searchString);
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<CrmStock> stock=crmStockDao.getDataOnce(page);
		page.setEntities(stock);
		
	}
	
	@Override
	public void findCagePage(PageModel<CrmStock> page, String searchString, GridSort sort) {
		page.put("where", searchString);
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<CrmStock> stock=crmStockDao.findCagePage(page);
		page.setEntities(stock);
		
	}
	
	public static String getCodeFromPartNumber(String partNumber) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < partNumber.length(); i++) {
			char ch = partNumber.charAt(i);
			/*if (isValidCharacter(ch)) {
				buffer.append(Character.toUpperCase(ch));
			}*/
			String regex = "[a-z0-9A-Z\u4e00-\u9fa5]";//其他需要，直接修改正则表达式就好
			if (String.valueOf(ch).matches(regex)) {
				buffer.append(String.valueOf(ch));
			}
		}
		return buffer.toString().replaceAll("Ω", "");
	}
	
	public static boolean isValidCharacter(char ch) {
		return Character.isLetterOrDigit(ch);
	}


}
