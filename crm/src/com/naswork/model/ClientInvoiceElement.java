package com.naswork.model;

import java.util.Date;
import java.util.List;

import com.naswork.module.finance.controller.clientinvoice.ClientInvoiceExcelVo;
import com.naswork.module.finance.controller.clientinvoice.ElementDataVo;

public class ClientInvoiceElement {
    private Integer id;

    private Integer clientInvoiceId;

    private Integer clientOrderElementId;
    
    private Double amount;

    private String remark;

    private Date updateTimestamp;

    private Integer terms;
    
    private List<ClientInvoiceElement> list;
    
    private List<ClientInvoiceExcelVo> voList;
    
    private String dataItem;
    
    private Double invoiceTotal;
    
    private Double orderTotal;
    
    public String getDataItem() {
    	return dataItem;
    }

    public void setDataItem(String dataItem) {
    	this.dataItem = dataItem;
    }

	public List<ClientInvoiceElement> getList() {
		return list;
	}

	public void setList(List<ClientInvoiceElement> list) {
		this.list = list;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientInvoiceId() {
        return clientInvoiceId;
    }

    public void setClientInvoiceId(Integer clientInvoiceId) {
        this.clientInvoiceId = clientInvoiceId;
    }

    public Integer getClientOrderElementId() {
        return clientOrderElementId;
    }

    public void setClientOrderElementId(Integer clientOrderElementId) {
        this.clientOrderElementId = clientOrderElementId;
    }



	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public Integer getTerms() {
        return terms;
    }

    public void setTerms(Integer terms) {
        this.terms = terms;
    }

	public List<ClientInvoiceExcelVo> getVoList() {
		return voList;
	}

	public void setVoList(List<ClientInvoiceExcelVo> voList) {
		this.voList = voList;
	}

	public Double getInvoiceTotal() {
		return invoiceTotal;
	}

	public void setInvoiceTotal(Double invoiceTotal) {
		this.invoiceTotal = invoiceTotal;
	}

	public Double getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(Double orderTotal) {
		this.orderTotal = orderTotal;
	}
}