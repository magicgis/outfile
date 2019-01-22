package com.naswork.utils.excel;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component("excelGeneratorMapConstant")
@Lazy(false)
public class ExcelGeneratorMapConstant {
	
	@Resource
	private SampleExcelGenerator sampleExcelGenerator;
	@Resource
	private QuoteExcelGenerator1 quoteExcelGenerator1;
	@Resource
	private QuoteExcelGenerator2 quoteExcelGenerator2;
	@Resource
	private QuoteExcelGenerator3 quoteExcelGenerator3;
	@Resource
	private SupplierInuquiryExcelGenerator supplierInuquiryExcelGenerator;
	@Resource	
	private OrderProfitStatementExcelGenerator orderProfitStatementExcelGenerator;
	@Resource	
	private QuoteProfitStatementExcelGenerator quoteProfitStatementExcelGenerator;
	@Resource
	private SupplierOrderExcelGenerator supplierOrderExcelGenerator;
	@Resource 
	private ClientInquiryExcelGenerator clientInquiryExcelGenerator;
	@Resource
	private ExportPackageExcelGenerator exportPackageExcelGenerator;
	@Resource
	private ImportpackageExcelGenerator importpackageExcelGenerator;
	@Resource
	private CertificationExcelGenerator certificationExcelGenerator;
	@Resource
	private ClientShipExcelGenerator clientShipExcelGenerator;
	@Resource
	private PendingExcelGenerator pendingExcelGenerator;
	@Resource
	private SupplierArrivalListExcelGenerator supplierArrivalListExcelGenerator;
	@Resource
	private SupplierWeatherQuoteExcelGenerator supplierWeatherQuoteExcelGenerator;
	@Resource
	private ClientOrderExcelGenerator clientOrderExcelGenerator;
	@Resource
	private ClientInvoiceExcelGenerator clientInvoiceExcelGenerator;
	@Resource
	private SupplierAirTypeInuquiryExcelGenerator supplierAirTypeInuquiryExcelGenerator;
	@Resource
	private PurchaseApplicationFormExcelGenerator purchaseApplicationFormExcelGenerator;
	@Resource
	private ClientSupplierInuquiryExcelGenerator clientSupplierInuquiryExcelGenerator;
	@Resource
	private ProfitStatementExcelGenerator profitStatementExcelGenerator;
	@Resource
	private LotsSupplierInuquiryExcelGenerator lotsSupplierInuquiryExcelGenerator;
	@Resource
	private PartinformationExcelGenerator partinformationExcelGenerator;
	@Resource
	private SupplierWeatherOrderExcelGenerator supplierWeatherOrderExcelGenerator;
	@Resource
	private ImportConditionExcelGenerator importConditionExcelGenerator;
	@Resource
	private CaacExcelGenerator caacExcelGenerator;
	@Resource
	private ClientOrderFinalExcelGenerator clientOrderFinalExcelGenerator;
	@Resource
	private ClientOrderSupplierQuoteExcelGenerator clientOrderSupplierQuoteExcelGenerator;
	@Resource
	private UpdateSupplierWeatherOrderExcelGenerator updateSupplierWeatherOrderExcelGenerator;
	@Resource
	private ClientWeatherOrderExcelGenerator clientWeatherOrderExcelGenerator;
	@Resource
	private LiluExcelGenerator liluExcelGenerator;
	@Resource
	private LotClientSupplierInuquiryExcelGenerator lotClientSupplierInuquiryExcelGenerator;
	@Resource
	private LotImportPackageElementExcelGenerator lotImportPackageElementExcelGenerator;
	@Resource
	private LotWeatherOrderElementExcelGenerator lotWeatherOrderElementExcelGenerator;
	@Resource
	private ImportPackgePaymentExcelGenerator importPackgePaymentExcelGenerator;
	@Resource
	private LotSupplierUnfinishElementExcelGenerator lotSupplierUnfinishElementExcelGenerator;
	@Resource
	private LotCertificationExcelGenerator lotCertificationExcelGenerator;
	@Resource
	private SupplierWeatherQuoteExcelGeneratorForCommission supplierWeatherQuoteExcelGeneratorForCommission;
	@Resource
	private StockMarketExcelGenerator stockMarketExcelGenerator;
	@Resource
	private AssetPackageForLocalExcelGenerator assetPackageForLocalExcelGenerator;

	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	public static Map<String,ExcelGeneratorBase> EXCEL_GENERATOR_MAP = new HashMap<String,ExcelGeneratorBase>();
	public void init(){
		logger.info("开始初始化 Excel生成器...");
		initData();
		logger.info("初始化Excel生成器完成");
	}
	
