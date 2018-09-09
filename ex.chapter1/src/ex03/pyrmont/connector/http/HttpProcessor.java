package ex03.pyrmont.connector.http;

import ex03.pyrmont.ServletProcessor;
import ex03.pyrmont.StaticResourceProcessor;

import java.net.Socket;
import java.io.OutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.apache.catalina.util.RequestUtil;
import org.apache.catalina.util.StringManager;
//连接器   创建
/* this class used to be called HttpServer */

/*    http请求示例：：：：：：
GET/sample.jspHTTP/1.1
Accept:image/gif.image/jpeg,* /*
Accept-Language:zh-cn
Connection:Keep-Alive
Host:localhost
User-Agent:Mozila/4.0(compatible;MSIE5.01;Window NT5.0)
Accept-Encoding:gzip,deflate
 
username=user&password=passworld*/




/**
 * 
 一个http请求的格式 如下所示：
<request-line>  :请求的类型，要访问的资源以及使用的HTTP的版本
<headers>   ：用来说明服务器要使用的附加信息
<blank line>   ： 空行
[<request-body>] ： 主体body 可以添加任意的数据

在HTTP请求中，第一行必须是一个请求行（request line），用来说明请求类型、要访问的资源以及使用的HTTP版本。
紧接着是一个首部（header）小节，
用来说明服务器要使用的附加信息。在首部之后是一个空行，再此之后可以添加任意的其他数据[称之为主体（body）]。
 */
public class HttpProcessor {

  public HttpProcessor(HttpConnector connector) {
    this.connector = connector;
  }
  /**
   * The HttpConnector with which this processor is associated.
   */
  private HttpConnector connector = null;
  private HttpRequest request;
  private HttpRequestLine requestLine = new HttpRequestLine();
  private HttpResponse response;

  protected String method = null;
  protected String queryString = null;

  /**
   * The string manager for this package.
   */
  protected StringManager sm =
    StringManager.getManager("ex03.pyrmont.connector.http");

