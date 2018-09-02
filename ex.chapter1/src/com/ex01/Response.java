package com.ex01;
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

public class Response {
	private static final int BUFFER_SIZE=1024;
	//根据请求 才能返回处理  所以持有一个请求对象
	Request request;
	OutputStream output;
	
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
			
		
		File file=new File(HttpServer.WEB_ROOT,request.getUri());
		if(file.exists()){
			fis=new FileInputStream(file);
			int ch=fis.read(bytes, 0, BUFFER_SIZE);
			while(ch!=-1){
				output.write(bytes, 0, ch);
				System.out.println(new String(bytes));
				ch=fis.read(bytes,0,BUFFER_SIZE);
			}
		}else{
			//文件不存在
			   String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
				          "Content-Type: text/html\r\n" +
				          "Content-Length: 23\r\n" +
				          "\r\n" +
				          "<h1>File Not Found</h1>";
			   output.write(errorMessage.getBytes());
		}
		} catch (Exception e) {
			System.out.println(e.toString());
		}finally{
			if(fis!=null){
				fis.close();
			}
		}
	}
		
	
	
}