	private void initData() {
		EXCEL_GENERATOR_MAP.clear();
		EXCEL_GENERATOR_MAP.put(sampleExcelGenerator.fetchMappingKey(), sampleExcelGenerator);
		EXCEL_GENERATOR_MAP.put(quoteExcelGenerator1.fetchMappingKey(), quoteExcelGenerator1);
		EXCEL_GENERATOR_MAP.put(quoteExcelGenerator2.fetchMappingKey(), quoteExcelGenerator2);
		EXCEL_GENERATOR_MAP.put(quoteExcelGenerator3.fetchMappingKey(), quoteExcelGenerator3);
		EXCEL_GENERATOR_MAP.put(orderProfitStatementExcelGenerator.fetchMappingKey(), orderProfitStatementExcelGenerator);
		EXCEL_GENERATOR_MAP.put(quoteProfitStatementExcelGenerator.fetchMappingKey(), quoteProfitStatementExcelGenerator);
		EXCEL_GENERATOR_MAP.put(supplierInuquiryExcelGenerator.fetchMappingKey(), supplierInuquiryExcelGenerator);
		EXCEL_GENERATOR_MAP.put(supplierOrderExcelGenerator.fetchMappingKey(), supplierOrderExcelGenerator);
		EXCEL_GENERATOR_MAP.put(clientInquiryExcelGenerator.fetchMappingKey(), clientInquiryExcelGenerator);
		EXCEL_GENERATOR_MAP.put(exportPackageExcelGenerator.fetchMappingKey(), exportPackageExcelGenerator);
		EXCEL_GENERATOR_MAP.put(importpackageExcelGenerator.fetchMappingKey(), importpackageExcelGenerator);
		EXCEL_GENERATOR_MAP.put(certificationExcelGenerator.fetchMappingKey(), certificationExcelGenerator);
		EXCEL_GENERATOR_MAP.put(clientShipExcelGenerator.fetchMappingKey(), clientShipExcelGenerator);
		EXCEL_GENERATOR_MAP.put(pendingExcelGenerator.fetchMappingKey(), pendingExcelGenerator);
		EXCEL_GENERATOR_MAP.put(supplierArrivalListExcelGenerator.fetchMappingKey(), supplierArrivalListExcelGenerator);
		EXCEL_GENERATOR_MAP.put(supplierWeatherQuoteExcelGenerator.fetchMappingKey(), supplierWeatherQuoteExcelGenerator);
		EXCEL_GENERATOR_MAP.put(clientOrderExcelGenerator.fetchMappingKey(), clientOrderExcelGenerator);
		EXCEL_GENERATOR_MAP.put(clientInvoiceExcelGenerator.fetchMappingKey(), clientInvoiceExcelGenerator);
		EXCEL_GENERATOR_MAP.put(supplierAirTypeInuquiryExcelGenerator.fetchMappingKey(), supplierAirTypeInuquiryExcelGenerator);
		EXCEL_GENERATOR_MAP.put(purchaseApplicationFormExcelGenerator.fetchMappingKey(), purchaseApplicationFormExcelGenerator);
		EXCEL_GENERATOR_MAP.put(clientSupplierInuquiryExcelGenerator.fetchMappingKey(), clientSupplierInuquiryExcelGenerator);
		EXCEL_GENERATOR_MAP.put(profitStatementExcelGenerator.fetchMappingKey(), profitStatementExcelGenerator);
		EXCEL_GENERATOR_MAP.put(lotsSupplierInuquiryExcelGenerator.fetchMappingKey(), lotsSupplierInuquiryExcelGenerator);
		EXCEL_GENERATOR_MAP.put(partinformationExcelGenerator.fetchMappingKey(), partinformationExcelGenerator);
		EXCEL_GENERATOR_MAP.put(supplierWeatherOrderExcelGenerator.fetchMappingKey(), supplierWeatherOrderExcelGenerator);
		EXCEL_GENERATOR_MAP.put(importConditionExcelGenerator.fetchMappingKey(), importConditionExcelGenerator);
		EXCEL_GENERATOR_MAP.put(caacExcelGenerator.fetchMappingKey(), caacExcelGenerator);
		EXCEL_GENERATOR_MAP.put(clientOrderFinalExcelGenerator.fetchMappingKey(), clientOrderFinalExcelGenerator);
		EXCEL_GENERATOR_MAP.put(clientOrderSupplierQuoteExcelGenerator.fetchMappingKey(), clientOrderSupplierQuoteExcelGenerator);
		EXCEL_GENERATOR_MAP.put(updateSupplierWeatherOrderExcelGenerator.fetchMappingKey(), updateSupplierWeatherOrderExcelGenerator);
		EXCEL_GENERATOR_MAP.put(clientWeatherOrderExcelGenerator.fetchMappingKey(), clientWeatherOrderExcelGenerator);
		EXCEL_GENERATOR_MAP.put(liluExcelGenerator.fetchMappingKey(), liluExcelGenerator);
		EXCEL_GENERATOR_MAP.put(lotClientSupplierInuquiryExcelGenerator.fetchMappingKey(), lotClientSupplierInuquiryExcelGenerator);
		EXCEL_GENERATOR_MAP.put(lotImportPackageElementExcelGenerator.fetchMappingKey(), lotImportPackageElementExcelGenerator);
		EXCEL_GENERATOR_MAP.put(lotWeatherOrderElementExcelGenerator.fetchMappingKey(), lotWeatherOrderElementExcelGenerator);
		EXCEL_GENERATOR_MAP.put(importPackgePaymentExcelGenerator.fetchMappingKey(), importPackgePaymentExcelGenerator);
		EXCEL_GENERATOR_MAP.put(lotSupplierUnfinishElementExcelGenerator.fetchMappingKey(), lotSupplierUnfinishElementExcelGenerator);
		EXCEL_GENERATOR_MAP.put(lotCertificationExcelGenerator.fetchMappingKey(), lotCertificationExcelGenerator);
		EXCEL_GENERATOR_MAP.put(supplierWeatherQuoteExcelGeneratorForCommission.fetchMappingKey(), supplierWeatherQuoteExcelGeneratorForCommission);
		EXCEL_GENERATOR_MAP.put(stockMarketExcelGenerator.fetchMappingKey(), stockMarketExcelGenerator);
		EXCEL_GENERATOR_MAP.put(assetPackageForLocalExcelGenerator.fetchMappingKey(), assetPackageForLocalExcelGenerator);
	}
		
}
