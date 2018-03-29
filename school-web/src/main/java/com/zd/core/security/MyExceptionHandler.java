package com.zd.core.security;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.zd.core.util.ModelUtil;

@ControllerAdvice
public class MyExceptionHandler {
	private static Logger logger = Logger.getLogger(MyExceptionHandler.class);
	
	@Autowired
	private  HttpServletResponse response;
	
	@ExceptionHandler(value={Exception.class})
	public void handlerException(Exception e){
		logger.error("错误原因：【"+e.getMessage()+"】 出错堆栈跟踪："+ Arrays.toString( e.getStackTrace()));

		try {
			writeJSON(response, "{ \"success\": false, \"obj\":\"请求失败，请重试或联系管理员！\"}");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void writeJSON(HttpServletResponse response, String contents) throws IOException {
		if (ModelUtil.isNotNull(response)) {
			response.setContentType("text/html;charset=UTF-8;");
			Writer writer = null;
			try {
				response.setCharacterEncoding("UTF-8");
				writer = response.getWriter();
				writer.write(contents);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					writer.flush();
					writer.close();
					response.flushBuffer();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
}
