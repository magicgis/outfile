package com.naswork.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.GyHelpDao;
import com.naswork.model.gy.GyHelp;
import com.naswork.service.GyHelpService;
import com.naswork.utils.ConvertUtil;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;

/**
 * @since 2016年05月05日 15:52:23
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
@Service("gyHelpService")
public class GyHelpServiceImpl implements GyHelpService{

	@Resource
	private GyHelpDao gyHelpDao;
	
	 /**
	 * 根据主键查询对象
	 * @param helpUuid
	 * @return
	 * @since 2016年05月05日 15:52:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public GyHelp findById(String helpUuid){
		return gyHelpDao.findById(helpUuid);
	}
	
	/**
	 * 新增
	 * @param gyHelp
	 * @since 2016年05月05日 15:52:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void add(GyHelp gyHelp){
		gyHelpDao.insert(gyHelp);
		
	}
	
	/**
	 * 更新
	 * @param gyHelp
	 * @since 2016年05月05日 15:52:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void modify(GyHelp gyHelp){
		gyHelpDao.update(gyHelp);
	}
	
	/**
	 * 删除
	 * @param helpUuid
	 * @since 2016年05月05日 15:52:23
	 * @author doudou<doudou@naswork.com>
	 * @version v1.0
	 */
	public void remove(String helpUuid){
		gyHelpDao.delete(helpUuid);
	}

	@Override
	public void searchPage(PageModel<GyHelp> page, String where,
			GridSort sort) {
		page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		page.setEntities(gyHelpDao.findPage(page));
	}
	
	@Override
	public GyHelp findByCode(String code) {
		List<GyHelp> helps = gyHelpDao.findByCode(code);
		if(helps!=null && helps.size()>0){
			return helps.get(0);
		}
		return null;
	}

}