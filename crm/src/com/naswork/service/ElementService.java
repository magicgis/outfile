package com.naswork.service;

import com.naswork.model.Element;

public interface ElementService {

	 public Element findIdByPn(String partNumber);
	 
	 public String getCodeFromPartNumber(String partNumber);
	 
	 public boolean isValidCharacter(char ch);
	 
	 public void insert(Element element);
}
