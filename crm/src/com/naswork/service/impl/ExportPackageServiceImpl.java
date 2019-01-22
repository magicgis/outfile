package com.naswork.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.naswork.dao.ClientDao;
import com.naswork.dao.ExchangeRateDao;
import com.naswork.dao.ExportPackageDao;
import com.naswork.dao.ExportPackageElementDao;
import com.naswork.dao.ExportPackageInstructionsDao;
import com.naswork.dao.SystemCodeDao;
import com.naswork.model.Client;
import com.naswork.model.ClientShip;
import com.naswork.model.ExchangeRate;
import com.naswork.model.ExportPackage;
import com.naswork.model.ExportPackageElement;
import com.naswork.model.ExportPackageInstructions;
import com.naswork.model.SystemCode;
import com.naswork.module.storage.controller.exportpackage.ExportPackageVo;
import com.naswork.service.ExportPackageElementService;
import com.naswork.service.ExportPackageService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

@Service("exportPackageService")
public class ExportPackageServiceImpl implements ExportPackageService {

	@Resource
	private ExportPackageDao exportPackageDao;
	@Resource
	private ClientDao clientDao;
	@Resource
	private ExchangeRateDao exchangeRateDao;
	@Resource
	private ExportPackageInstructionsDao exportPackageInstructionsDao;
	@Resource
	private SystemCodeDao systemCodeDao;
	@Resource
	private ExportPackageElementDao exportPackageElementDao;
	@Resource
	private ExportPackageElementService exportPackageElementService;
	
	@Override
	public int insertSelective(ExportPackage record) {
		Integer maxSeqInteger = exportPackageDao.getMaxSeq(record);
		Integer nextSeq = null;
		if (maxSeqInteger!=null) {
			nextSeq = maxSeqInteger+1;
		}else {
			nextSeq = 1;
		}
		if (record.getExportPackageInstructionsNumber()!=null && !"".equals(record.getExportPackageInstructionsNumber())) {
			ExportPackageInstructions exportPackageInstructions = exportPackageInstructionsDao.findByNumber(record.getExportPackageInstructionsNumber());
			record.setExportPackageInstructionsId(exportPackageInstructions.getId());
		}
		if (record.getFeeCurrencyId() != null) {
			ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(record.getFeeCurrencyId());
			record.setFeeRate(exchangeRate.getRate());
		}
		record.setSeq(nextSeq);
		ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(record.getCurrencyId());
		record.setExchangeRate(exchangeRate.getRate());
		String exportNumber = generateExportNumber(record);
		record.setExportNumber(exportNumber);
		record.setUpdateTimestamp(new Date());
		return exportPackageDao.insertSelective(record);
	}

	@Override
	public ExportPackage selectByPrimaryKey(Integer id) {
		return exportPackageDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ExportPackage record) {
		if (record.getFeeCurrencyId() != null) {
			ExportPackage exportPackage = exportPackageDao.selectByPrimaryKey(record.getId());
			if (exportPackage.getFeeCurrencyId() != null) {
				if (!exportPackage.getFeeCurrencyId().equals(record.getFeeCurrencyId())) {
					ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(record.getFeeCurrencyId());
					record.setFeeRate(exchangeRate.getRate());
				}
			}else {
				ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(record.getFeeCurrencyId());
				record.setFeeRate(exchangeRate.getRate());
			}
		}
		return exportPackageDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public void listPage(PageModel<ExportPackageVo> page,String where,GridSort sort) {
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(exportPackageDao.listPage(page));
	}
	
	public String generateExportNumber(ExportPackage exportPackage) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(outputDate(exportPackage.getExportDate(),
				"yyyyMMdd"));
		Client client = clientDao.selectByPrimaryKey(exportPackage.getClientId());
		buffer.append(client.getCode());
		buffer.append(StringUtils.leftPad(exportPackage.getSeq()
				.toString(), 3, '0'));
		return buffer.toString();
	}
	
	public static String outputDate(Date value, String pattern) {
		return value == null ? "" : FastDateFormat.getInstance(pattern).format(
				value);
	}

	@Override
	public ExportPackage findByCidAndexportDate(ExportPackageVo exportPackageVo) {
		return exportPackageDao.findByCidAndexportDate(exportPackageVo);
	}
	
    public Client getByExportPackageInstructionsNumber(String exportPackageInstructionsNumber){
    	return clientDao.getByExportPackageInstructionsNumber(exportPackageInstructionsNumber);
    }
    
	public Integer findByExportPackageInstructionsNumber(String exportPackageInstructionsNumber){
		return systemCodeDao.findByExportPackageInstructionsNumber(exportPackageInstructionsNumber);
	}
	
	public boolean createInvoice(ClientShip clientShip){
		try {
			List<ExportPackageElement> list = exportPackageElementDao.selectByExportId(clientShip.getExportPackageId());
			for (ExportPackageElement exportPackageElement : list) {
				exportPackageElementService.checkExportPackageForCom(exportPackageElement, clientShip.getId());
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public List<ExportPackage> getByIpeId(Integer importPackageElementId){
		return exportPackageDao.getByIpeId(importPackageElementId);
	}
	
	public ExportPackage getToTalAmount(Integer exportPackageId){
		return exportPackageDao.getToTalAmount(exportPackageId);
	}
	
}
