package com.ex02;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import com.ex02.Request;
import com.ex02.Response;

public class HttpServer2 {
	//关闭命令
	private static final String SHUTDOWN_COMMAND="/SHUTDOWN";
	//是否收到关闭命令
	private boolean shutdown=false;
	
	public static void main(String[] args) {
		HttpServer2 server=new HttpServer2();
		server.await();
	}

	private void await() {
		ServerSocket serverSocket=null;
		int port=8080;
		try {
			serverSocket=new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
		
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		//循环等待
		while(!shutdown){
			Socket socket=null;
			InputStream input=null;
			OutputStream output=null;
			
			try {
				socket=serverSocket.accept();
				System.out.println("收到请求："+socket);
				input=socket.getInputStream();
				output=socket.getOutputStream();
				//创建请求对象 并解析
				Request request=new Request(input);
				
				request.parse();
				//创建返回对象   利用监听返回的socket的输出流进行通信返回
				Response response=new Response(output);
				response.setRequest(request);
				//检查 请求是否是一个servlet 或者是静态 资源
				//检查servlet请求以 "/servlet" 开头
				if(request.getUri().startsWith("/servlet")){
					ServletProcessor2 processor=new ServletProcessor2();
					processor.process(request, response);
				}else{
					StaticResourceProcessor processor=new StaticResourceProcessor();
					processor.process(request, response);
				}
				
				//关闭socket连接
				socket.close();
				//检查 请求的是 关闭的 请求
				shutdown=request.getUri().equals(SHUTDOWN_COMMAND);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
		
			
		}
	}
	

}
