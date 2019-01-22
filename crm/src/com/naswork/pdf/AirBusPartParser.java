package com.naswork.pdf;

import java.io.IOException;

public class AirBusPartParser extends AirBusPDFParser {
	public static final int FOOT_LINE_NUM = 3;
	public static final String FIGURE_KEYWORD = "FIGURE";
	public static final String CONTINUE_KEYWORD = "(CONTINUED)";
	public AirBusPartParser(String fileName) throws IOException {
		super(fileName);
		// TODO Auto-generated constructor stub
	}
	
	public void parse(int pageNo, String fileName) throws IOException{
		String[] lines = this.read(pageNo);
		int length = lines.length;
		//System.out.println("Length:"+length);
		//System.out.println("Figure line:"+lines[length-FOOT_LINE_NUM]);
		if(lines[length-FOOT_LINE_NUM].contains(FIGURE_KEYWORD)){
			return;
		}else{
			String chsesu = lines[length-FOOT_LINE_NUM].trim(); 
			this.extractor.writeToFile(fileName+chsesu+".txt", lines, 0, length-FOOT_LINE_NUM, new String[]{}, true);
		}
	}
	

}
