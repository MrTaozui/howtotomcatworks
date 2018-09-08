package com.ex02;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

public class ServletProcessor2 {
	public void process(Request request,Response response){
		String uri=request.getUri();
		String servletName=uri.substring(uri.lastIndexOf("/")+1);
		URLClassLoader loader=null;
		try {
		//创建 urlClassLoader
		URL[] urls=new URL[1];
		URLStreamHandler streamHandler=null;
		File classPath=new File(Constants.WEB_ROOT);
	//  file:E:\eclipse_workSpace\howtotomcatworks\ex.chapter1\webroot\	
			String repository=new URL("file", null, classPath.getCanonicalPath()+File.separator).toString();
		urls[0]=new URL(null, repository, streamHandler);
		loader=new URLClassLoader(urls);
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		Class myClass=null;
		try {
			myClass=loader.loadClass(servletName);
		} catch (ClassNotFoundException e) {
			System.out.println(e.toString());
		}
		
		Servlet servlet=null;
		
		try {
			servlet=(Servlet)myClass.newInstance();
			RequestFacade requestFacade=new RequestFacade(request);
			ResponseFacade responseFacade=new ResponseFacade(response);
			servlet.service(requestFacade, responseFacade);
		}  catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	public static void main(String[] args) {
		File classPath=new File(Constants.WEB_ROOT);
		try {
			String repository=new URL("file", null, classPath.getCanonicalPath()+File.separator).toString();
		System.out.println(repository);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
