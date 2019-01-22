package com.naswork.pdf;

import java.io.IOException;

public abstract class AirBusPDFParser {
	
	protected PDFExtractor extractor;
	
	public AirBusPDFParser(String fileName) throws IOException{
		this.extractor = new PDFBoxExtractor();
		this.extractor.open(fileName);
	}
	
	protected String[] read(int pageNo) throws IOException{
		return this.extractor.extract(pageNo);
	}
	
	public void parse(int pageNo, String fileName) throws IOException{
		String[] lines = this.read(pageNo);
		this.extractor.writeToFile(fileName, lines, true);
	}
}
