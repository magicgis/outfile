package com.naswork.model;

public class HighPriceCrawlQuote {
    private Integer id;

    private Integer supplierQuoteId;

    private Integer isSend;
    
    private String person;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierQuoteId() {
        return supplierQuoteId;
    }

    public void setSupplierQuoteId(Integer supplierQuoteId) {
        this.supplierQuoteId = supplierQuoteId;
    }

    public Integer getIsSend() {
        return isSend;
    }

    public void setIsSend(Integer isSend) {
        this.isSend = isSend;
    }

	/**
	 * @return the person
	 */
	public String getPerson() {
		return person;
	}

	/**
	 * @param person the person to set
	 */
	public void setPerson(String person) {
		this.person = person;
	}
}