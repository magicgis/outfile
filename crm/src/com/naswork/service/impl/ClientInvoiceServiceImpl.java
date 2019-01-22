package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ClientInvoiceDao;
import com.naswork.dao.ClientInvoiceElementDao;
import com.naswork.dao.ExportPackageElementDao;
import com.naswork.model.ClientInvoice;
import com.naswork.model.ClientInvoiceElement;
import com.naswork.module.finance.controller.clientinvoice.ClientInvoiceExcelVo;
import com.naswork.module.finance.controller.clientinvoice.ElementDataVo;
import com.naswork.module.finance.controller.clientinvoice.ListDataVo;
import com.naswork.module.storage.controller.exportpackage.ExportPackageElementVo;
import com.naswork.service.ClientInvoiceService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
@Service("clientInvoiceServiceImpl")
public class ClientInvoiceServiceImpl implements ClientInvoiceService {

	@Resource
	private ClientInvoiceDao clientInvoiceDao;
	@Resource
	private ClientInvoiceElementDao clientInvoiceElementDao;
	@Resource
	private ExportPackageElementDao exportPackageElementDao;
	
	@Override
	public void deleteByPrimaryKey(Integer id) {
		clientInvoiceDao.deleteByPrimaryKey(id);
	}

	@Override
	public void insert(ClientInvoice record) {
		clientInvoiceDao.insert(record);
	}

	@Override
	public void insertSelective(ClientInvoice record) {
		clientInvoiceDao.insertSelective(record);
	}

	@Override
	public ClientInvoice selectByPrimaryKey(Integer id) {
		return clientInvoiceDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(ClientInvoice record) {
		clientInvoiceDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public void updateByPrimaryKey(ClientInvoice record) {
		clientInvoiceDao.updateByPrimaryKey(record);
	}
	
	public ClientInvoiceExcelVo getMessage(Integer invoiceId){
		return clientInvoiceDao.getMessage(invoiceId);
	}
	
	public List<ClientInvoiceExcelVo> getEleMessage(Integer invoiceId){
		return clientInvoiceElementDao.getEleMessage(invoiceId);
	}


	@Override
	public void listDataPage(PageModel<ListDataVo> page, String searchString, GridSort sort) {
		page.put("where", searchString);
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<ListDataVo> list=clientInvoiceDao.listDataPage(page);
		page.setEntities(list);
		
	}

	@Override
	public ListDataVo findById(String id) {
		return clientInvoiceDao.findById(id);
	}

	@Override
	public void findByOrderNumber(PageModel<ListDataVo> page, String searchString, GridSort sort) {
		page.setEntities(clientInvoiceDao.findByOrderNumberPage(page));
	}

	@Override
	public void autoinsert(ClientInvoice record)throws Exception {
		clientInvoiceDao.autoinsert(record);
		 ClientInvoiceElement clientInvoiceElement=new ClientInvoiceElement();
		if(record.getInvoiceType().equals(0)){
		 List<ElementDataVo> list=clientInvoiceElementDao.findByCoidAndCiid(record.getClientOrderId().toString(), record.getId().toString());
		 for (ElementDataVo elementDataVo : list) {
			 clientInvoiceElement.setAmount(elementDataVo.getClientOrderAmount());
			 clientInvoiceElement.setTerms(record.getTerms());
			 clientInvoiceElement.setClientInvoiceId(record.getId());
			 clientInvoiceElement.setClientOrderElementId(elementDataVo.getClientOrderElementId());
			 clientInvoiceElementDao.insert(clientInvoiceElement);
		}
		
		}else{
			List<ExportPackageElementVo>  list=exportPackageElementDao.findByEpidAndCoid(record.getExportPackageId(), record.getClientOrderId().toString());
			 for (ExportPackageElementVo elementDataVo : list) {
				 clientInvoiceElement.setAmount(elementDataVo.getAmount());
				 clientInvoiceElement.setTerms(record.getTerms());
				 clientInvoiceElement.setClientInvoiceId(record.getId());
				 clientInvoiceElement.setClientOrderElementId(elementDataVo.getClientOrderElementId());
				 clientInvoiceElementDao.insert(clientInvoiceElement);
			}
		}
	}

	@Override
	public ClientInvoice selectByCode(String invoiceNumber) {
		return clientInvoiceDao.selectByCode(invoiceNumber);
	}

	@Override
	public  List<ClientInvoice> selectByclientOrderId(Integer clientOrderId,Integer type) {
		return clientInvoiceDao.selectByclientOrderId(clientOrderId,type);
	}

	@Override
	public List<ClientInvoice> selectByclientOrderElementId(Integer id) {
		return clientInvoiceDao.selectByclientOrderElementId(id);
	}

	@Override
	public List<ListDataVo> findExportNumber(Integer clientInvoiceId) {
		return clientInvoiceDao.findExportNumber(clientInvoiceId);
	}

	@Override
	public List<ClientInvoice> findByexportMunber(ListDataVo vo) {
		return clientInvoiceDao.findByexportMunber(vo);
	}
	
	public ClientInvoiceElement getTotalByCoId(Integer clientOrderId){
		return clientInvoiceElementDao.getTotalByCoId(clientOrderId);
	}
	
	public Integer getInvoiceIdByCoId(Integer clientOrderId){
		return clientInvoiceDao.getInvoiceIdByCoId(clientOrderId);
	}

}
