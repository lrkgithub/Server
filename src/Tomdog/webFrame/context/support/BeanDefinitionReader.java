package Tomdog.webFrame.context.support;

import Tomdog.webFrame.beans.BeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BeanDefinitionReader {

    private Properties config = new Properties();

    private List<String> registyBeanClasses = new ArrayList<String>();

    private static final String SCAN_PACKAGE = "scanPackage";

    public BeanDefinitionReader(String locations) {

        InputStream in = this.getClass().getResourceAsStream(locations.replaceAll("classpath:", ""));

        try {
            config.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public Properties getConfig() {
        return config;
    }

    public List<String> getRegistyBeanClasses() {
        return registyBeanClasses;
    }

    //    扫描class文件
    private void doScanner(String packageName) {

        URL url = this.getClass().getClassLoader().getResource(File.separator + packageName.replaceAll("\\.", File.separator));

        if (null == url) {
            return;
        }

        File classDir =  new File(url.getFile());

        if (classDir.isDirectory()
                || null == classDir.listFiles()) {
            return;
        }

        for (File file : classDir.listFiles()) {

            if (file.isDirectory()) {
                doScanner(packageName + "." + file.getName());
            } else {

                if (file.getName().endsWith(".class")) {
                    registyBeanClasses.add(packageName + "." + file.getName().replace(".class", ""));
                }

            }
        }

    }

    private BeanDefinition registerBean(String className) {

        if (this.registyBeanClasses.contains(className)) {

            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setClassName(className);
            beanDefinition.setFactoryBeanName(lowerCase(className.substring(className.indexOf("."))));

            return beanDefinition;
        }

        return null;
    }

    private String lowerCase(String s) {
        char[] chars = s.toCharArray();
        chars[0] += 32;

        return new String(chars);
    }

}
