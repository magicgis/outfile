package com.naswork.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.naswork.dao.ClientInquiryDao;
import com.naswork.dao.DasiDao;
import com.naswork.dao.DasiMessageDao;
import com.naswork.dao.UserDao;
import com.naswork.model.ClientInquiry;
import com.naswork.model.Dasi;
import com.naswork.model.DasiMessage;
import com.naswork.service.DasiService;
import com.naswork.utils.QQMail;
import com.naswork.vo.UserVo;

@Service("dasiService")
public class DasiServiceImpl implements DasiService {

	@Resource
	private DasiDao dasiDao;
	@Resource
	private DasiMessageDao dasiMessageDao;
	@Resource
	private ClientInquiryDao clientInquiryDao;
	@Resource
	private UserDao userDao;
	
	@Override
	public void insertSelective(Dasi record) {
		dasiDao.insertSelective(record);
	}

	@Override
	public Dasi selectByPrimaryKey(Integer id) {
		return dasiDao.selectByPrimaryKey(id);
	}

	@Override
	public void updateByPrimaryKeySelective(Dasi record) {
		dasiDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<Dasi> getCompleteList() {
		return dasiDao.getCompleteList();
	}
	
	public void sendDasiEmail(){
		try {
			List<Dasi> list = dasiDao.getCompleteList();
			for (int i = 0; i < list.size(); i++) {
				List<DasiMessage> elementList = dasiMessageDao.getCrawlMessage(list.get(i).getId());
				if (elementList.size() > 0) {
					StringBuffer bodyText = new StringBuffer();
					bodyText.append("<div><table cellspacing=\"0\" width=\"100%\" class=\"ms-rteTable-default\" style=\"border-collapse:collapse; border: 1px solid rgb(198, 198, 198);\">"
							+ "<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
							+"件号"
							+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
							+"库存数量"
							+"</td></tr>"
							);
					for (DasiMessage dasiMessage : elementList) {
						bodyText.append("<tr><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+ dasiMessage.getPartNumber()
								+ "</td><td class=\"ms-rteTable-default\" style=\"border-collapse: collapse; border: 1px solid rgb(198, 198, 198); width: 14.2857%;\">"
								+ dasiMessage.getStorageAmount()
								+"</td></tr>"
								);
					}
					bodyText.append("</table></div>");
					ClientInquiry clientInquiry = clientInquiryDao.selectByPrimaryKey(list.get(i).getClientInquiryId());
					QQMail qqMail = new QQMail();
					qqMail.setUserName("3455816934@qq.com");
		    		qqMail.setPassWord("bzrzvaddkocrchba");
					qqMail.setTitle(clientInquiry.getQuoteNumber()+"询价单Dasi库存件号列表");
					qqMail.setBodyText(bodyText.toString());
					List<UserVo> userList = userDao.getByRole("国外采购");
					List<String> emailList = new ArrayList<String>();
					/*emailList.add("915736664@qq.com");
					emailList.add("tanoy@naswork.com");*/
					emailList.add("3131660631@qq.com");//kris 
					emailList.add("2026458660@qq.com");//limmy 
					emailList.add("3508643956@qq.com");//renee 
					emailList.add("3335454113@qq.com");//cassie 
					emailList.add("2326155059@qq.com ");//Saidy
					qqMail.setToList(emailList);
					
					qqMail.sendEmail();
				}
				list.get(i).setSendStatus(1);
				dasiDao.updateByPrimaryKeySelective(list.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
