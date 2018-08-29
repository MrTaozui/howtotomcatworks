package com.ex01;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 代表一个web服务器
 * @author taojiajun
 *
 */
public class HttpServer { 
	//System.getProperty(key) 获取系统变量
	public static final String WEB_ROOT=System.getProperty("user.dir")
			+File.separator+"webroot";
	//停止命令
	private static final String SHUTDOWN_COMMAND="/SHUTDOWN";
	//停止命令 接收
	private boolean shutdown=false;
	
	
	
	public static void main(String[] args) {
		HttpServer server=new HttpServer();
				server.await();
	}
	//等待HTTP请求 处理并返回给客户端，直至接收shutdown命令
	private void await(){
		ServerSocket serverSocket=null;
		int port=8080;
		try {
			//InetAddress---表示互联网协议（IP）地址
//InetAddress.getByName("www.163.com")在给定主机名的情况下确定主机的IP地址
           serverSocket=new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
		} catch (IOException e) {
			e.printStackTrace();
			//System.exit(0)是正常退出程序，而System.exit(1)或者说非0表示非正常退出程序
			System.exit(1);
		}
		//循环等待请求
		while(!shutdown){
			Socket socket=null;
			InputStream input=null;
			OutputStream output=null;
			
			try {
				socket=serverSocket.accept();
				input=socket.getInputStream();
				output=socket.getOutputStream();
				//创建请求对象 并解析
				Request request=new Request(input);
				request.parse();
				//创建返回对象   利用监听返回的socket的输出流进行通信返回
				Response response=new Response(output);
				response.setRequest(request);
				response.sendStaticResource();
				//关闭socket连接
				socket.close();
				//检查 请求的是 关闭的 请求
				shutdown=request.getUri().equals(SHUTDOWN_COMMAND);
			} catch (IOException e) {
				e.printStackTrace();
				continue;//本地请求出错  继续下一次请求
			}
			
		}
	}

}
