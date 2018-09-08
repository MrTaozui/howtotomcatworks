package com.ex02;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

/** 包装了一下 核心的方法 不能暴露在外面防止不安全的操作， 可以把这个类传递下去 给Servlet 
 * 的service方法调用  因为这个方法里面的request 和 response要暴露给其他人员调用的
* 这样不安全的方法还是有原来的类调用
**/
public class ResponseFacade implements ServletResponse {

	private ServletResponse response=null;
	 
	public ResponseFacade(Response response){
		this.response=response;
	}
	public void flushBuffer() throws IOException {
	    response.flushBuffer();
	  }

	  public int getBufferSize() {
	    return response.getBufferSize();
	  }

	  public String getCharacterEncoding() {
	    return response.getCharacterEncoding();
	  }

	  public Locale getLocale() {
	    return response.getLocale();
	  }

	  public ServletOutputStream getOutputStream() throws IOException {
	    return response.getOutputStream();
	  }

	  public PrintWriter getWriter() throws IOException {
	    return response.getWriter();
	  }

	  public boolean isCommitted() {
	    return response.isCommitted();
	  }

	  public void reset() {
	    response.reset();
	  }

	  public void resetBuffer() {
	    response.resetBuffer();
	  }

	  public void setBufferSize(int size) {
	    response.setBufferSize(size);
	  }

	  public void setContentLength(int length) {
	    response.setContentLength(length);
	  }

	  public void setContentType(String type) {
	    response.setContentType(type);
	  }

	  public void setLocale(Locale locale) {
	    response.setLocale(locale);
	  }
}
