package com.naswork.utils.excel;

import com.naswork.common.constants.FileConstant;
import com.naswork.dao.ArPricePartMappingDao;
import com.naswork.model.ArPricePartMapping;
import com.naswork.model.SupplierCommissionForStockmarket;
import com.naswork.module.storage.controller.assetpackage.ScfseVo;
import com.naswork.module.storage.controller.exportpackage.ExportPackageElementVo;
import com.naswork.service.ArPricePartMappingService;
import com.naswork.service.SupplierCommissionForStockmarketElementService;
import com.naswork.service.SupplierCommissionForStockmarketService;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.PageModel;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @Author: white
 * @Date: create in 2018-08-29 14:38
 * @Description: 用于资产包----统计数据加载
 * @Modify_By:
 */
@Service("assetPackageForLocalExcelGenerator")
public class AssetPackageForLocalExcelGenerator extends ExcelGeneratorBase {

    @Resource
    private SupplierCommissionForStockmarketElementService supplierCommissionForStockmarketElementService;
    @Resource
    private SupplierCommissionForStockmarketService supplierCommissionForStockmarketService;
    @Resource
    private ArPricePartMappingService arPricePartMappingService;
    @Resource
    private ArPricePartMappingDao arPricePartMappingDao;

    private static void copyCell(Cell oriCell, Cell newCell) {
        if (oriCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            newCell.setCellValue(oriCell.getBooleanCellValue());
        } else if (oriCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            newCell.setCellValue(oriCell.getNumericCellValue());
        } else if (oriCell.getCellType() == Cell.CELL_TYPE_STRING) {
            newCell.setCellValue(oriCell.getStringCellValue());
        } else if (oriCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            newCell.setCellFormula(oriCell.getCellFormula());
        }
        newCell.setCellStyle(oriCell.getCellStyle());
    }

    private static void createCell(Row row, int index, int numOfCell) {
        int lastCellNum = row.getLastCellNum();
        for (int dynamicIndex = 0; dynamicIndex < numOfCell; dynamicIndex++) {
            Cell oriCell = row.getCell(index + dynamicIndex);
            row.createCell(dynamicIndex + lastCellNum, oriCell.getCellType());
        }
        //int newLastCellNum = row.getLastCellNum();

        for (int dynamicIndex = lastCellNum - 1; dynamicIndex >= index; dynamicIndex--) {
            Cell oriCell = row.getCell(dynamicIndex);
            Cell newCell = row.getCell(dynamicIndex + numOfCell);
            copyCell(oriCell, newCell);
        }
    }

    public BigDecimal caculateProfitMargin(BigDecimal revenueRate) {
        return new BigDecimal(100.00).divide(
                new BigDecimal(100).subtract(revenueRate), 2,
                BigDecimal.ROUND_HALF_UP);
    }

    public static void testWrite(int beginColIndex, int dynamicColNum) throws FileNotFoundException, IOException {
        System.out.println("Begin to write");
        String fileName = "F:\\tmp\\1118113-B0704.xlsx";
        File file = new File(fileName);
        FileInputStream fs = new FileInputStream(file);
        XSSFWorkbook wb = new XSSFWorkbook(fs);
        Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        Row row = sheet.getRow(firstRowNum);
        createCell(row, beginColIndex, dynamicColNum);
        for (int dynamicIndex = 0; dynamicIndex < dynamicColNum; dynamicIndex++) {
            int cellnum = beginColIndex + dynamicIndex;
            Cell cell = row.getCell(cellnum);
            cell.setCellValue("HEADER" + String.valueOf(dynamicIndex));
        }
        for (int rowIndex = firstRowNum + 1; rowIndex <= lastRowNum; rowIndex++) {
            row = sheet.getRow(rowIndex);
            createCell(row, beginColIndex, dynamicColNum);
            for (int dynamicIndex = 0; dynamicIndex < dynamicColNum; dynamicIndex++) {
                int cellnum = beginColIndex + dynamicIndex;
                Cell cell = row.getCell(cellnum);
                cell.setCellValue(String.valueOf(rowIndex * 10 + dynamicIndex));
            }
        }
        String outputFileName = "F:\\tmp\\1118113-B0704_update.xlsx";
        File outputFile = new File(outputFileName);
        FileOutputStream fos = new FileOutputStream(outputFile);
        wb.write(fos);
        fos.flush();
        fos.close();

        System.out.println("Write Complete");
    }


