package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ElementDao;
import com.naswork.model.Element;
import com.naswork.service.ElementService;
@Service("elementService")
public class ElementServiceImpl implements ElementService {
	@Resource
	private ElementDao elementDao;
	@Override
	public Element findIdByPn(String partNumber) {
		String partNumberCode = getCodeFromPartNumber(partNumber);
		List<Element> elements=elementDao.findIdByPn(partNumberCode);
		if(elements.size()>0){
			return elementDao.findIdByPn(partNumberCode).get(0);
		}else{
			return null;
		}
		
	}
	
	public String getCodeFromPartNumber(String partNumber) {
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
		return buffer.toString();
	}
	
	public boolean isValidCharacter(char ch) {
		return Character.isLetterOrDigit(ch);
	}

	@Override
	public void insert(Element element) {
		elementDao.insert(element);
	}

}
