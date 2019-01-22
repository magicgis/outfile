package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.naswork.dao.VasStrogeDao;
import com.naswork.model.VasStroge;
import com.naswork.model.gy.GyFj;
import com.naswork.service.VasStrogeService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

@Service("vasStrogeService")
public class VasStrogeServiceImpl implements VasStrogeService {
	
	@Resource
	private VasStrogeDao vasStrogeDao;

	@Override
	public void insertSelective(VasStroge record) {
		vasStrogeDao.insertSelective(record);
	}

	@Override
	public VasStroge selectByPrimaryKey(Integer id) {
		return vasStrogeDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(VasStroge record) {
		vasStrogeDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public void getFileInformationPage(PageModel<VasStroge> page,String where,GridSort sort) {
		page.put("where", where);
    	if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
    	page.setEntities(vasStrogeDao.getFileInformationPage(page));
	}
	
	@Override
	public List<VasStroge> findByIds(String[] ids) {
		StringBuffer idsa = new StringBuffer();
		for (String id : ids) {
			idsa.append("'").append(id).append("',");
		}
		String idsb = idsa.toString().replaceAll(",$", "");
		return vasStrogeDao.findByIds(idsb);
	}
	
	public List<String> getShortPart(Integer id){
		return vasStrogeDao.getShortPart(id);
	}

}
