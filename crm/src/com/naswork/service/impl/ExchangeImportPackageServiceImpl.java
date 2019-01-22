package com.naswork.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ExchangeImportPackageDao;
import com.naswork.dao.UserDao;
import com.naswork.model.ExchangeImportPackage;
import com.naswork.module.marketing.controller.clientinquiry.MessageVo;
import com.naswork.service.ExchangeImportPackageService;
import com.naswork.utils.ConvertUtil;
import com.naswork.utils.ExchangeMail;
import com.naswork.vo.GridSort;
import com.naswork.vo.PageModel;
import com.naswork.vo.UserVo;

@Service("exchangeImportPackageService")
public class ExchangeImportPackageServiceImpl implements
		ExchangeImportPackageService {
	@Resource
	private ExchangeImportPackageDao exchangeImportPackageDao;
	@Resource
	private UserDao userDao;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return exchangeImportPackageDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ExchangeImportPackage record) {
		return exchangeImportPackageDao.insert(record);
	}

	@Override
	public int insertSelective(ExchangeImportPackage record) {
		return exchangeImportPackageDao.insertSelective(record);
	}

	@Override
	public ExchangeImportPackage selectByPrimaryKey(Integer id) {
		return exchangeImportPackageDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ExchangeImportPackage record) {
		return exchangeImportPackageDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(ExchangeImportPackage record) {
		return exchangeImportPackageDao.updateByPrimaryKey(record);
	}

	@Override
	public void warnListPage(
			PageModel<ExchangeImportPackage> page, String where,
			GridSort sort) {
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(exchangeImportPackageDao.warnListPage(page));
	}
	
	public void listPage(PageModel<ExchangeImportPackage> page, String where,
			GridSort sort){
    	page.put("where", where);
		if(sort!=null){
			sort.setName(ConvertUtil.toDBName(sort.getName()));
			String sotr=ConvertUtil.convertSort(sort);
			page.put("orderby", ConvertUtil.convertSort(sort));
		}else{
			page.put("orderby", null);
		}
		
		page.setEntities(exchangeImportPackageDao.listPage(page));
	}
	
	public MessageVo email(UserVo userVo){
		try {
			List<Integer> userList = exchangeImportPackageDao.getEmailUserId();
			for (int i = 0; i < userList.size(); i++) {
				List<ExchangeImportPackage> list = exchangeImportPackageDao.getEmailList(userList.get(i));
				if (list.size() > 0) {
					StringBuffer bodyText = new StringBuffer();
					bodyText.append("<div>请看系统维修仓 </div>");
					bodyText.append("<div><table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">"
							+"<td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
							+ "客户"
							+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
							+"订单"
							+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
							+"件号"
							+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
							+"数量"
							+"</td></tr>"
							);
					for (int j = 0; j < list.size(); j++) {
						bodyText.append("<div><table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">"
								+"<td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+ list.get(j).getClientCode()
								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+list.get(j).getOrderNumber()
								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+list.get(j).getPartNumber()
								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+list.get(j).getAmount()
								+"</td></tr>"
								);
					}
					bodyText.append("</table></div>");
					bodyText.append("<div>请知悉，谢谢！！！ </div>");
					try {
						ExchangeMail exchangeMail = new ExchangeMail();
						UserVo userVo2 = userDao.findById(new Integer(userVo.getUserId()));
						UserVo touser = userDao.findById(userList.get(i));
						exchangeMail.setUsername(userVo2.getEmail());
						exchangeMail.setPassword(userVo2.getEmailPassword());
						List<String> cc = new ArrayList<String>();
						List<String> to = new ArrayList<String>();
						List<String> bcc = new ArrayList<String>();
						cc.add(userVo2.getEmail());
						cc.add("kris@betterairgroup.com");
						cc.add("michelle@betterairgroup.com");
						to.add(touser.getEmail());
						exchangeMail.init();
						exchangeMail.doSend("客户交换件/维修件情况", to, cc, bcc, bodyText.toString(), "");
						for (int j = 0; j < list.size(); j++) {
							ExchangeImportPackage exchangeImportPackage = exchangeImportPackageDao.selectByPrimaryKey(list.get(j).getId());
							exchangeImportPackage.setEmailStatus(1);
							exchangeImportPackageDao.updateByPrimaryKeySelective(exchangeImportPackage);
						}
					} catch (Exception e) {
						e.printStackTrace();
						return new MessageVo(false,"邮件发送异常!");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new MessageVo(false,"邮件发送异常!");
		}
		return new MessageVo(true,"邮件发送成功!");
	}

}
