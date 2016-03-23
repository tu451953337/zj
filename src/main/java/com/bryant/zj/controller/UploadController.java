package com.bryant.zj.controller;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.bryant.zj.exception.ParameterException;
import com.bryant.zj.service.ExcelParser;

@Controller
@RequestMapping("/upload")
public class UploadController {
	
	@Autowired
	private ExcelParser excelParser;

	@RequestMapping(value="/excel", method={RequestMethod.POST})
	public String uploadExcel(HttpServletRequest request, ModelMap modelMap) throws IOException, Exception {
		String title = request.getParameter("title");
		if(!StringUtils.hasLength(title)) {
			throw new ParameterException("标题不能空");
		}
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
		Iterator<String> it = multipartRequest.getFileNames();
		while(it.hasNext()) {
			MultipartFile file = multipartRequest.getFile(it.next());
			String[][] content = excelParser.readExcelContent(file.getInputStream());
			modelMap.put("content", content);
			
			for(int i=0; i<content.length; i++) {
				for(int j=0; j<content[i].length; j++) {
					System.out.print(content[i][j]+"\t");
				}
				System.out.println();
			}
			
			if(content.length > 0) {
				int col = title.split(",").length;
				col = col == 1 ? title.split("，").length : col;
				if(col != content[0].length) {
					throw new ParameterException("标题个数和列数不匹配");
				}
				String[] colArr = new String[col];
				for(int i=0; i<col; i++) {
					colArr[i] = (char)(i+65) + "";
				}
				modelMap.put("col", colArr);
			}
		}
		return "index";
	}
	
	@RequestMapping("/sub")
	public void sub(HttpServletRequest request, HttpServletResponse response) {
		
	}
}
