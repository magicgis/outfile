package com.naswork.model;

import java.util.Date;

public class CompetitorQuoteElement {
    private Integer id;

    private Integer competitorQuoteId;

    private Integer clientInquiryElementId;

    private Double price;

    private Date updateTimestamp;
    
    private String currencyValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompetitorQuoteId() {
        return competitorQuoteId;
    }

    public void setCompetitorQuoteId(Integer competitorQuoteId) {
        this.competitorQuoteId = competitorQuoteId;
    }

    public Integer getClientInquiryElementId() {
        return clientInquiryElementId;
    }

    public void setClientInquiryElementId(Integer clientInquiryElementId) {
        this.clientInquiryElementId = clientInquiryElementId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

	public String getCurrencyValue() {
		return currencyValue;
	}

	public void setCurrencyValue(String currencyValue) {
		this.currencyValue = currencyValue;
	}
}