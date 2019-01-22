package com.naswork.module.system.controller.supplierpnmanage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.naswork.common.controller.BaseController;
import com.naswork.model.ClientClassify;
import com.naswork.model.Jbpm4Jbyj;
import com.naswork.model.SupplierPnRelationKey;
import com.naswork.model.SupplierQuote;
import com.naswork.model.SupplierQuoteElement;
import com.naswork.model.SupplierWeatherOrderElement;
import com.naswork.model.SystemCode;
import com.naswork.module.marketing.controller.clientquote.ClientQuoteVo;
import com.naswork.module.purchase.controller.supplierquote.MessageVo;
import com.naswork.module.purchase.controller.supplierquote.SupplierInquiryEmelentVo;
import com.naswork.module.system.controller.suppliermanage.FactoryVo;
import com.naswork.service.SupplierPnRelationBackUpService;
import com.naswork.service.SupplierPnRelationService;
import com.naswork.service.SystemCodeService;
import com.naswork.utils.EntityUtil;
import com.naswork.utils.excel.poi.POIExcelReader;
import com.naswork.utils.excel.poi.POIExcelWorkBook;
import com.naswork.vo.EditRowResultVo;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

import net.sf.json.JSONArray;

@Controller
@RequestMapping("/supplierpnmanage")
public class SupplierPnManageController extends BaseController {

	@Resource
	private SupplierPnRelationService supplierPnRelationService;
	@Resource
	private SystemCodeService systemCodeService;
	@Resource
	private SupplierPnRelationBackUpService supplierPnRelationBackUpService;
	
	
	/**
	 * 供应商列表
	 * **/
	@RequestMapping(value="/viewList",method=RequestMethod.GET)
	public String viewList(){
		return "/system/supplierpnmanage/list";
	}
	
	/**
	 * 供应商件号管理列表
	 * **/
	@RequestMapping(value="/pnViewList",method=RequestMethod.GET)
	public String pnViewList(HttpServletRequest request){
		request.setAttribute("supplierId", request.getParameter("id"));
		return "/system/supplierpnmanage/supllierpnelementlist";
	}
	
	/**
	 * 新增供应商件号
	 * **/
	@RequestMapping(value="/toAddTable",method=RequestMethod.GET)
	public String toAddTable(HttpServletRequest request){
		request.setAttribute("supplierId", request.getParameter("id"));
		String list=request.getParameter("list");
		return "/system/supplierpnmanage/addElementTable";
	}
	
	/**
	 * 新增供应商件号
	 * **/
	@RequestMapping(value="/tableData",method=RequestMethod.POST)
	public @ResponseBody ResultVo tableData(HttpServletRequest request,@ModelAttribute SupplierInquiryEmelentVo elementVo){
		request.setAttribute("supplierId", request.getParameter("id"));
		String partNumberCode="";
		String partNumberName="";
		boolean success = true;
		String message = "";
		if (elementVo.getList().size()>0) {
		for (int i = 0; i < elementVo.getList().size(); i++) {
		if(!elementVo.getList().get(i).getDescription().equals("")&&!elementVo.getList().get(i).getPartNumber().equals("")){
			partNumberCode=elementVo.getList().get(i).getPartNumber();
			partNumberName=elementVo.getList().get(i).getDescription();
			message+=partNumberCode+"PartNo"+partNumberName+"PartName";//件号和描述
				}else if(elementVo.getList().get(i).getDescription().equals("")&&!elementVo.getList().get(i).getPartNumber().equals("")){
					partNumberCode=elementVo.getList().get(i).getPartNumber();
					partNumberName="";
					message+=partNumberCode+"PartNo"+partNumberName+"PartName";//件号和描述
				}
			}
		}
		ResultVo resultVo=new ResultVo(success, message);
		return resultVo;
	}
	
