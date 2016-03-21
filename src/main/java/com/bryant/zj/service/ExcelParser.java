package com.bryant.zj.service;

import java.io.IOException;
import java.io.InputStream;

public interface ExcelParser {
	public String[] readExcelTitle(InputStream in) throws IOException ;
	public String[][] readExcelContent(InputStream in) throws IOException ;
	public void write(String[] title, String[][] content, String path) throws Exception;
}
