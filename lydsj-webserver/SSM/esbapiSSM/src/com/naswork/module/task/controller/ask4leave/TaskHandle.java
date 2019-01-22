package com.naswork.module.task.controller.ask4leave;

import org.jbpm.api.jpdl.DecisionHandler;
import org.jbpm.api.model.OpenExecution;

public class TaskHandle implements DecisionHandler{

	private static final long serialVersionUID = -3623377103696000971L;

	public String decide(OpenExecution exec) {
		return "接收";
	}
	
}