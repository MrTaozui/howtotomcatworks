package com.ex01;
/**
 *  socket
 * @author taojiajun
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class HttpSocketSend {
	public void socketdome(){
		Socket socket;
	try {
	 socket=new Socket("127.0.0.1",8080);
	 OutputStream os=socket.getOutputStream();
	 boolean autoFlush=true;
	 PrintWriter out=new PrintWriter(os, autoFlush);
	 BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
	//往web server(tomcat) 发送一个http请求
	 out.println("GET /index.jsp HTTP/1.1");// 方法   统一资源符  版本号
	 out.println("Host: localhost:8080");
	 out.println("Connection: Close");
	 out.println();
	 //读取返回
	 boolean loop=true;
	 StringBuffer sb=new StringBuffer(8096);
	 while(loop){
		 if(in.ready()){
			 int i=0;
			 while(i!=-1){
				 i=in.read();
				 sb.append((char)i);
			 }
			 loop=false;
		 }
		 Thread.currentThread().sleep(50);
	 }
	 //控制台展示返回体
	 System.out.println(sb.toString());
	 socket.close();
	 
	} catch (Exception e) {
		e.printStackTrace();
	} 
	}
	public static void main(String[] args) {
		new HttpSocketSend().socketdome();
	}
}
