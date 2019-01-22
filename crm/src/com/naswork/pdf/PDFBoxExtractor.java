package com.naswork.pdf;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class PDFBoxExtractor extends PDFExtractor {

	private PDDocument document;

	@Override
	public void open(String fileName) throws IOException {
		this.document =  PDDocument.load( new File(fileName) );
		this.numOfPage = this.document.getNumberOfPages();
	}

	@Override
	public String[] extract(int pageNo) throws IOException {
		if(pageNo > this.numOfPage){
			return null;
		}
		PDFTextStripperByArea stripper = new PDFTextStripperByArea();
		stripper.setSortByPosition( true );
		Rectangle rect = new Rectangle(0, 0, 1000, 1000 );
		stripper.addRegion( "class1", rect );
		PDPage page = this.document.getPage(pageNo-1);
		stripper.extractRegions(page);
		String regionText = stripper.getTextForRegion( "class1" );
		return regionText.split("\n");
	}
	
	public void close() throws IOException{
		this.document.close();
	}

}
