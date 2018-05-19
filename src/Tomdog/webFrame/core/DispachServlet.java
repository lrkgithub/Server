package Tomdog.webFrame.core;

import Tomdog.webFrame.ServletApi.HttpServlet;
import Tomdog.webFrame.annotation.Autoware;
import Tomdog.webFrame.annotation.Controller;
import Tomdog.webFrame.annotation.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DispachServlet extends HttpServlet {


    private Properties properties = new Properties();

    private List<String> classNames = new ArrayList<String>();

    private Map<String, Object> beanMap = new HashMap<String, Object>();

    private static final String PACKAGE = "package";

    @Override
    public void init() {

        doLoadConfig("WEB-INF/Resource.properties");

        doScanner(PACKAGE);

        doRegister();

        doAutoWared();

    }

    @Override
    public void destory() {
//      TODO
    }

    private void doLoadConfig(String location) {

        InputStream in = this.getClass().getClassLoader().getResourceAsStream(location);

        try {
            this.properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void doScanner(String packageLocation) {

        String packageName = properties.getProperty(packageLocation);

        ClassLoader classLoader = this.getClass().getClassLoader();

        URL url = classLoader.getResource("/" + packageLocation.replaceAll("\\.","//"));

        if (null == url || "".equals(url)) {
            return;
        }

        File file = new File(url.getFile());

        if (!file.exists() || !file.isDirectory() || null == file.list()) {
            return;
        }

        for (File subFile : file.listFiles()) {

            if (subFile.isDirectory()) {

                doScanner(packageName + "." + subFile.getName());

            } else {

                classNames.add(packageName + "." + subFile.getName().replace(".class", ""));

            }

        }

    }

    private void doRegister() {

        if (classNames.isEmpty()) {
            return;
        }

        for (String className : classNames) {

            Class clazz = null;

            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (null == clazz) {
                return;
            }

            String name = clazz.getSimpleName();

            String beanName = (name.substring(0, 1) + 32) + name.substring(1);


            if (clazz.isAnnotationPresent(Controller.class)) {

                Constructor[] constructors = clazz.getConstructors();

                if (constructors.length == 1) {
                    try {
                        this.beanMap.put(beanName, constructors[0].newInstance());
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }

            } else if (clazz.isAnnotationPresent(Service.class)) {

                Constructor[] constructors = clazz.getConstructors();

                Object instance = null;

                try {
                    instance = clazz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (null == instance) {
                    return;
                }

                this.beanMap.put(beanName, instance);

                Class<?>[] interfaces = clazz.getInterfaces();

                for (Class interfaceClass : interfaces) {

                    String interfaceName = interfaceClass.getSimpleName();

                    beanName = (interfaceName.substring(0, 1) + 32) + interfaceName.substring(1);

                    beanMap.put(beanName, instance);

                }

            }

        }

    }

    private void doAutoWared() {

        if (beanMap.isEmpty()) {
            return;
        }

        for (Map.Entry entry : beanMap.entrySet()) {

            Object obj = entry.getValue();

            Field[] fields = obj.getClass().getDeclaredFields();

            for (Field field : fields) {

                if (!field.isAnnotationPresent(Autoware.class)) {
                    continue;
                }

                Autoware autoware = field.getAnnotation(Autoware.class);
                String beanName = autoware.value().trim();

                if ("".equals(beanName)) {
                    beanName = field.getType().getName();
                }

                field.setAccessible(true);

                try {
                    field.set(obj, beanMap.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    private void initHandleMapping() {

    }

}
