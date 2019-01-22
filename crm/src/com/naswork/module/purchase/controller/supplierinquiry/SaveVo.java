package com.naswork.module.purchase.controller.supplierinquiry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.naswork.model.SupplierContact;
import com.naswork.module.marketing.controller.clientinquiry.ElementVo;


public class SaveVo {

	private List<ElementVo> listElementVo;
	
	private List<SupplierVo> listSupplierVo;
	
	private HashMap<String, ElementVo> mapElememtVo;
	
	private HashMap<String, SupplierVo> mapSupllierVo;
	
	private Integer inquiryElementCount;
	
	private Integer quoteElementCount;
	
	private List<SupplierContact> list;

	public List<ElementVo> getListElementVo() {
		return listElementVo;
	}

	public void setListElementVo(List<ElementVo> listElementVo) {
		this.listElementVo = listElementVo;
	}

	public List<SupplierVo> getListSupplierVo() {
		return listSupplierVo;
	}

	public void setListSupplierVo(List<SupplierVo> listSupplierVo) {
		this.listSupplierVo = listSupplierVo;
	}

	public Map<String, ElementVo> getMapElememtVo() {
		return mapElememtVo;
	}

	public void setMapElememtVo(HashMap<String, ElementVo> mapElememtVo) {
		this.mapElememtVo = mapElememtVo;
	}

	public Map<String, SupplierVo> getMapSupllierVo() {
		return mapSupllierVo;
	}

	public void setMapSupllierVo(HashMap<String, SupplierVo> mapSupllierVo) {
		this.mapSupllierVo = mapSupllierVo;
	}

	public Integer getInquiryElementCount() {
		return inquiryElementCount;
	}

	public void setInquiryElementCount(Integer inquiryElementCount) {
		this.inquiryElementCount = inquiryElementCount;
	}

	public Integer getQuoteElementCount() {
		return quoteElementCount;
	}

	public void setQuoteElementCount(Integer quoteElementCount) {
		this.quoteElementCount = quoteElementCount;
	}

	public List<SupplierContact> getList() {
		return list;
	}

	public void setList(List<SupplierContact> list) {
		this.list = list;
	}
	
}
