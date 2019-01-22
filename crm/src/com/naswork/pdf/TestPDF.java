package com.naswork.pdf;

import java.io.IOException;

public class TestPDF {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//testBasicPDFBox();
		//extractVendor();
		extractPart();
	}
	
	public static void testBasicPDFBox() throws IOException{
		String fileName = "F:\\Material\\outsource\\mis\\data\\test\\IPCpd0f\\IPCpdf\\Data\\NIPCMU_000003.pdf";
		String outputFile = "F:\\tmp\\NIPCMU_000003.txt";
		PDFExtractor extractor = new PDFBoxExtractor();
		extractor.open(fileName);
		String[] lines = extractor.extract(38);
		extractor.writeToFile(outputFile, lines, true);
		System.out.println("Write to file");
	}
	
	public static void extractVendor() throws IOException{
		String fileName = "F:\\Material\\outsource\\mis\\data\\test\\IPCpd0f\\IPCpdf\\Data\\NIPCMU_000002.pdf";
		String outputFile = "F:\\tmp\\vendor.txt";
		PDFExtractor extractor = new PDFBoxExtractor();
		extractor.open(fileName);
		for(int pageNo=71; pageNo<192; pageNo++){
			String[] lines = extractor.extract(pageNo);
			extractor.writeToFile(outputFile, lines, true);
			System.out.println("Finish write page " + pageNo);
		}
	}
	
	public static void extractPart() throws IOException{
		String fileName = "G:\\CRM项目\\CRM文档\\Quotation #C164261 3-9-2017 5-26-59 AM (002).pdf";
		String outputFile = "G:\\CRM项目\\CRM文档\\";
		AirBusPDFParser parser = new AirBusPartParser(fileName);
		int count =0;
		for(int pageNo=1; pageNo<=1; pageNo++){
			parser.parse(pageNo, outputFile);
			count++;
			if(count/100 * 100 == count){
				System.out.println("Finished "+count+" pages");
			}
		}
		System.out.println("Finished "+count+" pages");
	}

}
