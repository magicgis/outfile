package com.naswork.module.workflow.constants;

public class WorkFlowConstants {
	/**
	 * 主要任务处理页面
	 */
	public static final String MAIN_VIEW = "/workflow/dealTask_main";
	
	//审批提交参数名称（备注）
   	public static final String WORKFLOW_COMMENT = "wf_comment";
   	
   	//审批提交参数名称（意见）
	public static final String WORKFLOW_OUTCOME = "wf_outcome";
	
	//指定下一节点审批人员的参数名称（多个用户名以分号【;】隔开）
	public static final String ASSIGN_CONSIGNOR = "consignor";

	//任务发起人
	public static final String START_USER = "start_user";
	
	//任务描述信息
	public static final String TASK_INFO = "task_info";

	//默认提交方法
	public static final String DEFAULT_SUBMIT_URL = "/workflow/completeTask";
	
	//默认提交方法
	public static final String DEFAULT_TASKINFO_URL = "/workflow/taskInfo";
	
	/**
	 * 审批状态
	 * @since 2016年5月17日 下午7:55:04
	 * @author chenguojun<chengj@everygold.com>
	 * @version v1.0
	 */
	public enum SpztEnum {
		DENG_JI("0", "登记"),
		SHENG_HE_ZHONG("232", "审核中"),
		BU_TONG_GUO("233", "不通过"),
		TONG_GUO("235", "审核通过");
		
		private final String value;
		private final String text;
		private SpztEnum(String value, String text) {
			this.value = value;
			this.text = text;
		}
		@Override
		public String toString() {
			return value;
		}
		public String toText() {
			return text;
		}
		public String toValue() {
			return value;
		}
	}
	
	/**
	 *  流程key
	 *  @author Giam
	 *
	 */
	public enum ProcessKeys {
		TestProcess("test", "测试流程")
		
		;

		private final String value;
		private final String text;
		private ProcessKeys(String value, String text) {
			this.value = value;
			this.text = text;
		}
		@Override
		public String toString() {
			return value;
		}
		public String toText() {
			return text;
		}
		public String toValue() {
			return value;
		}
	}
}
