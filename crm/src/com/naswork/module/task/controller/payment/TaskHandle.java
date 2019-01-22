package com.naswork.module.task.controller.payment;

import org.jbpm.api.jpdl.DecisionHandler;
import org.jbpm.api.model.OpenExecution;

public class TaskHandle implements DecisionHandler{

	private static final long serialVersionUID = -3623377103696000971L;

	public String decide(OpenExecution exec) {
		String to = exec.getVariables().get("to").toString();
		return to;
		
	}
	
}