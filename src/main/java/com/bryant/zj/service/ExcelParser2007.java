package com.bryant.zj.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelParser2007 implements ExcelParser{
	
    private XSSFWorkbook wb;
    private XSSFSheet sheet;
    private XSSFRow row;
    
    public ExcelParser2007(InputStream is) {
    	try {
            wb = new XSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取Excel表格表头的内容
     * @param InputStream
     * @return String 表头内容的数组
     */
    public String[] readExcelTitle() {
        sheet = wb.getSheetAt(0);
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        System.out.println("colNum:" + colNum);
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            //title[i] = getStringCellValue(row.getCell((short) i));
            title[i] = getCellFormatValue(row.getCell((short) i));
        }
        return title;
    }

    /**
     * 读取Excel数据内容
     * @param InputStream
     * @return Map 包含单元格数据内容的Map对象
     */
    public String[][] readExcelContent() {
        sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题
        String[][] content = new String[rowNum][colNum];
        for (int i = 0; i < rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            while (j < colNum) {
                // 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
                // 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
                // str += getStringCellValue(row.getCell((short) j)).trim() +
                // "-";
                content[i][j] = getCellFormatValue(row.getCell((short) j)).trim();
                j++;
            }
        }
        return content;
    }

    /**
     * 获取单元格数据内容为字符串类型的数据
     * 
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private String getStringCellValue(XSSFCell cell) {
    	if (cell == null) {
            return "";
        }
        String strCell = "";
        switch (cell.getCellType()) {
        case XSSFCell.CELL_TYPE_STRING:
            strCell = cell.getStringCellValue();
            break;
        case XSSFCell.CELL_TYPE_NUMERIC:
            strCell = String.valueOf(cell.getNumericCellValue());
            break;
        case XSSFCell.CELL_TYPE_BOOLEAN:
            strCell = String.valueOf(cell.getBooleanCellValue());
            break;
        case XSSFCell.CELL_TYPE_BLANK:
            strCell = "";
            break;
        default:
            strCell = "";
            break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        return strCell;
    }

    /**
     * 获取单元格数据内容为日期类型的数据
     * 
     * @param cell
     *            Excel单元格
     * @return String 单元格数据内容
     */
    public String getDateCellValue(XSSFCell cell) {
        String result = "";
        try {
            int cellType = cell.getCellType();
            if (cellType == XSSFCell.CELL_TYPE_NUMERIC) {
                Date date = cell.getDateCellValue();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                result = (calendar.get(Calendar.YEAR) + 1900) + "-" + (calendar.get(Calendar.MONTH) + 1)
                        + "-" + calendar.get(Calendar.DAY_OF_MONTH);
            } else if (cellType == XSSFCell.CELL_TYPE_STRING) {
                String date = getStringCellValue(cell);
                result = date.replaceAll("[年月]", "-").replace("日", "").trim();
            } else if (cellType == XSSFCell.CELL_TYPE_BLANK) {
                result = "";
            }
        } catch (Exception e) {
            System.out.println("日期格式不正确!");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据HSSFCell类型设置数据
     * @param cell
     * @return
     */
    private String getCellFormatValue(XSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
            case XSSFCell.CELL_TYPE_NUMERIC:
            case XSSFCell.CELL_TYPE_FORMULA: {
                // 判断当前的cell是否为Date
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 如果是Date类型则，转化为Data格式
                    
                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    //cellvalue = cell.getDateCellValue().toLocaleString();
                    
                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellvalue = sdf.format(date);
                    
                }
                // 如果是纯数字
                else {
                    // 取得当前Cell的数值
                    cellvalue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            }
            // 如果当前Cell的Type为STRIN
            case XSSFCell.CELL_TYPE_STRING:
                // 取得当前的Cell字符串
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            // 默认的Cell值
            default:
                cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }
    
    public static void write(String[] title, String[][] content, String path) throws Exception{
		// 第一步，创建一个workbook，对应一个Excel文件  
		XSSFWorkbook workbook = new XSSFWorkbook();
		
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet 
		XSSFSheet sheet = workbook.createSheet("sheet");
		
		// 第三步，创建标题行
		//定义格式
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell = null;
		for(int i=0; i<title.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(cellStyle);
		}
		
		// 第四步，创建数据行
		if(content != null) {
			for (int i = 0; i < content.length; i++) {
				row = sheet.createRow(i+1);
				for (int j = 0; j < content[i].length; j++) {
					row.createCell(j).setCellValue(content[i][j]);
				}
			}
		}
		
		//输出到指定路径
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(path);
			workbook.write(outputStream);
		} finally {
			if(outputStream != null) {
				outputStream.close();
			}
		}
	}

    public static void main(String[] args) {
        try {
//        	String path = "D:\\WorkspaceJingrongyun\\Code\\jinrongyun\\doc\\乾多多文档\\2015.7.24银行代码.xls";
        	String path="C:\\Users\\Administrator\\Documents\\repayment.xlsx";
            // 对读取Excel表格标题测试
            InputStream is = new FileInputStream(path);
            ExcelParser2007 excelReader = new ExcelParser2007(is);
            String[] title = excelReader.readExcelTitle();
            System.out.println("获得Excel表格的标题:");
            for (String s : title) {
                System.out.print(s + " ");
            }
            System.out.println();
            // 对读取Excel表格内容测试
            String[][] content = excelReader.readExcelContent();
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
        }
    }
}
