package com.naswork.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import jxl.biff.RecordData;

import org.springframework.stereotype.Service;

import com.naswork.dao.AuthorityRelationDao;
import com.naswork.dao.CompetitorDao;
import com.naswork.dao.SupplierClassifyDao;
import com.naswork.dao.SupplierDao;
import com.naswork.dao.UserDao;
import com.naswork.model.AuthorityRelation;
import com.naswork.model.Competitor;
import com.naswork.model.Supplier;
import com.naswork.model.SupplierClassify;
import com.naswork.module.xtgl.controller.PowerVo;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.ExchangeMail;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.RoleVo;
import com.naswork.vo.UserVo;
@Service("supplierservice")
public class SupplierServiceImpl implements SupplierService {

	@Resource
	private SupplierDao supplierDao;
	@Resource
	private SupplierClassifyDao supplierClassifyDao;
	@Resource
	private AuthorityRelationDao authorityRelationDao;
	@Resource
	private UserDao userDao;
	@Resource
	private RoleService roleService;
	@Resource
	private CompetitorDao competitorDao;
	
	@Override
	public void listPage(PageModel<Supplier> page, String searchString, GridSort sort) {
		page.put("where", searchString);
		if(sort != null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		List<Supplier> suppliers=supplierDao.listPage(page);
		
		page.setEntities(suppliers);
		
	}

	@Override
	public void insertSelective(Supplier record,List<UserVo> userVos) {
		supplierDao.insertSelective(record);
		String s=record.getSupplierClassifyId();
		String[] supplierClassifyId=s.split(",");
		SupplierClassify supplierClassify=new SupplierClassify();
		for (int i = 0; i < supplierClassifyId.length; i++) {
			supplierClassify.setSupplierClassifyId(supplierClassifyId[i]);
			supplierClassify.setSupplierId(record.getId());
			supplierClassifyDao.insert(supplierClassify);
		}
		for (UserVo userVo : userVos) {
			PowerVo powerVo=new PowerVo();
			powerVo.setSupplierId(record.getId());
			powerVo.setUserId(new Integer(userVo.getUserId()));
			authorityRelationDao.insertSelective(powerVo);
		}
		if (new Integer(record.getCode()) > 1000) {
			//新增竞争对手
			Competitor competitor = new Competitor();
			competitor.setCurrencyId(record.getCurrencyId());
			competitor.setCode(record.getCode());
			competitor.setName(record.getName());
			competitorDao.insertSelective(competitor);
		}
	}

	@Override
	public Supplier findByCode(String code) {
		return supplierDao.findByCode(code);
	}

	@Override
	public Supplier selectByPrimaryKey(Integer id) {
		return supplierDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(Supplier record) {
		supplierDao.updateByPrimaryKey(record);
		SupplierClassify supplierClassify=new SupplierClassify();
		supplierClassifyDao.deleteByPrimaryKey(record.getId());
		String s=record.getSupplierClassifyId();
		String[] supplierClassifyId=s.split(",");
		for (int i = 0; i < supplierClassifyId.length; i++) {
			supplierClassify.setSupplierClassifyId(supplierClassifyId[i]);
			supplierClassify.setSupplierId(record.getId());
			supplierClassifyDao.insert(supplierClassify);
		}
//		AuthorityRelation authorityRelation=new AuthorityRelation();
//		authorityRelation.setSupplierId(record.getId());
//		authorityRelation.setUserId(record.getOwner());
//		authorityRelationDao.updateBySupplierId(authorityRelation);
	}
	
	public Integer getCurrencyId(String code){
		return supplierDao.getCurrencyId(code);
	}

	@Override
	public Supplier findByShortName(String shortName) {
		return supplierDao.findByShortName(shortName);
	}

	@Override
	public Supplier findByName(String name) {
		return supplierDao.findByName(name);
	}

	public Integer findByUrl(String url){
		return supplierDao.findByUrl(url);
	}
	
	public List<Supplier> Suppliers(PageModel<Supplier> page) {
		return supplierDao.Suppliers(page);
	}

	@Override
	public List<Supplier> selectAll() {
		return supplierDao.selectAll();
	}
	
	public void getOutTimeSupplier(){
		List<UserVo> userList = userDao.getUsers();
		for (UserVo userVo : userList) {
			StringBuffer text = new StringBuffer();
			List<Supplier> list = supplierDao.getOutTimeSupplier(new Integer(userVo.getUserId()));
			for (Supplier supplier : list) {
				StringBuffer date = new StringBuffer();
				Calendar cal = Calendar.getInstance();
				cal.setTime(supplier.getAptitude());
				date.append(cal.get(Calendar.YEAR)).append("年")
					 .append(cal.get(Calendar.MONTH)+1).append("月")
					 .append(cal.get(Calendar.DAY_OF_MONTH)).append("日")
					 .append(supplier.getCode()).append("号");
				text.append("<div>供应商").append(supplier.getCode()).append("的资质在").append(date).append("过期，</div>");
			}
			List<String> email = new ArrayList<String>();
			List<String> cc = new ArrayList<String>();
			List<String> bcc = new ArrayList<String>();
			String realPath = "";
			if (userVo.getEmail()!=null && !"".equals(userVo.getEmail()) && text.length()>0) {
				email.add(userVo.getEmail());
				ExchangeMail exchangeMail = new ExchangeMail();
				exchangeMail.setUsername(userVo.getEmail());
				exchangeMail.setPassword(userVo.getEmailPassword());
				exchangeMail.init();
				try {
					if (userVo.getEmailPassword() != null && !"".equals(userVo.getEmailPassword())) {
						exchangeMail.doSend("供应商资质过期提醒", email, cc, bcc, text.toString(), realPath);
						for (int i = 0; i < list.size(); i++) {
							list.get(i).setLastWarnTime(new Date());
							supplierDao.updateByPrimaryKeySelective(list.get(i));
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					if (userVo.getEmailPassword() != null && !"".equals(userVo.getEmailPassword())) {
						for (int i = 0; i < list.size(); i++) {
							list.get(i).setLastWarnTime(null);
							supplierDao.updateByPrimaryKeySelective(list.get(i));
						}
					}	
				}
			}
			
		}
		
	}
	
	public Supplier selectByCode(String code){
		return supplierDao.selectByCode(code);
	}
	
	public List<Integer> checkByCode(String code){
		return supplierDao.checkByCode(code);
	}

	@Override
	public List<Supplier> getSupplierByCodeAndUserId(String userId, String supplierCode) {
		return supplierDao.getSupplierByCodeAndUserId(userId,supplierCode);
	}

}
