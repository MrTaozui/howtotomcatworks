package com.ex01;
/**
 * 代表一个Http请求 从客户端请求的 inputstream对象来构造这个实例
 * @author taojiajun
 *
 */

import java.io.IOException;
import java.io.InputStream;

public class Request {
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
	
/*	public static void main(String[] args) {
		System.out.println("123 456".indexOf(' ',4));
	}
	*/
	
}
