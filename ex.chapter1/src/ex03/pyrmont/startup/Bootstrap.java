package ex03.pyrmont.startup;

import ex03.pyrmont.connector.http.HttpConnector;
// 启动程序
public final class Bootstrap {
  public static void main(String[] args) {
    HttpConnector connector = new HttpConnector();
    connector.start();
  }
}