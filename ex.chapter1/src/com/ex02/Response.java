package com.ex02;
/**
 * HTTP 返回响应
 * @author taojiajun
 *
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

public class Response implements ServletResponse{
	private static final int BUFFER_SIZE=1024;
	//根据请求 才能返回处理  所以持有一个请求对象
	Request request;
	OutputStream output;
	PrintWriter writer;
	public Response(OutputStream output){
		this.output=output;
	}
	public void setRequest(Request request){
		this.request=request;
	}
	/**
	 * 发送静态资源
	 */
	public void sendStaticResource() throws IOException{
		byte[]bytes=new byte[BUFFER_SIZE];
		FileInputStream fis=null;	
		try {
			
		
		File file=new File(Constants.WEB_ROOT,request.getUri());
		fis=new FileInputStream(file);
		
			
			int ch=fis.read(bytes, 0, BUFFER_SIZE);
			while(ch!=-1){
				output.write(bytes, 0, ch);
				System.out.println(new String(bytes));
				ch=fis.read(bytes,0,BUFFER_SIZE);
			}
		
			
		
		} catch (Exception e) {
			//文件不存在
			   String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
				          "Content-Type: text/html\r\n" +
				          "Content-Length: 23\r\n" +
				          "\r\n" +
				          "<h1>File Not Found</h1>";
			   output.write(errorMessage.getBytes());
		}finally{
			if(fis!=null){
				fis.close();
			}
		}
	}
	/**ServletResponse接口实现开始**/
	public void flushBuffer() throws IOException {
		// TODO Auto-generated method stub
		
	}
	public int getBufferSize() {
		// TODO Auto-generated method stub
		return 0;
	}
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}
	public ServletOutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	public PrintWriter getWriter() throws IOException {
		// autoflash 为true的时候  println() 刷新 否则不
		writer=new PrintWriter(output,true);
		return writer;
	}
	public boolean isCommitted() {
		return false;
	}
	public void reset() {
		
	}
	public void resetBuffer() {
		
	}
	public void setBufferSize(int arg0) {
		
	}
	public void setContentLength(int arg0) {
		
	}
	public void setContentType(String arg0) {
		
	}
	public void setLocale(Locale arg0) {
		
	}
		
	/**ServletResponse接口实现结束**/
	
}
