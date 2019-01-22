package com.naswork.common.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.naswork.common.constants.FileConstant;
import com.naswork.filter.ContextHolder;
import com.naswork.model.gy.GyExcel;
import com.naswork.model.gy.GyFj;
import com.naswork.service.FileService;
import com.naswork.service.GyExcelService;
import com.naswork.utils.EntityUtil;
import com.naswork.utils.Office2Pdf;
import com.naswork.utils.excel.ExcelGeneratorBase;
import com.naswork.utils.excel.ExcelGeneratorMapConstant;
import com.naswork.utils.json.JsonUtils;
import com.naswork.vo.ColumnVo;
import com.naswork.vo.EditRowResultVo;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UserVo;

@Controller
@RequestMapping("/excelfile")
public class ExcelController extends BaseController {
	@Resource
	private GyExcelService gyExcelService;
	@Resource
	private FileService fileService;

	/**
	 * excel文件列表
	 * 
	 * @return
	 * @since 2016年5月5日 上午11:15:37
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String excelFilelist(HttpServletRequest request) {
		String data = request.getParameter("data");
		String title = request.getParameter("title");
		Assert.notNull(data);
		request.setAttribute("batch", request.getParameter("batch"));
		request.setAttribute("title", title);
		request.setAttribute("data", data);
		request.setAttribute("datajson", JsonUtils.urlParam2Json(data));
		PageData pd = new PageData(request);
		if (pd != null) {
			request.setAttribute("pd", JsonUtils.toJson(pd));
		}
		if(data.contains("dynamic")){
			return "/excelfile/dynamiclist";
		}
		return "/excelfile/list";
	}
	
	
	@RequestMapping(value = "/list/dynamicColNames", method = RequestMethod.POST)
	public @ResponseBody
	ColumnVo excelListDynamicCol(HttpServletRequest request,
			HttpServletResponse response) {
		String businessKey = getString(request, "businessKey");
		int dynamicColCount = Integer.valueOf(businessKey.substring(businessKey.lastIndexOf(".")+1));
		List<String> displayNames = new ArrayList<String>();
		List<String> colNames = new ArrayList<String>();
		for(int i=0;i<dynamicColCount;i++){
			displayNames.add("COL"+i);
			colNames.add("col"+i);			
		}
		ColumnVo result = new ColumnVo();
		result.setColumnDisplayNames(displayNames);
		result.setColumnKeyNames(colNames);
		
		return result;
	}
	/**
	 * 附件列表数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @since 2016年5月5日 下午3:04:37
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/list/data", method = RequestMethod.POST)
	public @ResponseBody
	JQGridMapVo excelListData(HttpServletRequest request,
			HttpServletResponse response) {
		PageModel<GyExcel> page = getPage(request);
		String businessKey = getString(request, "businessKey");
		boolean dynamic = false;
		int dynamicColCount = 0;
		
		if(businessKey.contains("dynamic")){
			dynamicColCount = Integer.valueOf(businessKey.substring(businessKey.lastIndexOf(".")+1));
			dynamic = true;
		}
		JQGridMapVo jqgrid = new JQGridMapVo();
		UserVo user = ContextHolder.getCurrentUser();
		page.put("userId", user.getUserId());
		gyExcelService.searchPage(page, businessKey, getSort(request));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (GyExcel gyExcel : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(gyExcel);
				if(dynamic==true){
					for(int i=0;i<dynamicColCount;i++){
						map.put("col"+i, gyExcel.getExcelFileId()+"_"+i);
					}
				}
				mapList.add(map);
			}
			jqgrid.setRows(mapList);
		} else {
			jqgrid.setRecords(0);
			jqgrid.setTotal(0);
			jqgrid.setRows(new ArrayList<Map<String, Object>>());
		}
		// 导出
		if (StringUtils.isNotEmpty(request.getParameter("exportModel"))) {
			try {
				exportService.exportGridToXls("excel文件列表",
						request.getParameter("exportModel"), jqgrid.getRows(),
						response);
				return null;
			} catch (Exception e) {
				logger.warn("导出数据出错!", e);
			}
		}
		return jqgrid;
		
	}
	
	/**
	 * 下载方法
	 * 
	 * @param response
	 * @return
	 * @since 2015年9月7日 下午5:10:49
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/download/{ids}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> download(HttpServletResponse response,
			@PathVariable("ids") String ids) {
		String[] idarr = ids.split(",");
		if (idarr == null || idarr.length == 0)
			return null;

		if (idarr.length == 1) {// 只有一个附件时直接下载
			String id = idarr[0];
			GyExcel gyExcel = gyExcelService.findById(id);
			String fileName = gyExcel.getExcelFileName();
			if(fileName.indexOf(".")==-1){
				fileName = fileName+"."+gyExcel.getExcelType();
			}
			if (gyExcel != null) {
				HttpHeaders headers = new HttpHeaders();
				try {
					String fileNames[]=fileName.split("_");
					if(fileNames.length>4){
						fileName=fileNames[1]+" "+fileNames[0]+fileNames[fileNames.length-1].substring(6);
					}
					headers.setContentDispositionFormData("attachment",
							new String(fileName.getBytes("UTF-8"),
									"ISO-8859-1"));
					headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
					headers.setContentLength(gyExcel.getExcelFileLength().longValue());
					return new ResponseEntity<byte[]>(
							FileUtils.readFileToByteArray(new File(
											gyExcel.getExcelFilePath())), headers,
							HttpStatus.OK);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {// 多个附件，压缩后下载
			List<GyExcel> excels = gyExcelService.findByIds(idarr);
			ZipOutputStream zos;
			try {
				response.setContentType("application/zip");
				String fileName = "Excel打包.zip";
				response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"),"ISO-8859-1")); 
				if (excels != null && excels.size() > 0) {
					// 压缩下载
					byte[] buffer = new byte[1024];
						zos = new ZipOutputStream(response.getOutputStream());
						for (GyExcel gyExcel : excels) {
							String subFileName = gyExcel.getExcelFileName();
							if(subFileName.indexOf(".")==-1){
								subFileName = subFileName+"."+gyExcel.getExcelType();
							}
							FileInputStream fis = new FileInputStream(
									new File(gyExcel.getExcelFilePath()));
							zos.putNextEntry(new ZipEntry(new String(subFileName)));
							int len;
							while ((len = fis.read(buffer)) > 0) {
								zos.write(buffer, 0, len);
							}
							zos.setEncoding("UTF-8");
							zos.closeEntry();
							fis.close();
						}
						zos.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	@RequestMapping(value = "/remove",method = RequestMethod.POST)
	public @ResponseBody ResultVo removeExcel(@RequestParam String ids){
		boolean success = false;
		String message = "";
		try{
			gyExcelService.removeExcelByIds(ids);
			success = true;
			message = "删除附件成功";
		}catch(Exception e){
			e.printStackTrace();
			success = false;
			message = "删除附件失败";
		}
		
		return new ResultVo(success, message);
	}

	@RequestMapping(value = "/edit",method = RequestMethod.POST)
	public @ResponseBody EditRowResultVo updateExcel(HttpServletRequest request){
		GyExcel gyExcel = new GyExcel();
		String excelFileId = getString(request,"excelFileId");
		String excelFileName = getString(request,"excelFileName");
		gyExcel.setExcelFileName(excelFileName);
		gyExcel.setExcelFileId(excelFileId);
		gyExcel.setUserId(getString(request, "userId"));
		boolean success = false;
		String message = "";
		try{
			if(excelFileId.equals("-1")){
				gyExcel.setExcelType("xls");
				gyExcel.setExcelFileLength(new Long(1000));
				gyExcel.setYwId("3");
				gyExcel.setYwTableName("SampleTableName");
				gyExcel.setYwTablePkName("id");
				gyExcel.setXh(0);
				gyExcelService.add(gyExcel);
				message = "新增excel成功";				
			}else{
				gyExcelService.modify(gyExcel);
				message = "更新excel成功";				
			}
			success = true;
		}catch(Exception e){
			e.printStackTrace();
			success = false;
			message = "更新excel失败";
		}
		
		EditRowResultVo result = new EditRowResultVo(success, message);
		if(success && excelFileId.equals("-1")){
			result.setRowKeyName("excelFileId");
			result.setRowKeyValue(gyExcel.getExcelFileId());
		}
		
		return result;
	}
	
	/**
	 * 生成excel
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/generate", method = RequestMethod.POST)
	public @ResponseBody String generate(HttpServletRequest request) throws Exception {
		String businessKey = getString(request, "businessKey");
		return gyExcelService.generateExcel(businessKey);		
	}
	
	/**
	 * 在线预览excel
	 * @param request
	 * @return
	 * @throws Exception
	 * @since 2018年3月81日 下午3:23:14
	 * @author tanoy<tanoy@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping("/viewExcel")
	public void PoiExcelToHtml(HttpServletResponse response,HttpServletRequest request){
		String ids = getString(request, "ids");
		String[] idarr = ids.split(",");
		String id = idarr[0];
		GyExcel gyExcel = gyExcelService.findById(id);
		String fileName = gyExcel.getExcelFilePath();
		fileName = fileName.replace("\\/", "");
		fileName = fileName.replace("//", "\\");
		fileName = fileName.replace("/", "\\");
		if(fileName.indexOf(".")==-1){
			fileName = fileName+"."+gyExcel.getExcelType();
		}
		Office2Pdf office2Pdf = new Office2Pdf();
		String pdf = office2Pdf.excel2pdf(fileName);
		if (pdf != null) {
			File file = new File(pdf);
			response.setContentType("application/pdf");
	        try {
	            if(file.exists()) {
	                DataOutputStream dataOutputStream = new DataOutputStream(response.getOutputStream());
	                DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
	
	                byte[] buffer = new byte[2048];
	                int len = buffer.length;
	                while((len = dataInputStream.read(buffer, 0, len)) != -1) {
	                    dataOutputStream.write(buffer);
	                    dataOutputStream.flush();
	                }
	                dataInputStream.close();
	                dataOutputStream.close();
	            }
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
		}
    }
	
	/**
	 * 跳转查看pdf页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/toViewPdf",method=RequestMethod.GET)
	public String toViewPdf(HttpServletRequest request){
		request.setAttribute("ids", getString(request, "ids"));
		return "/excelfile/viewpdf";
	}

}
