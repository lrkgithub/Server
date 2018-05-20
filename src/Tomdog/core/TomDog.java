package Tomdog.core;

import Tomdog.core.Connector;
import Tomdog.webFrame.ServletApi.Servlet;
import Tomdog.webFrame.context.ApplicationContext;
import Tomdog.webFrame.core.DispachServlet;

public class TomDog {

    static ApplicationContext applicationContext;

    public static void main(String[] args) {

        applicationContext = new ApplicationContext("WEB-INF/Resource.properties");

        Connector connector = new Connector();
        connector.start();
    }
}
