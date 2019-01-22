package com.naswork.service;

import java.util.List;

import com.naswork.model.ExportPackageElement;
import com.naswork.model.ImportPackageElement;
import com.naswork.module.finance.controller.clientinvoice.ClientInvoiceExcelVo;
import com.naswork.module.storage.controller.exportpackage.ExportPackageElementVo;
import com.naswork.module.storage.controller.storagedetail.StorageDetailVo;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

public interface ExportPackageElementService {

	void insertSelective(ExportPackageElement record);

    ExportPackageElement selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExportPackageElement record);
    
    /**
     * 批量新增出库明细
     */
    public void insertElements(List<ClientInvoiceExcelVo> list,Integer exportPackageId);
    
    /**
     * 列表
     */
    public void listPage(PageModel<ExportPackageElementVo> page, String where,
			GridSort sort);
    
    /**
     * 新增列表
     */
    public List<ExportPackageElementVo> addElementPage(PageModel<ExportPackageElementVo> page,String where,GridSort sort);
    
    
   void exportPackageInstructionsPage(PageModel<ExportPackageElementVo> page,String where,GridSort sort);
   
   Double BoxWeight(ImportPackageElement parame);
    
    /**
     * 获取入库明细
     * @param where
     * @return
     */
    public List<ExportPackageElementVo> getImportElement(PageModel<ExportPackageElementVo> page);
    
    public List<ExportPackageElementVo> creatExcel(Integer id);
    
    public List<ImportPackageElement> getElementByLocation(String location);
    
    public List<ExportPackageElementVo> getCountWithLocation(Integer exportPackageId);
    
    /**
     * 缺失件列表
     */
    public void lackOfPartNumber(PageModel<ImportPackageElement> page,Integer exportPackageId);
    
    public List<ImportPackageElement> getLackList(PageModel<ImportPackageElement> page);
    
    void findClientOrderNumber(PageModel<ExportPackageElementVo> page);
    
    List<ExportPackageElementVo> findByEpidAndCoid(String  exportPackageId,String clientOrderId);
    
    ExportPackageElementVo findEpeAmount(Integer  exportPackageId,Integer clientOrderId);
    
    /**
     * 查询入库明细ID
     */
    public ExportPackageElement getImportPackageElementId(PageModel<ExportPackageElement> page);
    
    /**
     * 根据客户订单ID获取出库数量
     * @param clientOrderId
     * @return
     */
    public int getExportPackageElementCount(Integer clientOrderId);
    
    /**
     * 根据客户订单ID获取订单数量
     * @param clientOrderId
     * @return
     */
    public int getCLientOrderElementCount(Integer clientOrderId);
    
    /**
     * 生成尾款发票
     * @param exportPackageElement
     */
    public void checkExportPackage(ExportPackageElement exportPackageElement);
    
    /**
     * 按照出库指令查询
     * @param instructionsId
     * @return
     */
    public List<ExportPackageElementVo> selectByInstructionsId(Integer instructionsId);
    
    public int deleteByPrimaryKey(Integer id);
	
    /**
     * 检查该位置是否多件了
     * @param list
     * @param importList
     * @return
     */
    public List<ImportPackageElement> unEqual(List<ExportPackageElementVo> list,List<ImportPackageElement> importList);
    
    
    /**
     * 出库明细箱子重量
     * **/
    List<ExportPackageElementVo> findBoxWeight(Integer id);
    
    public void checkExportPackageForCom(ExportPackageElement exportPackageElement,Integer shipId);
    
    public List<ExportPackageElement> getByExportElementByOrderId(Integer clientOrderElementId,Integer importPackageId);
}
