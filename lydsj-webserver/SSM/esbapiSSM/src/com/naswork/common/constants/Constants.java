/**
 * 
 */
package com.naswork.common.constants;

/**
 * @since 2016年3月16日 下午2:34:06
 * @author doudou<doudou@naswork.com>
 * @version v1.0
 */
public class Constants {

	public static final int PAGESIZE = 15;

	/*
	*定义开始时间START_TIME和结束时间END_TIME
	 */
	public static final String START_TIME = "08:30:00";
	public static final String END_TIME = "19:30:00";
	
	/**
	 * 会话中的用户名
	 */
	public static final String SESSION_USER = "pj_main_session_user";
	
	public static final int APPROVAL_STATUS_NONE = -1;
	
	public static final int APPROVAL_STATUS_DRAFT = 0;
	
	public static final int APPROVAL_STATUS_SENT = 1;
	
	public static final int APPROVAL_STATUS_RECEIVED = 2;
	
	public static final int APPROVAL_STATUS_APPROVED = 3;
	
	public static final int APPROVAL_STATUS_REJECTED = 4;
	
	public static final int APPROVAL_STATUS_CANCELLED = 5;
	
	/**
	 * 文书字轨枚举
	 * @since 2016年5月7日 下午12:04:05
	 * @author xiaohu
	 * @version v1.0
	 */
	public enum WszgEnum{
		/** 选案交办  */
		XJ("0101"),
		
		/** 选案转办  */
		XZ("0102"),
		
		/** 举报交办  */
		JJ("0201"),
		
		/** 举报转办  */
		JZ("0202");
		
		private String value;
		
		private WszgEnum(String value){
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		
	}
	
	
	public enum WsDmEnum{
		
		/** 税务局稽查局转办函  */
		ZBH("A005"),
		/** 税收违法案件交办函  */
		SSWFJBH("A006"),
		/** 税收违法信息记录单 */
		WFXXJLD("B001"),
		/** 检举税收违法行为受理回执  */
		JJ_WFXWSLHZ("B002"),
		/** 税收违法检举案件督办函  */
		DBH("B004"),
		/** 税收违法检举事项交办函  */
		JBH("B005"),
		/** 税收违法检举案件延期查处申请  */
		JJ_AJYQCCSQ("B006"),
		/** 税收违法检举案件催办函  */
		CBH("B008"),
		/** 检举税收违法行为查办结果简要告知书 */
		JJ_CBJGJYGZS("B009"),
		/** 检举纳税人税收违法行为领奖审批表  */
		JJ_JLSPB("B011"),
		/** 检举纳税人税收违法行为领奖通知书  */
		JJ_LJTZS("B012"),
		/** 检举纳税人税收违法行为奖金领款财务凭证  */
		JJ_LKZYPZ("B013"),
		/** 检举纳税人税收违法行为付款专用凭证  */
		JJ_FKZYPZ("B014"),
		/** 税务行政执法审批表 */
		C_SWJZZFSPB("C010"),
	
		
		/** 自查通知书  */
		ZCTZS("1"),
		/** 自查约谈通知书  */
		ZCYTTZS("2"),
		/** 纳税异常质询书  */
		NSYCZXS("3"),
		/** 自查补税告知书  */
		ZCBSGZS("4"),
		/** 决定性文书撤销  */
		JDXWSCX("5"),
		/** 税务事项通知书  */
		SWSXTZS("6"),
		/** 同级交办单  */
		TJJBH("11"),
		/** 延期查处批复  */
		YQCCPF("12"),
		/** 延期申请-延复  */
		YQSQYF("13"),
		/** 关于退还账簿资料的通知  */
		THZBZLTZS("14"),
		/** 税务稽查案件委托协查函  */
		WTXCH("15"),
		/** 执行_税务事项通知书  */
		ZX_SWSXTZS("16"),
		/** 执行_强制执行催告书  */
		ZX_QZZXCGS("17"),
		/** 税务检查通知书  */
		SWJCTZS("18"),
		/** 询问通知书  */
		XWTZS("19"),
		/** 协查复函  */
		XCFH("20"),
		/** 税收违法案件转办函  */
		SSWFZBH("24"),
		/** 辅导自查任务通知书  */
		FDZCRWTZS("25"),
		/** 协查事项任务通知书 */
		XCRWTZS("26"),
		/** 稽查案件查前准备通知书  */
		CQZBTZS("27"),
		/** 完成稽查案件查前准备意见书  */
		CQZBYJS("29"),
		/** 调取账簿资料通知书  */
		DQZBZLTZS("30"),
		/** 检查存款账户许可证明  */
		JCZHCKTZS("31"),
		/** 选案案源线索编号  */
		AYXSBH("32"),
		/** 案源受理号 */
		AYSLH("33"),
		/** 税收违法案件协查回复函 */
		XCHFH("34");
		

		private String value;
		
		private WsDmEnum(String value){
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

}
