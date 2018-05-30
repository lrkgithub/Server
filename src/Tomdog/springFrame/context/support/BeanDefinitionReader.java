package Tomdog.springFrame.context.support;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BeanDefinitionReader {

    private Properties config = new Properties();

//    此处的List<String>必须是放的全限定名字
    private List<String> registyBeanClasses = new ArrayList<String>();

    private static final String SCAN_PACKAGE = "package";

    public BeanDefinitionReader(String locations) {

        InputStream in = this.getClass().getClassLoader().getResourceAsStream(locations);

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

    public boolean contains(String classFullName) {
        return this.registyBeanClasses.contains(classFullName);
    }

    public List<String> loadBeanDefinition() {
        doScanner(config.getProperty(SCAN_PACKAGE));
        return this.registyBeanClasses;
    }

    //    扫描class文件
    private void doScanner(String packageName) {

        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", File.separator));

        if (null == url) {
            return;
        }

        File classDir =  new File(url.getFile());

        if (!classDir.isDirectory()
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

    private String lowerCase(String s) {
        char[] chars = s.toCharArray();
        chars[0] += 32;

        return new String(chars);
    }

}
