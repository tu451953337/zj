package com.bryant.zj.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.bryant.zj.service.ExcelParser;
import com.bryant.zj.service.ExcelParserImpl;

public class UploadExcel extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			if (ServletFileUpload.isMultipartContent(req)) {
				DiskFileItemFactory dff = new DiskFileItemFactory();// 创建该对象
				ServletFileUpload sfu = new ServletFileUpload(dff);// 创建该对象
				FileItemIterator it = sfu.getItemIterator(req);
				// InputStream inputStream = null;
				int realcol = 0;
				int readcol = 0;
				while (it.hasNext()) {
					FileItemStream fileItemStream = it.next();
					InputStream inputStream = fileItemStream.openStream();
					if (fileItemStream.isFormField()) {
						String title = Streams.asString(inputStream, "utf-8");
						String[] titleArr = null;
						if (title.indexOf(",") > -1) {
							titleArr = title.split(",");
						} else if (title.indexOf("，") > -1) {
							titleArr = title.split("，");
						} else {
							req.getRequestDispatcher("error.html").forward(req,
									resp);
							return;
						}
						readcol = titleArr.length;
//						req.getSession().setAttribute("title", titleArr);

					} else {
						ExcelParser excelReader = new ExcelParserImpl();

						System.out.println();
						String[][] content = excelReader.readExcelContent(inputStream);

						System.out.println("获得Excel表格的内容:");
						if (content.length > 0) {
							if (content[0].length > 0) {
								realcol = content[0].length;
								String[] col = new String[content[0].length];
								for (int i = 0; i < content[0].length; i++) {
									col[i] = ((char) (i + 65)) + "";
								}
								req.setAttribute("col", col);
							}
							for (int i = 0; i < content.length; i++) {
								for (int j = 0; j < content[i].length; j++) {
									System.out.print(content[i][j] + "\t");
								}
								System.out.println();
							}
						}
						req.getSession().setAttribute("content", content);
						req.getSession().setAttribute("filename",
								fileItemStream.getName());
					}

				}
				if (realcol != readcol) {
					req.getRequestDispatcher("error.html").forward(req, resp);
					return;
				}
			}

			req.getRequestDispatcher("index.jsp").forward(req, resp);
		} catch (FileNotFoundException e) {
			System.out.println("未找到指定路径的文件!");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
