package com.ex02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
/**
 * 包装了一下 核心的方法 不能暴露在外面防止不安全的操作， 可以把这个类传递下去给Servlet 
 * 的service方法调用 因为这个方法里面的request 和 response要暴露给其他人员调用的
 * 这样不安全的方法还是有原来的类调用
 * @author taojiajun
 *
 */
public class RequestFacade implements ServletRequest{
	private ServletRequest request=null;
	
	public RequestFacade(Request request){
		this.request=request;
	}
	
	 /* implementation of the ServletRequest*/
	  public Object getAttribute(String attribute) {
	    return request.getAttribute(attribute);
	  }

	  public Enumeration getAttributeNames() {
	    return request.getAttributeNames();
	  }

	  public String getRealPath(String path) {
	    return request.getRealPath(path);
	  }

	  public RequestDispatcher getRequestDispatcher(String path) {
	    return request.getRequestDispatcher(path);
	  }

	  public boolean isSecure() {
	    return request.isSecure();
	  }

	  public String getCharacterEncoding() {
	    return request.getCharacterEncoding();
	  }

	  public int getContentLength() {
	    return request.getContentLength();
	  }

	  public String getContentType() {
	    return request.getContentType();
	  }

	  public ServletInputStream getInputStream() throws IOException {
	    return request.getInputStream();
	  }

	  public Locale getLocale() {
	    return request.getLocale();
	  }

	  public Enumeration getLocales() {
	    return request.getLocales();
	  }

	  public String getParameter(String name) {
	    return request.getParameter(name);
	  }

	  public Map getParameterMap() {
	    return request.getParameterMap();
	  }

	  public Enumeration getParameterNames() {
	    return request.getParameterNames();
	  }

	  public String[] getParameterValues(String parameter) {
	    return request.getParameterValues(parameter);
	  }

	  public String getProtocol() {
	    return request.getProtocol();
	  }

	  public BufferedReader getReader() throws IOException {
	    return request.getReader();
	  }

	  public String getRemoteAddr() {
	    return request.getRemoteAddr();
	  }

	  public String getRemoteHost() {
	    return request.getRemoteHost();
	  }

	  public String getScheme() {
	   return request.getScheme();
	  }

	  public String getServerName() {
	    return request.getServerName();
	  }

	  public int getServerPort() {
	    return request.getServerPort();
	  }

	  public void removeAttribute(String attribute) {
	    request.removeAttribute(attribute);
	  }

	  public void setAttribute(String key, Object value) {
	    request.setAttribute(key, value);
	  }

	  public void setCharacterEncoding(String encoding)
	    throws UnsupportedEncodingException {
	    request.setCharacterEncoding(encoding);
	  }

}
