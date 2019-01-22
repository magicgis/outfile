package com.naswork.utils;


import org.apache.log4j.Logger;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * @since 2018-01-05
 * @author Tanoy
 *
 */
public class QQMail {
	
	private String userName; // 3537552702@qq.com
	
	private String passWord;//授权码：hvnrmypnneqwdbbf
	
	private String bodyText;
	
	private List<String> toList;
	
	private InternetAddress[] to;
	
	private String title;
	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the passWord
	 */
	public String getPassWord() {
		return passWord;
	}

	/**
	 * @param passWord the passWord to set
	 */
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	/**
	 * @return the bodyText
	 */
	public String getBodyText() {
		return bodyText;
	}

	/**
	 * @param bodyText the bodyText to set
	 */
	public void setBodyText(String bodyText) {
		this.bodyText = bodyText;
	}

	/**
	 * @return the toList
	 */
	public List<String> getToList() {
		return toList;
	}

	/**
	 * @param toList the toList to set
	 * @throws AddressException 
	 */
	public void setToList(List<String> toList) throws AddressException {
		this.toList = toList;
		InternetAddress[] internetAddress=new InternetAddress[toList.size()];
		for (int i = 0; i < toList.size(); i++) {
			internetAddress[i] = new InternetAddress(toList.get(i));
		}
		setTo(internetAddress);
	}

	/**
	 * @return the to
	 */
	public InternetAddress[] getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(InternetAddress[] to) {
		this.to = to;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	private static Logger logger = Logger.getLogger(QQMail.class);

	/**
	 * 发送QQ邮件
	 */
	public boolean sendEmail() {
		boolean flag = false;

		try {
			Properties properties = new Properties();

			/*properties.put("mail.transport.protocol", "smtp");// 连接协议

			properties.put("mail.smtp.host", "smtp.qq.com");// 主机名

			properties.put("mail.smtp.port", 465);// 端口号

			properties.put("mail.smtp.auth", "true");

			properties.put("mail.smtp.ssl.enable", "true");// 设置是否使用ssl安全连接 

			properties.put("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息
*/
			properties.setProperty("mail.host", "smtp.qq.com");
			properties.setProperty("mail.transport.protocol", "smtp");
			properties.setProperty("mail.smtp.auth", "true");
			properties.setProperty("email.smtp.timeout", "25000");
			properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			properties.setProperty("mail.smtp.port", "465");
			properties.setProperty("mail.smtp.socketFactory.port", "465");
			logger.info("询价单："+title+"  邮件发送处理---------属性设置正确");
			/*// 开启SSL加密，否则会失败
			MailSSLSocketFactory sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
			properties.put("mail.smtp.ssl.enable", "true");
			properties.put("mail.smtp.ssl.socketFactory", sf);*/
			
			// 得到回话对象
			Session session = Session.getInstance(properties);

			// 获取邮件对象
			Message message = new MimeMessage(session);

			logger.info("询价单："+title+"  邮件发送处理---------获取邮件对象正确");
			// 设置发件人邮箱地址
			message.setFrom(new InternetAddress(userName));

			// 设置收件人地址
			message.setRecipients( RecipientType.TO, to);
			
			// 设置邮件格式，与邮件内容
			message.setContent(bodyText,"text/html;charset=UTF-8");

			// 设置邮件标题
			message.setSubject(title);

			logger.info("询价单："+title+"  邮件发送处理---------设置邮件信息正确");

			// 设置邮件内容
			//message.setText(bodyText);

			// 得到邮差对象
			Transport transport = session.getTransport("smtp");

			// 连接自己的邮箱账户,密码为刚才得到的授权码
			transport.connect(userName, passWord);
			
			//发送邮件
			transport.sendMessage(message, message.getAllRecipients());

			logger.info("询价单："+title+"  邮件发送处理---------发送邮件正确");

			transport.close();

			//发送成功标志
			flag = true;

			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("发送邮件出现异常"+e.getMessage());
		}
		return flag;
	}

}