    @Override
    protected String fetchMappingKey() {
        return "AssetPackageExcel";
    }

    @Override
    protected String fetchTemplateFileName(String currencyId) {
        return File.separator + "exceltemplate" + File.separator + "assetpackageforlocal.xlsx";
    }

    @Override
    protected String fetchOutputFilePath() {
        return FileConstant.UPLOAD_REALPATH + File.separator + FileConstant.EXCEL_FOLDER + File.separator + "sampleoutput";
    }

    @Override
    protected String fetchOutputFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date now = new Date();

        return "AssetPackage" + "_" + this.fetchUserName() + "_" + format.format(now);
    }

    @Override
    protected String fetchYwTableName() {
        return "supplier_commission_for_stockmarket";
    }

    @Override
    protected String fetchYwTablePkName() {
        return "id";
    }

    /**
     * @Author: Create by white
     * @Description: 将数据库查询到数据 写到excel
     * @Date: 2018-08-29 15:00
     * @Params: [wb, ywId]
     * @Return: void
     * @Throws:
     */
    @Override
    protected void writeData(POIExcelWorkBook wb, String ywId) {
        SupplierCommissionForStockmarket supplierCommissionForStockmarket = supplierCommissionForStockmarketService.selectByPrimaryKey(Integer.parseInt(ywId));
        PageModel<ScfseVo> page = new PageModel<ScfseVo>();
        if (supplierCommissionForStockmarket.getClientInquiryId() != null) {
            page.put("ciid", supplierCommissionForStockmarket.getClientInquiryId());
        }
        page.put("ywId", ywId);
        List<ScfseVo> scfseVoList = supplierCommissionForStockmarketElementService.getListData(page);
        Sheet sheet = wb.getSheetAt(wb.getActiveSheetIndex());
        sheet.setForceFormulaRecalculation(true);
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();

        Row lastRow = sheet.getRow(lastRowNum);
        short firstCellNum = lastRow.getFirstCellNum();
        short lastCellNum = lastRow.getLastCellNum();
        String[] cellKeys = new String[lastCellNum - firstCellNum + 1];
        for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
            Cell cell = lastRow.getCell(cellnum);
            if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                String str = cell.getStringCellValue();
                if (str == null) {
                    continue;
                }
                if (str.startsWith("$")) {
                    String key = str.substring(1);
                    cellKeys[cellnum - firstCellNum] = key;
                }
            }
        }
        if (scfseVoList != null && scfseVoList.size() > 0) {
            for (int i = 0; i < scfseVoList.size(); i++) {
                ScfseVo scfseVo = scfseVoList.get(i);
                if(scfseVo.getPartNumber() == "" || scfseVo.getPartNumber().toLowerCase() == "n/a"){
                    continue;
                }
                Row elementRow = sheet.createRow(lastRowNum + i);
                for (int cellnum = firstCellNum; cellnum <= lastCellNum; cellnum++) {
                    Cell cell = lastRow.getCell(cellnum);
                    Cell elementCell = elementRow.createCell(cellnum);
                    if (cell != null) {
//                        elementCell.setCellStyle(cell.getCellStyle());
                        if (!StringUtils.isEmpty(cellKeys[cellnum
                                - firstCellNum])) {
                            String key = cellKeys[cellnum - firstCellNum];
                            //Object value = null;
                            if (key.equals("NO")) {
                                this.write(elementCell, i + 1);
                            } else if (key.equals("ITEM")) {
                                Object item = scfseVo.getItem();
                                this.write(elementCell, item);
                            } else if (key.equals("PART_NUMBER")) {
                                Object partNumber = scfseVo.getPartNumber();
                                this.write(elementCell, partNumber);
                            }else if(key.equals("ALT_PN")){
                                Object alt = scfseVo.getAlt();
                                this.write(elementCell,alt);
                            }
                            else if (key.equals("DESCRIPTION")) {
                                Object description = scfseVo.getDescription();
                                this.write(elementCell, description);
                            } else if (key.equals("SERIAL_NUMBER")) {
                                Object serialNumber = scfseVo.getSerialNumber();
                                this.write(elementCell, serialNumber);
                            } else if (key.equals("DOM")) {
                                Object dom = scfseVo.getDom();
                                this.write(elementCell, dom);
                            } else if (key.equals("QTY")) {
                                Object amount = scfseVo.getAmount();
                                this.write(elementCell, amount);
                            } else if (key.equals("CONDITION")) {
                                Object conditionValue = scfseVo.getConditionValue();
                                this.write(elementCell, conditionValue);
                            }else if(key.equals("TSN")){
                                Object tsn = scfseVo.getTsn();
                                this.write(elementCell, tsn);
                            }else if(key.equals("CSN")){
                                Object csn = scfseVo.getCsn();
                                this.write(elementCell, csn);
                            }else if(key.equals("ATA")){
                                Object ata = scfseVo.getAta();
                                this.write(elementCell, ata);
                            }
                            else if (key.equals("MANUFACTURER")) {
                                Object manufacturer = scfseVo.getManufacturer();
                                this.write(elementCell, manufacturer);
                            }else if(key.equals("MAX")){
                                Object max = scfseVo.getMax();
                                this.write(elementCell,max);
                            }else if(key.equals("MIN")){
                                Object min = scfseVo.getMin();
                                this.write(elementCell,min);
                            }
                            else if (key.equals("IQUIRY_COUNT")) {
                                //询价次数
                                Object inquiryCount = scfseVo.getInquiryCount();
                                this.write(elementCell, inquiryCount);
                            } else if (key.equals("IQUIRY_AMOUNT")) {
                                //询价数量
                                Object inquiryAmount = scfseVo.getInquiryAmount();
                                this.write(elementCell, inquiryAmount);
                            } else if (key.equals("CLIENTCODE")) {
                                Object clientCode = scfseVo.getClientCode();
                                this.write(elementCell, clientCode);
                            } else if (key.equals("PRICES_OLD")) {
//                                Object pricesOld = scfseVo.getPricesOld();
                                String avgOld = "0";
                                List<Double> pricesOldList = new ArrayList<Double>();
                                if (scfseVo.getPricesOld() != null) {
                                	String[] PricesOldArr = scfseVo.getPricesOld().toString().split(",");
                                	for (int j = 0; j < PricesOldArr.length; j++) {
										if (PricesOldArr[j] != null && !"".equals(PricesOldArr[j])) {
											pricesOldList.add(new Double(PricesOldArr[j]));
										}
									}
								}
                                if (scfseVo.getPricesOldMain() != null) {
                                	String[] PricesOldArr = scfseVo.getPricesOldMain().toString().split(",");
                                	for (int j = 0; j < PricesOldArr.length; j++) {
										if (PricesOldArr[j] != null && !"".equals(PricesOldArr[j])) {
											pricesOldList.add(new Double(PricesOldArr[j]));
										}
									}
								}
                                if (scfseVo.getPricesOldAlter() != null) {
                                	String[] PricesOldArr = scfseVo.getPricesOldAlter().toString().split(",");
                                	for (int j = 0; j < PricesOldArr.length; j++) {
										if (PricesOldArr[j] != null && !"".equals(PricesOldArr[j])) {
											pricesOldList.add(new Double(PricesOldArr[j]));
										}
									}
								}
                                Collections.sort(pricesOldList);
                                if (pricesOldList.size() > 5) {
                                	Double sum = 0.0;
                                    for (int index = 1; index < pricesOldList.size() - 1; index++) {
                                        if (pricesOldList.get(index) != null && !"".equals(pricesOldList.get(index))) {
                                            sum = sum + new Double(pricesOldList.get(index));
                                        }
                                    }
                                    BigDecimal amount = new BigDecimal(sum);
                                    BigDecimal length = new BigDecimal(pricesOldList.size()-2);
                                    avgOld = amount.divide(length, 2, BigDecimal.ROUND_HALF_UP).toString();
                                } else if (pricesOldList.size() > 0) {
                                	Double sum = 0.0;
                                    for (int index = 0; index < pricesOldList.size(); index++) {
                                        if (pricesOldList.get(index) != null && !"".equals(pricesOldList.get(index))) {
                                            sum = sum + new Double(pricesOldList.get(index));
                                        }
                                    }
                                    BigDecimal amount = new BigDecimal(sum);
                                    BigDecimal length = new BigDecimal(pricesOldList.size());
                                    avgOld = amount.divide(length, 2, BigDecimal.ROUND_HALF_UP).toString();
                                }
                                /*if (pricesOld != null) {
                                    String[] PricesOldArr = pricesOld.toString().split(",");
                                    Double sum = 0.0;
                                    if (PricesOldArr.length > 5) {
                                        for (int index = 1; index < PricesOldArr.length - 1; index++) {
                                            if (PricesOldArr[index] != null && !"".equals(PricesOldArr[index])) {
                                                sum = sum + new Double(PricesOldArr[index]);
                                            }
                                        }
                                        BigDecimal amount = new BigDecimal(sum);
                                        BigDecimal length = new BigDecimal(PricesOldArr.length-2);
                                        avgOld = amount.divide(length, 2, BigDecimal.ROUND_HALF_UP).toString();
                                    } else {
                                        for (int index = 0; index < PricesOldArr.length; index++) {
                                            if (PricesOldArr[index] != null && !"".equals(PricesOldArr[index])) {
                                                sum = sum + new Double(PricesOldArr[index]);
                                            }
                                        }
                                        BigDecimal amount = new BigDecimal(sum);
                                        BigDecimal length = new BigDecimal(PricesOldArr.length);
                                        avgOld = amount.divide(length, 2, BigDecimal.ROUND_HALF_UP).toString();
                                    }
                                }*/
                                this.write(elementCell, avgOld);
                            } else if (key.equals("PRICES_NEW")) {
                            	String avgNew = "0";
                            	List<Double> pricesNewList = new ArrayList<Double>();
                                if (scfseVo.getPricesNew() != null) {
                                	String[] PricesArr = scfseVo.getPricesNew().toString().split(",");
                                	for (int j = 0; j < PricesArr.length; j++) {
										if (PricesArr[j] != null && !"".equals(PricesArr[j])) {
											pricesNewList.add(new Double(PricesArr[j]));
										}
									}
								}
                                if (scfseVo.getPricesNewMain() != null) {
                                	String[] PricesArr = scfseVo.getPricesNewMain().toString().split(",");
                                	for (int j = 0; j < PricesArr.length; j++) {
										if (PricesArr[j] != null && !"".equals(PricesArr[j])) {
											pricesNewList.add(new Double(PricesArr[j]));
										}
									}
								}
                                if (scfseVo.getPricesNewAlter() != null) {
                                	String[] PricesArr = scfseVo.getPricesNewAlter().toString().split(",");
                                	for (int j = 0; j < PricesArr.length; j++) {
										if (PricesArr[j] != null && !"".equals(PricesArr[j])) {
											pricesNewList.add(new Double(PricesArr[j]));
										}
									}
								}
                                Collections.sort(pricesNewList);
                                if (pricesNewList.size() > 5) {
                                	Double sum = 0.0;
                                    for (int index = 1; index < pricesNewList.size() - 1; index++) {
                                        if (pricesNewList.get(index) != null && !"".equals(pricesNewList.get(index))) {
                                            sum = sum + new Double(pricesNewList.get(index));
                                        }
                                    }
                                    BigDecimal amount = new BigDecimal(sum);
                                    BigDecimal length = new BigDecimal(pricesNewList.size()-2);
                                    avgNew = amount.divide(length, 2, BigDecimal.ROUND_HALF_UP).toString();
                                } else if (pricesNewList.size() > 0) {
                                	Double sum = 0.0;
                                    for (int index = 0; index < pricesNewList.size(); index++) {
                                        if (pricesNewList.get(index) != null && !"".equals(pricesNewList.get(index))) {
                                            sum = sum + new Double(pricesNewList.get(index));
                                        }
                                    }
                                    BigDecimal amount = new BigDecimal(sum);
                                    BigDecimal length = new BigDecimal(pricesNewList.size());
                                    avgNew = amount.divide(length, 2, BigDecimal.ROUND_HALF_UP).toString();
                                }
                                /*Object pricesNew = scfseVo.getPricesNew();
                                String avgNew = "0";
                                if (pricesNew != null) {
                                    String[] PricesNewArr = pricesNew.toString().split(",");
                                    Double sum = 0.0;
                                    if (PricesNewArr.length > 5) {
                                        for (int index = 1; index < PricesNewArr.length - 1; index++) {
                                            if (PricesNewArr[index] != null && !"".equals(PricesNewArr[index])) {
                                                sum = sum + new Double(PricesNewArr[index]);
                                            }
                                        }
                                        BigDecimal amount = new BigDecimal(sum);
                                        BigDecimal length = new BigDecimal(PricesNewArr.length-2);
                                        avgNew = amount.divide(length, 2, BigDecimal.ROUND_HALF_UP).toString();
                                    } else {
                                        for (int index = 0; index < PricesNewArr.length; index++) {
                                            if (PricesNewArr[index] != null && !"".equals(PricesNewArr[index])) {
                                                sum = sum + new Double(PricesNewArr[index]);
                                            }
                                        }
                                        BigDecimal amount = new BigDecimal(sum);
                                        BigDecimal length = new BigDecimal(PricesNewArr.length);
                                        avgNew = amount.divide(length, 2, BigDecimal.ROUND_HALF_UP).toString();
                                    }

                                }*/
                                this.write(elementCell, avgNew);
                            } else if (key.equals("AR_PRICE")) {
                                ArPricePartMapping arPricePartMapping = arPricePartMappingDao.getNewArPriceByPartNumber(scfseVo.getPartNumber().trim());
                                Object arPrice = "";
                                if(arPricePartMapping != null && !"".equals(arPricePartMapping.getArPrice()) && arPricePartMapping.getArPrice() != null){
                                    arPrice = arPricePartMapping.getArPrice();
                                }
                                this.write(elementCell, arPrice);
                            }else if(key.equals("AR_SALE_PRICE")){
                                ArPricePartMapping arPricePartMapping = arPricePartMappingDao.getNewArPriceByPartNumber(scfseVo.getPartNumber().trim());
                                Object arSalePrice = "";
                                if(arPricePartMapping != null && !"".equals(arPricePartMapping.getArSalePrice()) && arPricePartMapping.getArSalePrice() != null){
                                    arSalePrice = arPricePartMapping.getArSalePrice();
                                }
                                this.write(elementCell, arSalePrice);
                            }
                            else if(key.equals("MRO_REPAIR")){
                                Object mroPrice = scfseVo.getMroRepair();
                                if(mroPrice != null){
                                    String [] mroArray = mroPrice.toString().split(",");
                                    Double sum = 0.0;
                                    if (mroArray.length>5){
                                        for (int mroIndex = 1;mroIndex<mroArray.length-1;mroIndex++){
                                                sum = sum + new Double(mroArray[mroIndex]);
                                        }
                                        BigDecimal total = new BigDecimal(sum);
                                        BigDecimal length = new BigDecimal(mroArray.length-2);
                                        mroPrice = total.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
                                    }else{
                                        for (int mroIndex= 0;mroIndex<mroArray.length;mroIndex++){
                                            sum = sum + new Double(mroArray[mroIndex]);
                                        }
                                        BigDecimal total = new BigDecimal(sum);
                                        BigDecimal length = new BigDecimal(mroArray.length);
                                        mroPrice = total.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
                                    }
                                }
                                this.write(elementCell,mroPrice);
                            }else if(key.equals("MRO_OVERHAUL")){
                                Object mroOverhaul = scfseVo.getMroOverhaul();
                                if(mroOverhaul != null){
                                  String [] mroArray = mroOverhaul.toString().split(",");
                                  Double sum = 0.0;
                                  int mroArrayLength = mroArray.length;
                                  if(mroArrayLength>5){
                                      for (int mroIndex= 1;mroIndex<mroArrayLength-1;mroIndex++){
                                         sum = sum + new Double(mroArray[mroIndex]);
                                      }
                                      BigDecimal total = new BigDecimal(sum);
                                      BigDecimal length = new BigDecimal(mroArrayLength-2);
                                      mroOverhaul = total.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
                                  }else{
                                      for(int mroIndex = 0;mroIndex<mroArrayLength;mroIndex++){
                                          sum = sum + new Double(mroArray[mroIndex]);
                                      }
                                      BigDecimal total = new BigDecimal(sum);
                                      BigDecimal length = new BigDecimal(mroArrayLength);
                                      mroOverhaul = total.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
                                  }
                                }
                                this.write(elementCell,mroOverhaul);
                            }else if(key.equals("TEST_INSPECTED")){
                                Object testInspected = scfseVo.getTestInspected();
                                if(testInspected != null){
                                    String [] mroArray = testInspected.toString().split(",");
                                    Double sum = 0.0;
                                    int mroArrayLength = mroArray.length;
                                    if(mroArrayLength>5){
                                        for (int mroIndex= 1;mroIndex<mroArrayLength-1;mroIndex++){
                                            sum = sum + new Double(mroArray[mroIndex]);
                                        }
                                        BigDecimal total = new BigDecimal(sum);
                                        BigDecimal length = new BigDecimal(mroArrayLength-2);
                                        testInspected = total.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
                                    }else{
                                        for(int mroIndex = 0;mroIndex<mroArrayLength;mroIndex++){
                                            sum = sum + new Double(mroArray[mroIndex]);
                                        }
                                        BigDecimal total = new BigDecimal(sum);
                                        BigDecimal length = new BigDecimal(mroArrayLength);
                                        testInspected = total.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
                                    }
                                }
                                this.write(elementCell,testInspected);
                            }else if(key.equals("SV")){
                                Object sv = scfseVo.getSv();
                                if(sv != null){
                                    String [] mroArray = sv.toString().split(",");
                                    Double sum = 0.0;
                                    int mroArrayLength = mroArray.length;
                                    if(mroArrayLength>5){
                                        for (int mroIndex= 1;mroIndex<mroArrayLength-1;mroIndex++){
                                            sum = sum + new Double(mroArray[mroIndex]);
                                        }
                                        BigDecimal total = new BigDecimal(sum);
                                        BigDecimal length = new BigDecimal(mroArrayLength-2);
                                        sv = total.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
                                    }else{
                                        for(int mroIndex = 0;mroIndex<mroArrayLength;mroIndex++){
                                            sum = sum + new Double(mroArray[mroIndex]);
                                        }
                                        BigDecimal total = new BigDecimal(sum);
                                        BigDecimal length = new BigDecimal(mroArrayLength);
                                        sv = total.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
                                    }
                                }
                                this.write(elementCell,sv);
                            }else if(key.equals("OH")){
                                Object oh = scfseVo.getOh();
                                if(oh != null){
                                    String [] mroArray = oh.toString().split(",");
                                    Double sum = 0.0;
                                    int mroArrayLength = mroArray.length;
                                    if(mroArrayLength>5){
                                        for (int mroIndex= 1;mroIndex<mroArrayLength-1;mroIndex++){
                                            sum = sum + new Double(mroArray[mroIndex]);
                                        }
                                        BigDecimal total = new BigDecimal(sum);
                                        BigDecimal length = new BigDecimal(mroArrayLength-2);
                                        oh = total.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
                                    }else{
                                        for(int mroIndex = 0;mroIndex<mroArrayLength;mroIndex++){
                                            sum = sum + new Double(mroArray[mroIndex]);
                                        }
                                        BigDecimal total = new BigDecimal(sum);
                                        BigDecimal length = new BigDecimal(mroArrayLength);
                                        oh = total.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
                                    }
                                }
                                this.write(elementCell,oh);
                            }else if(key.equals("MRO_TEST")){
                                Object mroTest = scfseVo.getMro_test();
                                if(mroTest != null){
                                    String [] mroArray = mroTest.toString().split(",");
                                    Double sum = 0.0;
                                    int mroArrayLength = mroArray.length;
                                    if(mroArrayLength>5){
                                        for (int mroIndex= 1;mroIndex<mroArrayLength-1;mroIndex++){
                                            sum = sum + new Double(mroArray[mroIndex]);
                                        }
                                        BigDecimal total = new BigDecimal(sum);
                                        BigDecimal length = new BigDecimal(mroArrayLength-2);
                                        mroTest = total.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
                                    }else{
                                        for(int mroIndex = 0;mroIndex<mroArrayLength;mroIndex++){
                                            sum = sum + new Double(mroArray[mroIndex]);
                                        }
                                        BigDecimal total = new BigDecimal(sum);
                                        BigDecimal length = new BigDecimal(mroArrayLength);
                                        mroTest = total.divide(length,2,BigDecimal.ROUND_HALF_UP).toString();
                                    }
                                }
                                this.write(elementCell,mroTest);
                            }
                        }
                    }
                }
            }
        }
    }
}
