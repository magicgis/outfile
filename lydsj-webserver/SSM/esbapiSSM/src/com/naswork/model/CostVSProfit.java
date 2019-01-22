package com.naswork.model;

import java.io.Serializable;

/**
 * Copyright: Copyright (c) 2018 cheng
 * 
 * @Description 成本和利润
 * @version v1.0.0
 * @author cheng
 * @date 2018年11月2日 下午5:13:25
 * 
 */
public class CostVSProfit implements Serializable {

	private static final long serialVersionUID = 7648140575632677853L;
	private Double cost;// 成本
	private Double profit;// 利润

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

}
