package Tomdog.core;

import Tomdog.springFrame.context.ApplicationContext;

public class TomDog {

    static ApplicationContext applicationContext;

    public static void main(String[] args) {

        applicationContext = new ApplicationContext("WEB-INF/Resource.properties");

        Connector connector = new Connector();
        connector.start();
    }
}
