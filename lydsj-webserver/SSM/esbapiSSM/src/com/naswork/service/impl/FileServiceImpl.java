/**
 * 
 */
package com.naswork.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import jxl.read.biff.BiffException;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.naswork.common.constants.FileConstant;
import com.naswork.filter.ContextHolder;
import com.naswork.model.gy.GyFj;
import com.naswork.service.FileService;
import com.naswork.service.GyFjService;
import com.naswork.utils.StringUtil;
import com.naswork.utils.UuidUtil;
import com.naswork.utils.excel.XlsUtil;
import com.naswork.vo.UploadFileVo;
import com.naswork.vo.UserVo;

/**
 * @since 2016年5月5日 上午10:11:54
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
@Service("fileService")
public class FileServiceImpl implements FileService {
	@Resource
	private GyFjService gyFjService;
	
	@Override
	public List<Map<String, String>> importXls(File xlsFile)
			throws FileNotFoundException, BiffException, IOException {
		return XlsUtil.readXls(xlsFile);
	}
	
	@Override
	public UploadFileVo uploadFile(Map<String, String[]> paramap , MultipartFile multipartFile) throws Exception {
		/** 如果是图片,读取图片原始高宽 ,读不到不报错**/
		InputStream is = null;
		UploadFileVo uploadFile = new UploadFileVo();
		String originalFilename = multipartFile.getOriginalFilename(); // 文件全名
		String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")+1); // 后缀
		
		//判断是不是图片，如果是图片，获取它的高和宽
		try {
			byte[] bytes = multipartFile.getBytes();
			is = new ByteArrayInputStream(bytes);
			BufferedImage bufimg = ImageIO.read(is);
			// 只有图片才获取高宽
			if (bufimg != null) {
				uploadFile.setImgHeight(bufimg.getHeight());
				uploadFile.setImgWidth(bufimg.getWidth());
			}
			is.close();
		} catch (Exception e) {
			try {
				is.close();
			} catch (IOException e1) {
			}
		}
		
		String folderName=getStringFromParamMap(paramap, "folderName");
		
		//上传文件
		String randomKey = RandomStringUtils.randomNumeric(6);
		String relativePath = "";
		Date date = new Date();
		String dateStr = new SimpleDateFormat("yyyyMMdd").format(date);
		String timeStr = new SimpleDateFormat("HHmmssSSS").format(date);
	
		if(null!=folderName&&!"".endsWith(folderName)){
			 relativePath = StringUtil.join(FileConstant.UPLOAD_FOLDER, "/",folderName, "/"+suffix+"/");
		}else{
			 relativePath = StringUtil.join(FileConstant.UPLOAD_FOLDER, "/",dateStr, "/"+suffix+"/");
			
		}
		String realPath  = StringUtil.join(FileConstant.UPLOAD_REALPATH,relativePath);
		File saveFile = new File(realPath);
		if (!saveFile.exists()) {//目录不存在则创建
			saveFile.mkdirs();
		}
		// 图片文件名: 时间戳 + 随机串 + 高宽
		String fileName = StringUtil.join(timeStr, randomKey, "_", uploadFile.getImgHeight(), "_",
				uploadFile.getImgWidth(), ".", suffix);
		uploadFile.setName(fileName);
		File file = new File(StringUtil.join(realPath, fileName));
		multipartFile.transferTo(file);//保存文件
		
		//保存附件表
		GyFj fj = new GyFj();
		fj.setFjLength(multipartFile.getSize());
		fj.setFjPath(relativePath+fileName);//只记录相对地址
		fj.setFjType(suffix);
		UserVo user = ContextHolder.getCurrentUser(); 
		if(user!=null){
			fj.setUserId(user.getUserId());
		}else{
			fj.setUserId("0");
		}
		fj.setLrsj(new Date());
		
		fj = setYwParam(fj,paramap);
		
		fj.setFjName(getStringFromParamMap(paramap, "fjName"));
		gyFjService.add(fj);
		uploadFile.setId(fj.getFjId());//返回给前台的ID
		uploadFile.setPath(StringUtil.join(FileConstant.DOWNLOAD_FUNCTION,fj.getFjId()));
		
		return uploadFile;
	}

	private GyFj setYwParam(GyFj fj, Map<String, String[]> paramap) {
		String businessKey = getStringFromParamMap(paramap, "businessKey");
		String[] businessKeyArr = businessKey.split("\\.");
		
		fj.setYwTableName(businessKeyArr[0]);
		fj.setYwTablePkName(businessKeyArr[1]);
		fj.setYwId(businessKeyArr[2]);
		return fj;
	}

	private String getStringFromParamMap(Map<String, String[]> paramap,
			String key) {
		String[] v = paramap.get(key);
		if(v!=null && v.length>0){
			return v[0];
		}
		return null;
	}

	@Override
	public void deleteFile(String filePath) {
		File file = new File(FileConstant.UPLOAD_REALPATH+ filePath);
		if(file.exists()){
			file.delete();
		}
		
	}
	
	

}
