package com.bryant.zj.service;

import java.io.InputStream;

import com.bryant.zj.model.EnumExcelType;

public interface ExcelParser {
	public String[] readExcelTitle(InputStream in) throws Exception ;
	public String[][] readExcelContent(InputStream in) throws Exception ;
	public void write(String[] title, String[][] content, String path, EnumExcelType type) throws Exception;
}
