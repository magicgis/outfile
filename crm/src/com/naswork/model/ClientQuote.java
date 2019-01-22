package com.naswork.model;

import java.util.Date;

public class ClientQuote {
	private String ids;
	
	private Double prepayRate;
	
	private Double shipPayRate;
	
	private Integer shipPayPeriod;
	
	private Double receivePayRate;
	
	private Integer receivePayPeriod;
	
	private Integer id;

    private Integer clientInquiryId;

    private Integer seq;

    private String quoteNumber;

    private Date quoteDate;

    private Integer currencyId;

    private Double exchangeRate;

    private Date updateTimestamp;

    private Double profitMargin;
    
    private Integer termsOfDelivery;
    
    private String termsOfDeliveryValue;
    
    private Double freight;
    
    private Double lowestFreight;
    
    private Double fixedCost;
    
    private Double bankCost;

    public String getTermsOfDeliveryValue() {
		return termsOfDeliveryValue;
	}

	public void setTermsOfDeliveryValue(String termsOfDeliveryValue) {
		this.termsOfDeliveryValue = termsOfDeliveryValue;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientInquiryId() {
        return clientInquiryId;
    }

    public void setClientInquiryId(Integer clientInquiryId) {
        this.clientInquiryId = clientInquiryId;
    }

    public Integer getSeq() {
        return seq;
    }

    public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getQuoteNumber() {
        return quoteNumber;
    }

    public void setQuoteNumber(String quoteNumber) {
        this.quoteNumber = quoteNumber;
    }

    public Date getQuoteDate() {
        return quoteDate;
    }

    public void setQuoteDate(Date quoteDate) {
        this.quoteDate = quoteDate;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }


	public Integer getShipPayPeriod() {
		return shipPayPeriod;
	}

	public void setShipPayPeriod(Integer shipPayPeriod) {
		this.shipPayPeriod = shipPayPeriod;
	}

	public Integer getReceivePayPeriod() {
		return receivePayPeriod;
	}

	public void setReceivePayPeriod(Integer receivePayPeriod) {
		this.receivePayPeriod = receivePayPeriod;
	}

	public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public Double getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(Double profitMargin) {
        this.profitMargin = profitMargin;
    }

	public Double getPrepayRate() {
		return prepayRate;
	}

	public void setPrepayRate(Double prepayRate) {
		this.prepayRate = prepayRate;
	}

	public Double getShipPayRate() {
		return shipPayRate;
	}

	public void setShipPayRate(Double shipPayRate) {
		this.shipPayRate = shipPayRate;
	}

	public Double getReceivePayRate() {
		return receivePayRate;
	}

	public void setReceivePayRate(Double receivePayRate) {
		this.receivePayRate = receivePayRate;
	}

	public Integer getTermsOfDelivery() {
		return termsOfDelivery;
	}

	public void setTermsOfDelivery(Integer termsOfDelivery) {
		this.termsOfDelivery = termsOfDelivery;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Double getLowestFreight() {
		return lowestFreight;
	}

	public void setLowestFreight(Double lowestFreight) {
		this.lowestFreight = lowestFreight;
	}

	public Double getFixedCost() {
		return fixedCost;
	}

	public void setFixedCost(Double fixedCost) {
		this.fixedCost = fixedCost;
	}

	/**
	 * @return the bankCost
	 */
	public Double getBankCost() {
		return bankCost;
	}

	/**
	 * @param bankCost the bankCost to set
	 */
	public void setBankCost(Double bankCost) {
		this.bankCost = bankCost;
	}
	
	


}