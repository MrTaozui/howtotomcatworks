package com.ex02;
/**
 * 代表一个Http请求 从客户端请求的 inputstream对象来构造这个实例
 * 
 * 主要内容  解析是什么请求 请求的路径等
 * @author taojiajun
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;

public class Request implements ServletRequest{
	private InputStream input;
	private String uri;
	public Request(InputStream input){
		this.input=input;
	}
	public void parse(){
		//从socket中读取
		StringBuffer request=new StringBuffer(2048);
		int i;
		byte[] buffer=new byte[2048];
		try {
			i=input.read(buffer);//读取2048 会包含  方法 -资源-协议
		} catch (IOException e) {
			e.printStackTrace();
			i=-1;
		}
		for(int j=0;j<i;j++){
			request.append((char)buffer[j]);
		}
		System.out.println(request.toString());
		uri=parseUri(request.toString());
	}
	
	/**
	 * GET uri 
	 * 从输入流中 取出 所请求的资源 地址
	 */	
	private String parseUri(String requestString) {
		int index1,index2;
		index1=requestString.indexOf(' ');
		if(index1!=-1){//找不到则为-1
			index2=requestString.indexOf(' ',index1+1);//从index+1 后开始截取
			if(index2>index1){//找到了
				return requestString.substring(index1+1, index2);
			}
		}
		return null;
	}
	/**
	 * 获取客户端请求的资源  
	 * @return
	 */
	public String getUri() {
		return uri;
	}
	/**
	 * 实现 servletRequest接口 start
	 */
	public Object getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}
	public int getContentLength() {
		// TODO Auto-generated method stub
		return 0;
	}
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}
	public ServletInputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}
	public Enumeration getLocales() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getParameter(String arg0) {
		
		return null;
	}
	public Map getParameterMap() {
		
		return null;
	}
	public Enumeration getParameterNames() {
		
		return null;
	}
	public String[] getParameterValues(String arg0) {
		
		return null;
	}
	public String getProtocol() {
		
		return null;
	}
	public BufferedReader getReader() throws IOException {
	
		return null;
	}
	public String getRealPath(String arg0) {
	
		return null;
	}
	public String getRemoteAddr() {
		
		return null;
	}
	public String getRemoteHost() {
		
		return null;
	}
	public RequestDispatcher getRequestDispatcher(String arg0) {
		
		return null;
	}
	public String getScheme() {
		
		return null;
	}
	public String getServerName() {
		
		return null;
	}
	public int getServerPort() {
		
		return 0;
	}
	public boolean isSecure() {
		
		return false;
	}
	public void removeAttribute(String arg0) {
	
		
	}
	public void setAttribute(String arg0, Object arg1) {
	
		
	}
	public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
		
		
	}
	/**
	 * 实现 servletRequest接口 end
	 */
	
/*	public static void main(String[] args) {
		System.out.println("123 456".indexOf(' ',4));
	}
	*/
	
}
