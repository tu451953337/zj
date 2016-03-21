package com.bryant.zj.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelParser2007 extends AbstractExcelParser{

    public static void main(String[] args) {
        try {
//        	String path = "D:\\WorkspaceJingrongyun\\Code\\jinrongyun\\doc\\乾多多文档\\2015.7.24银行代码.xls";
        	String path="C:\\Users\\Administrator\\Documents\\repayment.xlsx";
            // 对读取Excel表格标题测试
            InputStream is = new FileInputStream(path);
            ExcelParser excelReader = new ExcelParser2007();
            String[] title = excelReader.readExcelTitle(is);
            System.out.println("获得Excel表格的标题:");
            for (String s : title) {
                System.out.print(s + " ");
            }
            System.out.println();
            // 对读取Excel表格内容测试
            String[][] content = excelReader.readExcelContent(is);
            System.out.println("获得Excel表格的内容:");
            for (int i = 0; i < content.length; i++) {
            	for(int j=0; j<content[i].length; j++) {
            		System.out.print(content[i][j]+"\t");
            	}
            	System.out.println();
            }

        } catch (FileNotFoundException e) {
            System.out.println("未找到指定路径的文件!");
            e.printStackTrace();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

	@Override
	protected Workbook createWriteWorkbook() {
		return new XSSFWorkbook();
	}

	@Override
	protected Workbook createReadWorkbook(InputStream in) throws IOException {
		return new XSSFWorkbook(in);
	}
}