  public void process(Socket socket) {
	 
    SocketInputStream input = null;
    OutputStream output = null;
    try {
      input = new SocketInputStream(socket.getInputStream(), 2048);
      output = socket.getOutputStream();

      // create HttpRequest object and parse
      request = new HttpRequest(input);

      // create HttpResponse object
      response = new HttpResponse(output);
      response.setRequest(request);
      //设置响应头
      response.setHeader("Server", "Pyrmont Servlet Container");

      parseRequest(input, output);//  解析   GET  HTTP   queryString sessionid  等 放入request中
      parseHeaders(input);// 解析请求头部，  keep-alive,浏览器版本 cookie。。。等

      //check if this is a request for a servlet or a static resource
      //a request for a servlet begins with "/servlet/"
      if (request.getRequestURI().startsWith("/servlet/")) {
        ServletProcessor processor = new ServletProcessor();
        processor.process(request, response);
      }
      else {
    	  //load static  resource   like  .html 
        StaticResourceProcessor processor = new StaticResourceProcessor();
        processor.process(request, response);
      }

      // Close the socket
      socket.close();
      // no shutdown for this application
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 该方法是同类方法的简化版

org.apache.catalina.connector.http.HttpProcessor。

但是，这个方法只解析一些“简单”的头文件，例如

“cookie”、“内容长度”和“内容类型”，忽略其他标题。
   * This method is the simplified version of the similar method in
   * org.apache.catalina.connector.http.HttpProcessor.
   * However, this method only parses some "easy" headers, such as
   * "cookie", "content-length", and "content-type", and ignore other headers.
   * @param input The input stream connected to our socket
   *
   * @exception IOException if an input/output error occurs
   * @exception ServletException if a parsing error occurs
   */
  private void parseHeaders(SocketInputStream input)//  循环读取头部 
    throws IOException, ServletException {
    while (true) {
      HttpHeader header = new HttpHeader();;

      // Read the next header
      input.readHeader(header);
      if (header.nameEnd == 0) {
        if (header.valueEnd == 0) {
          return;
        }
        else {
          throw new ServletException
            (sm.getString("httpProcessor.parseHeaders.colon"));
        }
      }

      String name = new String(header.name, 0, header.nameEnd);
      String value = new String(header.value, 0, header.valueEnd);
      request.addHeader(name, value);
      // do something for some headers, ignore others.
      if (name.equals("cookie")) {
        Cookie cookies[] = RequestUtil.parseCookieHeader(value);
        for (int i = 0; i < cookies.length; i++) {
          if (cookies[i].getName().equals("jsessionid")) {
            // Override anything requested in the URL
            if (!request.isRequestedSessionIdFromCookie()) {
              // Accept only the first session id cookie
              request.setRequestedSessionId(cookies[i].getValue());
              request.setRequestedSessionCookie(true);
              request.setRequestedSessionURL(false);
            }
          }
          request.addCookie(cookies[i]);
        }
      }
      else if (name.equals("content-length")) {
        int n = -1;
        try {
          n = Integer.parseInt(value);
        }
        catch (Exception e) {
          throw new ServletException(sm.getString("httpProcessor.parseHeaders.contentLength"));
        }
        request.setContentLength(n);
      }
      else if (name.equals("content-type")) {
        request.setContentType(value);
      }
    } //end while
  }

  /**
   * 解析  请求头和请求体   获取 GET等方法HTTP 协议 请求资源/index.html , queryString : key=value  session相关等。。。。
   * @param input
   * @param output
   * @throws IOException
   * @throws ServletException
   */
// 请求头  POST /index.php HTTP/1.1
  private void parseRequest(SocketInputStream input, OutputStream output)
    throws IOException, ServletException {
	 
    // Parse the incoming request line
    input.readRequestLine(requestLine);// 把request 写入requestLine 然后 可以自动解析。
    String method =
      new String(requestLine.method, 0, requestLine.methodEnd);//   获取方法  get /post
    String uri = null;
    String protocol = new String(requestLine.protocol, 0, requestLine.protocolEnd);//协议  HTTP/1.1

    // Validate the incoming request line
    if (method.length() < 1) {
      throw new ServletException("Missing HTTP request method");
    }
    else if (requestLine.uriEnd < 1) {
      throw new ServletException("Missing HTTP request URI");
    }
    // Parse any query parameters out of the request URI
    int question = requestLine.indexOf("?");   //  是否存在请求体  ?key=value
    if (question >= 0) {
      request.setQueryString(new String(requestLine.uri, question + 1,
        requestLine.uriEnd - question - 1));
      uri = new String(requestLine.uri, 0, question);
    }
    else {
      request.setQueryString(null);
      uri = new String(requestLine.uri, 0, requestLine.uriEnd); //  /index.html
    }


    // Checking for an absolute URI (with the HTTP protocol) 判断是不是绝对资源符
    if (!uri.startsWith("/")) {
      int pos = uri.indexOf("://");
      // Parsing out protocol and host name
      if (pos != -1) {
        pos = uri.indexOf('/', pos + 3);
        if (pos == -1) {
          uri = "";
        }
        else {
          uri = uri.substring(pos);
        }
      }
    }

    // Parse any requested session ID out of the request URI  是一个会话标识符号  一般存在cookie中，要是查询字符串带过来 ，则保存
    String match = ";jsessionid=";
    int semicolon = uri.indexOf(match);
    if (semicolon >= 0) {
      String rest = uri.substring(semicolon + match.length());
      int semicolon2 = rest.indexOf(';');
      if (semicolon2 >= 0) {
        request.setRequestedSessionId(rest.substring(0, semicolon2));
        rest = rest.substring(semicolon2);
      }
      else {
        request.setRequestedSessionId(rest);
        rest = "";
      }
      request.setRequestedSessionURL(true);
      uri = uri.substring(0, semicolon) + rest;
    }
    else {
      request.setRequestedSessionId(null);
      request.setRequestedSessionURL(false);
    }

    // Normalize URI (using String operations at the moment)
    String normalizedUri = normalize(uri);// 判断 含不含有 非法字符   纠正部分URI 例如： \ 替换为 /

    // Set the corresponding request properties  设置相应的请求属性
    ((HttpRequest) request).setMethod(method);
    request.setProtocol(protocol);
    if (normalizedUri != null) {
      ((HttpRequest) request).setRequestURI(normalizedUri);
    }
    else {
      ((HttpRequest) request).setRequestURI(uri);
    }

    if (normalizedUri == null) {
      throw new ServletException("Invalid URI: " + uri + "'");
    }
  }

  /**
   * 返回上下文相关路径，以表示的“/”开头

在“..”和“..”元素之后指定路径的规范版本

解决了。如果指定的路径尝试超出

当前上下文的边界(例如，太多的“..”路径元素

，返回null。
   * Return a context-relative path, beginning with a "/", that represents
   * the canonical version of the specified path after ".." and "." elements
   * are resolved out.  If the specified path attempts to go outside the
   * boundaries of the current context (i.e. too many ".." path elements
   * are present), return <code>null</code> instead.
   *
   * @param path Path to be normalized
   */
  protected String normalize(String path) {
    if (path == null)
      return null;
    // Create a place for the normalized path
    String normalized = path;

    // Normalize "/%7E" and "/%7e" at the beginning to "/~"
    if (normalized.startsWith("/%7E") || normalized.startsWith("/%7e"))
      normalized = "/~" + normalized.substring(4);

    // Prevent encoding '%', '/', '.' and '\', which are special reserved
    // characters
    if ((normalized.indexOf("%25") >= 0)
      || (normalized.indexOf("%2F") >= 0)
      || (normalized.indexOf("%2E") >= 0)
      || (normalized.indexOf("%5C") >= 0)
      || (normalized.indexOf("%2f") >= 0)
      || (normalized.indexOf("%2e") >= 0)
      || (normalized.indexOf("%5c") >= 0)) {
      return null;
    }

    if (normalized.equals("/."))
      return "/";

    // Normalize the slashes and add leading slash if necessary  规范化斜杠，必要时添加前导斜杠
    if (normalized.indexOf('\\') >= 0)
      normalized = normalized.replace('\\', '/');
    if (!normalized.startsWith("/"))
      normalized = "/" + normalized;

    // Resolve occurrences of "//" in the normalized path
    while (true) {
      int index = normalized.indexOf("//");
      if (index < 0)
        break;
      normalized = normalized.substring(0, index) +
        normalized.substring(index + 1);
    }

    // Resolve occurrences of "/./" in the normalized path
    while (true) {
      int index = normalized.indexOf("/./");
      if (index < 0)
        break;
      normalized = normalized.substring(0, index) +
        normalized.substring(index + 2);
    }

    // Resolve occurrences of "/../" in the normalized path
    while (true) {
      int index = normalized.indexOf("/../");
      if (index < 0)
        break;
      if (index == 0)
        return (null);  // Trying to go outside our context
      int index2 = normalized.lastIndexOf('/', index - 1);
      normalized = normalized.substring(0, index2) +
        normalized.substring(index + 3);
    }

    // Declare occurrences of "/..." (three or more dots) to be invalid
    // (on some Windows platforms this walks the directory tree!!!)
    if (normalized.indexOf("/...") >= 0)
      return (null);

    // Return the normalized path that we have completed
    return (normalized);

  }

}
