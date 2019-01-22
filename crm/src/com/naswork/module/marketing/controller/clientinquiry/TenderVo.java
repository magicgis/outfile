package com.naswork.module.marketing.controller.clientinquiry;

import java.util.List;

import com.naswork.model.Competitor;
import com.naswork.model.CompetitorQuote;
import com.naswork.model.CompetitorQuoteElement;

public class TenderVo {

	private List<Competitor> competitorList;
	
	private List<CompetitorQuoteElement> competitorQuoteElementList;
	
	private List<CompetitorQuote> competitorQuoteList;

	public List<Competitor> getCompetitorList() {
		return competitorList;
	}

	public void setCompetitorList(List<Competitor> competitorList) {
		this.competitorList = competitorList;
	}

	public List<CompetitorQuoteElement> getCompetitorQuoteElementList() {
		return competitorQuoteElementList;
	}

	public void setCompetitorQuoteElementList(
			List<CompetitorQuoteElement> competitorQuoteElementList) {
		this.competitorQuoteElementList = competitorQuoteElementList;
	}

	public List<CompetitorQuote> getCompetitorQuoteList() {
		return competitorQuoteList;
	}

	public void setCompetitorQuoteList(List<CompetitorQuote> competitorQuoteList) {
		this.competitorQuoteList = competitorQuoteList;
	}

	
}
