/**
 * 
 */
package com.naswork.common.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.w3c.dom.Document;

import com.naswork.common.constants.FileConstant;
import com.naswork.model.gy.GyFj;
import com.naswork.service.FileService;
import com.naswork.service.GyFjService;
import com.naswork.utils.EntityUtil;
import com.naswork.utils.Office2Pdf;
import com.naswork.utils.json.JsonUtils;
import com.naswork.vo.JQGridMapVo;
import com.naswork.vo.PageData;
import com.naswork.vo.PageModel;
import com.naswork.vo.ResultVo;
import com.naswork.vo.UploadFileVo;

/**
 * @since 2016年5月7日 下午3:04:15
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
@Controller
@RequestMapping("/fj")
public class AttachmentController extends BaseController {
	@Resource
	private GyFjService gyFjService;
	@Resource
	private FileService fileService;

	/**
	 * 附件列表
	 * 
	 * @return
	 * @since 2016年5月5日 上午11:15:37
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String attachmentlist(HttpServletRequest request) {
		String data = request.getParameter("data");
		Assert.notNull(data);
		request.setAttribute("data", data);
		request.setAttribute("datajson", JsonUtils.urlParam2Json(data));
		PageData pd = new PageData(request);
		if (pd != null) {
			request.setAttribute("pd", JsonUtils.toJson(pd));
		}
		request.setAttribute("fileType", request.getParameter("fileType"));
		String roleId=request.getParameter("roleId");
		request.setAttribute("roleId", roleId);
		return "/attachment/list";
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
	JQGridMapVo attachmentlistdata(HttpServletRequest request,
			HttpServletResponse response) {
		PageModel<GyFj> page = getPage(request);
		
		String businessKey = getString(request, "businessKey");
		page.put("businessKey", businessKey);
		
		String roleId = getString(request, "roleId");
		if(null!=roleId&&!"".equals(roleId)){
			page.put("roleId", roleId);
		}
		
		
		JQGridMapVo jqgrid = new JQGridMapVo();
		String searchString = request.getParameter("searchString");
		if (StringUtils.isNotBlank(searchString)) {

		} else {

		}
		String[] businessKeys=businessKey.split("\\.");
		String ywid = businessKey.substring(businessKey.lastIndexOf(".") + 1, businessKey.length());
		page.put("parm", "and YW_TABLE_NAME ='"+businessKeys[0]+"'"+")"+"or ( fj.YW_ID = '" + ywid + "'" +"and YW_TABLE_NAME ='"+businessKeys[0]+"_element"+"'"+"))");
		gyFjService.searchPage(page, searchString, getSort(request));
		if (page.getEntities().size() > 0) {
			jqgrid.setPage(page.getPageNo());
			jqgrid.setRecords(page.getRecordCount());
			jqgrid.setTotal(page.getPageCount());
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (GyFj fj : page.getEntities()) {
				Map<String, Object> map = EntityUtil.entityToTableMap(fj);
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
				exportService.exportGridToXls("附件列表",
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
			GyFj fj = gyFjService.findById(id);
			String fileName = fj.getFjName();
			if(fileName.indexOf(".")==-1){
				fileName = fileName+"."+fj.getFjType();
			}
			if (fj != null) {
				HttpHeaders headers = new HttpHeaders();
				try {
					headers.setContentDispositionFormData("attachment",
							new String(fileName.getBytes("UTF-8"),
									"ISO-8859-1"));
					headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
					headers.setContentLength(fj.getFjLength().longValue());
					return new ResponseEntity<byte[]>(
							FileUtils.readFileToByteArray(new File(
									FileConstant.UPLOAD_REALPATH
											+ fj.getFjPath())), headers,
							HttpStatus.OK);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {// 多个附件，压缩后下载
			List<GyFj> fjs = gyFjService.findByIds(idarr);
			ZipOutputStream zos;
			try {
				response.setContentType("application/zip");
				String fileName = "附件打包.zip";
				response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"),"ISO-8859-1")); 
				if (fjs != null && fjs.size() > 0) {
					// 压缩下载
					byte[] buffer = new byte[1024];
						zos = new ZipOutputStream(response.getOutputStream());
						for (GyFj gyFj : fjs) {
							String subFileName = gyFj.getFjName();
							if(subFileName.indexOf(".")==-1){
								subFileName = subFileName+"."+gyFj.getFjType();
							}
							FileInputStream fis = new FileInputStream(
									new File(FileConstant.UPLOAD_REALPATH
											+ gyFj.getFjPath()));
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

	/**
	 * 附件删除
	 * @param ids
	 * @return
	 * @since 2016年5月10日 下午7:05:44
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/remove",method = RequestMethod.POST)
	public @ResponseBody ResultVo removeFj(@RequestParam String ids){
		boolean success = false;
		String message = "";
		try{
			gyFjService.removeFjByIds(ids);
			success = true;
			message = "删除附件成功";
		}catch(Exception e){
			e.printStackTrace();
			success = false;
			message = "删除附件失败";
		}
		
		return new ResultVo(success, message);
	}
	
	/**
	 * 处理上传方法
	 * @param request
	 * @return
	 * @throws Exception
	 * @since 2015年9月1日 下午3:45:02
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody String upload(HttpServletRequest request) throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("file");
		Map<String, String[]> paramap = multipartRequest.getParameterMap();
		UploadFileVo uploadFile = fileService.uploadFile(paramap, multipartFile);
		return uploadFile.toString();
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
	@RequestMapping("/viewPdf")
	public void PoiExcelToHtml(HttpServletResponse response,HttpServletRequest request){
		String ids = getString(request, "ids");
		String[] idarr = ids.split(",");
		String id = idarr[0];
		GyFj fj = gyFjService.findById(id);
		String fileName = FileConstant.UPLOAD_REALPATH + fj.getFjPath();
		fileName = fileName.replace("//", "\\");
		fileName = fileName.replace("/", "\\");
		if(fileName.indexOf(".")==-1){
			fileName = fileName+"."+fj.getFjType();
		}
		Office2Pdf office2Pdf = new Office2Pdf();
		String pdf = "";
		if (fileName.indexOf(".doc") >= 0 || fileName.indexOf(".docx") >= 0) {
			pdf = office2Pdf.word2Pdf(fileName);
		}else {
			pdf = office2Pdf.excel2pdf(fileName);
		}
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
		return "/attachment/viewpdf";
	}
}
