package com.bryant.zj.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.bryant.zj.model.ColumnTitleModel;
import com.bryant.zj.model.EnumExcelType;
import com.bryant.zj.service.ExcelParserImpl;

public class SubServlet extends HttpServlet {
	private static final long serialVersionUID = 2823084223770197211L;

	private String[] getTitle(List<ColumnTitleModel> list) {
		String[] title = new String[list.size()];
		Collections.sort(list, new Comparator<ColumnTitleModel>(){

			@Override
			public int compare(ColumnTitleModel o1, ColumnTitleModel o2) {
				return o1.getCol().compareTo(o2.getCol());
			}
			
		});
		for (int i = 0; i < list.size(); i++) {
			title[i] = list.get(i).getTitle();
		}
		return title;
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String json = req.getParameter("data");
		if(json != null ) {
			List<ColumnTitleModel> list = JSON.parseArray(json, ColumnTitleModel.class);
			System.out.println(list);
			String[][] content = (String[][])req.getSession().getAttribute("content");

			BufferedOutputStream out = null;
	    	BufferedInputStream in = null;
	    	try {
	    		String filename = (String) req.getSession().getAttribute("filename") ;
	        	String path = req.getSession().getServletContext().getRealPath("/") + "/" + filename;
	        	//输出数据到指定文件
				new ExcelParserImpl().write(getTitle(list), content, path, EnumExcelType.EXCEL2007);
				//获取文件
				File file = new File(path);
				//设置response为excel
				response.setContentType("application/msexcel;charset=GBK");
				//设置输出文件名
		        response.setHeader("Content-Disposition", "attachment;filename=\""+ filename + "\"");
		        //设置文件大小
		        response.setContentLength((int) file.length());
		        
				in = new BufferedInputStream(new FileInputStream(file));
				out = new BufferedOutputStream(response.getOutputStream());
				int n = -1;
				byte[] b = new byte[4096];
				while( (n=in.read(b, 0, 4096)) > -1) {
					out.write(b, 0, n);
				}
				out.flush();
				response.flushBuffer();
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(in != null) {
					in.close();
				}
				if(out != null) {
					out.close();
				}
			}
		}
	}

}