	/**
	 * excel上传
	 */
	@RequestMapping(value="/uploadExcel",method=RequestMethod.POST)
	public @ResponseBody String uploadExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute 
			SupplierQuote record) {
		InputStream fileStream = null;
		boolean success=false;
		String message="";
		String partNumberCode="";
		String partNumberName="";
		Integer id =new Integer(request.getParameter("id"));
		MessageVo messageVo =new MessageVo();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		try {
//			 messageVo = supplierQuoteElemenetService.uploadExcel(multipartFile, id);
			byte[] bytes = multipartFile.getBytes();
			String fileName = multipartFile.getOriginalFilename();
			fileStream = new ByteArrayInputStream(bytes);
			POIExcelReader reader = new POIExcelReader(fileStream, fileName);
			POIExcelWorkBook wb = reader.getWorkbook();
		    Sheet sheet = wb.getSheetAt(0);
			 //定义行
		    Row row;	
		    for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
		    	row = sheet.getRow(i);
		    	if (row!=null && row.getCell(0) != null && !"".equals(row.getCell(0).toString())) {
		    		Cell oneCell = row.getCell(0);
		            String partNumber = ""; 
		            if (oneCell.getCellType()==HSSFCell.CELL_TYPE_STRING) {
						partNumber = oneCell.toString();
					}else if (oneCell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC) {
						HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
					    partNumber = dataFormatter.formatCellValue(oneCell);
					}
		            String description = row.getCell(1).toString();
		        	if(!description.equals("")&&!partNumber.equals("")){
		    			partNumberCode=partNumber;
		    			partNumberName=description;
		    			message+=partNumberCode+"PartNo"+partNumberName+"PartName";
    				}else if(description.equals("")&&!partNumber.equals("")){
		    			partNumberCode=partNumber;
		    			partNumberName="";
		    			message+=partNumberCode+"PartNo"+partNumberName+"PartName";
    				}
		    	}
		    }
		    success=true;
		    messageVo.setFlag(success);
			messageVo.setMessage(message);
		} catch (Exception e) {
				e.printStackTrace();
				messageVo.setFlag(success);
				messageVo.setMessage(e.getMessage());
			}
		
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
	
	/**
	 * 新增供应商件号
	 * @throws UnsupportedEncodingException 
	 * **/
	@RequestMapping(value="/elementData",method=RequestMethod.GET)
	public String elementData(HttpServletRequest request) throws UnsupportedEncodingException{
		String data=request.getParameter("data");
		String supplierId=request.getParameter("supplierId");
		data = new String(data.getBytes("iso8859-1"),"UTF-8");
		String[] partNumAndPartNos=data.split("PartName");
		List<SupplierPnRelationKey> list = new ArrayList<SupplierPnRelationKey>();
		List<SupplierPnRelationKey> lists = new ArrayList<SupplierPnRelationKey>();
		for (int i = 0; i < partNumAndPartNos.length; i++) {
			String[] partNumAndPartNo=partNumAndPartNos[i].split("PartNo");
			SupplierPnRelationKey supplierPnRelationKey=new SupplierPnRelationKey();
			supplierPnRelationKey.setSupplierId(Integer.parseInt(supplierId));
			if(partNumAndPartNo.length>1){
			supplierPnRelationKey.setPartName(partNumAndPartNo[1]);
			}
			supplierPnRelationKey.setPartNum(partNumAndPartNo[0]);
			list.add(supplierPnRelationKey);
			lists=supplierPnRelationService.selectByNameAndNum(getCodeFromPartNumber(partNumAndPartNo[0]));
			if(lists.size()>1){
				for (int j = 0; j < lists.size(); j++) {
					lists.get(j).setMatch("否");
					if(partNumAndPartNo.length>1){
					if(lists.get(j).getPartName().trim().indexOf(partNumAndPartNo[1].trim())>-1){
						lists.get(j).setMatch("是");
						list.add(lists.get(j));
						lists.remove(j);
						j=j-1;
					}
					}
				}
				list.addAll(lists);
			}else if(lists.size()==1){
				lists.get(0).setMatch("否");
				if(partNumAndPartNo.length>1){
				if(lists.get(0).getPartName().trim().indexOf(partNumAndPartNo[1].trim())>-1){
					lists.get(0).setMatch("是");
				}
				}
					
				
				list.addAll(lists);
			}
		}
		request.setAttribute("list", list);
		return "/system/supplierpnmanage/addElementTable2";
	}
	
	/**
	 * 新增供应商件号
	 * **//*
	@RequestMapping(value="/addData",method=RequestMethod.POST)
	public @ResponseBody ResultVo addData(HttpServletRequest request,@ModelAttribute SupplierPnRelationKey supplierPnRelationKey){
		String supplierId=request.getParameter("id");
		supplierPnRelationKey.setSupplierId(Integer.parseInt(supplierId));
		boolean success = true;
		String message = "新增成功";
		if(supplierPnRelationKey.getList().size()>0){
		for (int i = 0; i < supplierPnRelationKey.getList().size(); i++) {
			if(null!=supplierPnRelationKey.getList().get(i).getDataItem()&&supplierPnRelationKey.getList().get(i).getDataItem().equals("check")){
//				 List<SupplierPnRelationKey>  list=supplierPnRelationService.selectBybsn(supplierPnRelationKey.getList().get(i).getBsn(),supplierId);
//				if(list.size()==0){
//				supplierPnRelationKey.setBsn(supplierPnRelationKey.getList().get(i).getBsn());
//				supplierPnRelationService.insert(supplierPnRelationKey);
//				}
			}
		}
		}
		ResultVo resultVo=new ResultVo(success, message);
		return resultVo;
	}*/
	
	
	
	/**
	 * 列表数据分页
	 * **/
	@RequestMapping(value="/listData",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo queryclientquote(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ClientQuoteVo clientQuoteV){
		PageModel<SupplierPnRelationKey> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		String supplierId=request.getParameter("supplierId");
		page.put("supplierId", supplierId);
		supplierPnRelationService.selectByPrimaryKeyPage(page);
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (SupplierPnRelationKey supplierPnRelationKey : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(supplierPnRelationKey);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
		}else{
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		return jqgrid;
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
		return buffer.toString();
	}
    public static boolean isValidCharacter(char ch) {
		return Character.isLetterOrDigit(ch);
	}
    
	/*
	 *  excel上传能力清单
	 */
	@RequestMapping(value="/partUploadExcel",method=RequestMethod.POST)
	public @ResponseBody String partUploadExcel(HttpServletRequest request,HttpServletResponse response) {
		boolean success=false;
		String message="";
		UserVo userVo=getCurrentUser(request);
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		response.setContentType("text/html; charset=utf-8");
		String id=request.getParameter("id");
		String supplierPnType=request.getParameter("supplierPnType");
		MessageVo messageVo = supplierPnRelationService.excelUpload(multipartFile,Integer.parseInt(id),supplierPnType);
		JSONArray json = new JSONArray();
		json.add(messageVo);
		message =json.toString();
		response.setContentType("text/html;chartset=UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 
		return message;
	}
    
	/**
	 * 供应商件号类型
	 */
	@RequestMapping(value="/supplierPnType",method=RequestMethod.POST)
	public @ResponseBody ResultVo supplierPnType(HttpServletRequest request) {
		boolean success = true;
		String msg = "";
		List<SystemCode> codes=systemCodeService.findSupplierByType("SUPPLIER_PN_TYPE");
		success = true;
		JSONArray json = new JSONArray();
			json.add(codes);
		msg =json.toString();
		return new ResultVo(success, msg);
	}

	/*
	 * 错误列表
	 */
	@RequestMapping(value="/toUnknow",method=RequestMethod.GET)
	public String toUnknow() {
		return "/system/supplierpnmanage/errorlist";
	}
	
	
	/**
	 * 删除供应商件号
	 * **/
	@RequestMapping(value="/deleteData",method=RequestMethod.POST)
	public @ResponseBody ResultVo deleteData(HttpServletRequest request){
		String supplierId=request.getParameter("supplierId");
		String bsn=request.getParameter("bsn");
		SupplierPnRelationKey key=new SupplierPnRelationKey();
		key.setBsn(bsn);
		key.setSupplierId(Integer.parseInt(supplierId));
		supplierPnRelationService.deleteByPrimaryKey(key);
		boolean success = true;
		String message = "删除成功";
		ResultVo resultVo=new ResultVo(success, message);
		return resultVo;
	}
	
	/**
	 * 修改供应商件号信息
	 */
	@RequestMapping(value="/updateSupplierPn",method=RequestMethod.POST)
	public @ResponseBody EditRowResultVo updateSupplierPn(HttpServletRequest request,@ModelAttribute SupplierPnRelationKey key) {
		boolean success = false;
		String message = "";
		try {
			supplierPnRelationService.updateByPrimarySelectiveKey(key);
			success=true;
			message="修改完成";
		} catch (Exception e) {
			success=false;
			message="修改失败";
		}
		
		return new EditRowResultVo(success, message);
	}
	
	/*
	 * 错误列表
	 */
	@RequestMapping(value="/toPartAbilityErrorList",method=RequestMethod.GET)
	public String toPartAbilityErrorList(HttpServletRequest request) {
		PageModel<FactoryVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo = getCurrentUser(request);
		//page.put("userId", userVo.getUserId());
		//String supplierId=request.getParameter("supplierId");
		//page.put("supplierId", supplierId);
		supplierPnRelationBackUpService.getByUserIdPage(page, new Integer(userVo.getUserId()));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (FactoryVo factoryVo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(factoryVo);
				mapList.add(map);
			}
			JSONArray json = new JSONArray();
			json.add(mapList);
			request.setAttribute("list", json.toString());
			request.setAttribute("count", mapList.size());
		}else{
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		//request.setAttribute("count", page.getRecordCount());8
		//return jqgrid;
		return "/system/supplierpnmanage/partabilityerrorlist";
	}
	
	/**
	 * 错误列表数据分页
	 * **/
	@RequestMapping(value="/partAbilityErrorList",method = RequestMethod.POST)
	public @ResponseBody JQGridMapVo partAbilityErrorList(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ClientQuoteVo clientQuoteV){
		PageModel<FactoryVo> page = getPage(request);
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo userVo = getCurrentUser(request);
		//page.put("userId", userVo.getUserId());
		//String supplierId=request.getParameter("supplierId");
		//page.put("supplierId", supplierId);
		supplierPnRelationBackUpService.getByUserIdPage(page, new Integer(userVo.getUserId()));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (FactoryVo factoryVo : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(factoryVo);
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
		}else{
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String,Object>>());
		}
		return jqgrid;
	}	
	
	/**
	 * 保存匹配的
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saveMatch",method=RequestMethod.POST)
	public @ResponseBody ResultVo saveMatch(HttpServletRequest request){
		String[] bsn= getString(request, "bsn").split(",");
		String[] msns= getString(request, "msn").split(",");
		String[] ids = getString(request, "id").split(",");
		boolean success = supplierPnRelationService.saveMatch(bsn, ids);
		if (success) {
			return new ResultVo(success, "新增成功！");
		}else {
			return new ResultVo(success, "新增失败！");
		}
			
	}
	
	/*
	 * 删除数据
	 */
	@RequestMapping(value="/deleteMessage",method=RequestMethod.POST)
	public @ResponseBody ResultVo deleteMessage(HttpServletRequest request) {
		boolean success=true;
		String message="";
		UserVo userVo=getCurrentUser(request);
		supplierPnRelationBackUpService.deleteMessage(new Integer(userVo.getUserId()));
		return new ResultVo(success, message);
	}
	   
		
}
