package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.ex01.HttpServer;

public class HttpServerTest {
	private static  final String WEB_ROOT=System.getProperty("user.dir");
	public static void main(String[] args) {
		new HttpServerTest().doServer();
	}
	
	public void doServer(){
		try {
			ServerSocket serverSocket=new ServerSocket(8080);
			Socket socoke= serverSocket.accept();
			InputStream in= socoke.getInputStream();
			printIn(in);
			OutputStream out=socoke.getOutputStream();
			byte[]bytes=new byte[1024];
			FileInputStream fis=null;	
			File file=new File("E:/index.html");
			if(file.exists()){
				fis=new FileInputStream(file);
				int ch=fis.read(bytes, 0, 1024);
				while(ch!=-1){
					out.write(bytes, 0, ch);
					ch=fis.read(bytes,0,1024);
				}
			}else{
				System.out.println("不存在");
			}
			out.close();
			in.close();
			socoke.close();
			serverSocket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
		}
	}
	private void printIn(InputStream in){
		//从socket中读取
				StringBuffer request=new StringBuffer(2048);
				int i;
				byte[] buffer=new byte[2048];
				try {
					i=in.read(buffer);//读取2048 会包含  方法 -资源-协议
				} catch (IOException e) {
					e.printStackTrace();
					i=-1;
				}
				for(int j=0;j<i;j++){
					request.append((char)buffer[j]);
				}
				System.out.println(request.toString());
	}

}
