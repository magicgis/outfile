package com.naswork.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ElementDao;
import com.naswork.dao.ExchangeRateDao;
import com.naswork.dao.ExportPackageDao;
import com.naswork.dao.ImportPackageDao;
import com.naswork.dao.ImportStorageLocationListDao;
import com.naswork.dao.SupplierOrderElementDao;
import com.naswork.dao.SupplierQuoteDao;
import com.naswork.model.ClientContact;
import com.naswork.model.ExchangeRate;
import com.naswork.model.ExportPackage;
import com.naswork.model.ExportPackageElement;
import com.naswork.model.ImportPackage;
import com.naswork.model.ImportPackagePayment;
import com.naswork.model.ImportStorageLocationList;
import com.naswork.model.SupplierOrderElement;
import com.naswork.module.purchase.controller.importpackage.ImportPackageListVo;
import com.naswork.module.purchase.controller.importpackage.StorageFlowVo;
import com.naswork.module.purchase.controller.importpackage.SupplierVo;
import com.naswork.module.purchase.controller.supplierquote.SupplierQuoteVo;
import com.naswork.service.ExportPackageElementService;
import com.naswork.service.ImportPackageService;
import com.naswork.service.SupplierOrderElementService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
@Service("importPackageService")
public class ImportPackageServiceImpl implements ImportPackageService {

	@Resource
	private ImportPackageDao importPackageDao;
	@Resource
	private ElementDao elementDao ;
	@Resource
	private ExportPackageDao exportPackageDao;
	@Resource
	private ExchangeRateDao exchangeRateDao;
	@Resource
	private SupplierOrderElementDao supplierOrderElementDao;
	@Resource
	private ExportPackageElementService exportPackageElementService;
	@Resource
	private ImportStorageLocationListDao importStorageLocationListDao;
	
	
	@Override
	public void findListDatePage(PageModel<ImportPackageListVo> page, String searchString, GridSort sort) {
		page.put("where", searchString);
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		List<ImportPackageListVo> importPackageList=importPackageDao.findListDatePage(page);
		page.setEntities(importPackageList);
		
	}

	@Override
	public void updateByPrimaryKey(ImportPackage record) {
		ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(record.getCurrencyId());
		record.setExchangeRate(exchangeRate.getRate());
		if (record.getFeeCurrencyId() != null) {
			ImportPackage importPackage = importPackageDao.selectByPrimaryKey(record.getId());
			if (importPackage.getFeeCurrencyId() != null) {
				if (!importPackage.getFeeCurrencyId().equals(record.getFeeCurrencyId())) {
					ExchangeRate feeRate = exchangeRateDao.selectByPrimaryKey(record.getFeeCurrencyId());
					record.setFeeRate(feeRate.getRate());
				}
			}else {
				ExchangeRate feeRate = exchangeRateDao.selectByPrimaryKey(record.getFeeCurrencyId());
				record.setFeeRate(feeRate.getRate());
			}
		}
		importPackageDao.updateByPrimaryKey(record);
	}

	@Override
	public Integer findmaxseq(ImportPackage importPackage) {
		
		Integer seq=importPackageDao.findmaxseq(importPackage);
//		Integer maxSeq=0;
//		if(seq==0){
//			maxSeq=seq;
//		}
//		else{
//			maxSeq=seq;
//		}
		return seq+1;
	}

	@Override
	public List<SupplierVo> findsupplier(Integer supplierid) {
		return importPackageDao.findsupplier(supplierid);
	}

	@Override
	public void insert(ImportPackage record) {
		List<SupplierOrderElement> list = supplierOrderElementDao.getSupplierElement(record.getLogisticsNo());
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setImportStatus(1);
			supplierOrderElementDao.updateByPrimaryKeySelective(list.get(i));
		}
		ExchangeRate exchangeRate = exchangeRateDao.selectByPrimaryKey(record.getCurrencyId());
		record.setExchangeRate(exchangeRate.getRate());
		if (record.getFeeCurrencyId() != null) {
			ExchangeRate feeRate = exchangeRateDao.selectByPrimaryKey(record.getFeeCurrencyId());
			record.setFeeRate(feeRate.getRate());
		}
		importPackageDao.insert(record);
	}

	@Override
	public List<ImportPackageListVo> findImportPackageDate(String id) {
		return importPackageDao.findImportPackageDate(id);
	}
	
	@Override
	public List<ImportPackageListVo> findOriginalNumber(Integer originalNumber) {
		return importPackageDao.findOriginalNumber(originalNumber);
	}
	
	 public int getOriginalNumber(Date importDate) {
		int prefix = getOriginalNumberPrefix(importDate);
		int onStart = prefix * 100;
		int onEnd = prefix * 100 + 100;
		int max = importPackageDao.findMaxOriginalNumber(onStart, onEnd);
		int sum =max+1;
		if(sum==onEnd){
			 onStart = prefix * 1000;
			 onEnd = prefix * 1000 + 1000;
			 max = importPackageDao.findMaxOriginalNumber(onStart, onEnd);
		}
		int originalNumber;
		if (max > 0) {
			originalNumber = max + 1;
		} else {
			originalNumber = onStart + 1;
		}
		return originalNumber;
	}
	
	public int getOriginalNumberPrefix(Date importDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(importDate);
		int year = calendar.get(Calendar.YEAR) % 100;
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int prefix = year * 10000 + month * 100 + day;
		return prefix;
	}

	@Override
	public ImportPackage selectByPrimaryKey(Integer id) {
		return importPackageDao.selectByPrimaryKey(id);
	}

	@Override
	public List<ClientContact> findclient(Integer clientid) {
		return importPackageDao.findclient(clientid);
	}

	@Override
	public Integer getMaxSeq(ExportPackage exportPackage) {
		Integer seq=exportPackageDao.getMaxSeq(exportPackage);
		int maxSeq;
		if(null==seq){
			maxSeq = 0;
		}else{
			maxSeq=seq;
		}
		return maxSeq+1;
	}

	
	public void updateStatusByPrimaryKey(ImportPackage importPackage){
		importPackageDao.updateStatusByPrimaryKey(importPackage);
	}
	
	public ImportPackage getFeeMessage(Integer importPackageId){
		return importPackageDao.getFeeMessage(importPackageId);
	}
	
}
