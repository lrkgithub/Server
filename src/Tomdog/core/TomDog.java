package Tomdog.core;

import Tomdog.core.Connector;
import Tomdog.webFrame.ServletApi.Servlet;
import Tomdog.webFrame.core.DispachServlet;

public class TomDog {

    static Servlet servlet;

    public static void main(String[] args) {

        servlet = new DispachServlet();

        servlet.init();

        Connector connector = new Connector();
        connector.start();
    }
}
