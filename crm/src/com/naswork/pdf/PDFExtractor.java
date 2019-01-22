package com.naswork.pdf;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public abstract class PDFExtractor {
	
	protected int numOfPage;
	
	public abstract void open(String fileName)
			 throws IOException;
	public abstract String[] extract(int pageNo)
			 throws IOException;
	public abstract void close()
			 throws IOException;
	public void writeToFile(String fileName, String[] lines, boolean append) throws IOException{
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName, append));
		for(String line: lines){
			out.write(line);
			out.newLine();			
		}
		out.close();
	}

	public void writeToFile(String fileName, String[] lines, int startRow, int endRow, String[] filterList, boolean append) throws IOException{
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName, append));
		for(int i=startRow; i<endRow; i++){
			String line = lines[i];
			boolean filtered = false;
			for(String keyword: filterList){
				if(line.contains(keyword)){
					filtered = true;
					break;
				}					
			}
			if(filtered){
				continue;
			}
			out.write(line);
			out.newLine();			
		}
		out.close();
	}

}
