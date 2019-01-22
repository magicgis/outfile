package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.naswork.model.gy.GyFj;
import com.naswork.dao.GyFjDao;
import com.naswork.service.FileService;
import com.naswork.service.GyFjService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

/**
 * @since 2016年05月05日 15:52:23
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
@Service("gyFjService")
public class GyFjServiceImpl implements GyFjService{

	@Resource
	private GyFjDao gyFjDao;
	@Resource
	private FileService fileService;
	
	 /**
	 * 根据主键查询对象
	 * @param fjUuid
	 * @return
	 * @since 2016年05月05日 15:52:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public GyFj findById(String fjUuid){
		return gyFjDao.findById(fjUuid);
	}
	
	/**
	 * 新增
	 * @param gyFj
	 * @since 2016年05月05日 15:52:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void add(GyFj gyFj){
		gyFjDao.insert(gyFj);
		
	}
	
	/**
	 * 更新
	 * @param gyFj
	 * @since 2016年05月05日 15:52:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void modify(GyFj gyFj){
		gyFjDao.update(gyFj);
	}
	
	/**
	 * 删除
	 * @param fjUuid
	 * @since 2016年05月05日 15:52:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void remove(String fjUuid){
		GyFj fj = findById(fjUuid);
		String filePath = fj.getFjPath();
		gyFjDao.delete(fjUuid);
		fileService.deleteFile(filePath);
	}

	@Override
	public void searchPage(PageModel<GyFj> page, String where, GridSort sort) {
		if(!"".equals(where)){
		String businessKey = page.getString("businessKey");
		String ywid = businessKey.substring(businessKey.lastIndexOf(".") + 1, businessKey.length());
		String tableName = businessKey.split("\\.")[0];
		where = "( fj.YW_ID = '" + ywid + "'" ;
		page.put("where", where);
		}
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(gyFjDao.findPage(page));
	}

	@Override
	public List<GyFj> findByIds(String[] idarr) {
		StringBuffer idsb = new StringBuffer();
		for (String id : idarr) {
			idsb.append("'").append(id).append("',");
		}
		String ids = idsb.toString().replaceAll(",$", "");
		return gyFjDao.findByIds(ids);
	}

	@Override
	public void removeFjByIds(String ids) {
		String[] idarr = ids.split(",");
		for (String id : idarr) {
			remove(id);
		}
	}
	
	/**
	 * 根据业务ID获取附件
	 * @param ywid
	 * @return
	 * @since 2016年5月23日 下午7:13:53
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public List<GyFj> findByYwid(String ywid) {
		return gyFjDao.findByYwid(ywid);
	}

}