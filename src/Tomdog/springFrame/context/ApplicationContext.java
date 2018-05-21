package Tomdog.springFrame.context;

import Tomdog.springFrame.annotation.Autoware;
import Tomdog.springFrame.annotation.Controller;
import Tomdog.springFrame.annotation.Service;
import Tomdog.springFrame.beans.BeanDefinition;
import Tomdog.springFrame.beans.BeanPostProcessor;
import Tomdog.springFrame.beans.BeanWrapper;
import Tomdog.springFrame.context.support.BeanDefinitionReader;
import Tomdog.springFrame.core.BeanFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext extends DefaultListableBeanFactory implements BeanFactory {

    private String configLocation;

    private BeanDefinitionReader reader;

    private Map<String, Object> beanCacheMap = new ConcurrentHashMap<String, Object>();

    private Map<String, BeanWrapper> beanWrapperMap = new ConcurrentHashMap<String, BeanWrapper>();

    public ApplicationContext(String configLocation) {
        this.configLocation = configLocation;
        refresh();
    }

    @Override
    public void refresh() {

        this.reader = new BeanDefinitionReader(this.configLocation);

//        从配置文件得到客户端编译的class文件位置，放在一个List<String>中，方便下一步加载进jvm，然后注册
        List<String> beanDefinitions = reader.loadBeanDefinition();

//       将这些客户端编译的类注册到IOC容器中
        doRegisty(beanDefinitions);

        doAutowrited();
    }

    @Override
    public Object getBean(String beanName) {

        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);

        BeanPostProcessor benaPostProcessor = new BeanPostProcessor();

        Object instance = instantionBean(beanDefinition);

        if (null == instance) {
            return null;
        }

        BeanWrapper beanWrapper = new BeanWrapper(instance);
        this.beanWrapperMap.put(beanName, beanWrapper);


        return this.beanWrapperMap.get(beanName).getWrapperInstance();
    }

    public String[] getBeanDefinitionNames() {

        return (String[])this.beanDefinitionMap.keySet().toArray();

    }

    private void doRegisty(List<String> beanDefinition) {

        for (String classFullname : beanDefinition) {

            Class<?> clazz = null;

            try {
                clazz = Class.forName(classFullname);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (null == clazz) {
                continue;
            }

            BeanDefinition bean = registeBean(classFullname);

            if (null != bean) {
                this.beanDefinitionMap.put(bean.getClassName(), bean);
            }

            Class<?>[] interfaces = clazz.getInterfaces();

            for (Class c : interfaces) {
                this.beanDefinitionMap.put(c.getName(), bean);
            }
        }
    }

    private BeanDefinition registeBean(String classFullName) {

        BeanDefinition beanDefinition = null;

        if (reader.contains(classFullName)) {
            beanDefinition = new BeanDefinition();
            beanDefinition.setClassName(classFullName);

            String classSimpleName = classFullName.substring(classFullName.lastIndexOf(".") + 1);
            String beanName = lowerFirstCharacter(classSimpleName.charAt(0)) + classSimpleName.substring(1);

            beanDefinition.setFactoryBeanName(beanName);
        }
        return beanDefinition;
    }

    private void doAutowrited() {

        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : this.beanDefinitionMap.entrySet()) {

            if (!beanDefinitionEntry.getValue().isLasyInit()) {
                Object instance = getBean(beanDefinitionEntry.getKey());
            }
        }

        for (Map.Entry<String, BeanWrapper> beanWrapperEntry : this.beanWrapperMap.entrySet()) {

            populateBean(beanWrapperEntry.getValue().getInstance());

        }

    }

    private Object instantionBean(BeanDefinition beanDefinition) {

        Object instance = null;
        String className = beanDefinition.getClassName();
        Class<?> clazz = null;

        if (this.beanCacheMap.containsKey(className)) {
            instance = beanCacheMap.get(className);
        } else {

            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (null != clazz) {

                try {
                    instance = clazz.getConstructors()[0].newInstance();
                    this.beanCacheMap.put(className, instance);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return instance;
    }

    private void populateBean(Object instance) {

        Class clazz = instance.getClass();

        if (!(clazz.isAnnotationPresent(Controller.class)
                || clazz.isAnnotationPresent(Service.class))) {
            return;
        }

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {

            if (! field.isAnnotationPresent(Autoware.class)) {
                continue;
            }

            String autowareBeanName = field.getAnnotation(Autoware.class).value().trim();

            if ("".equals(autowareBeanName)) {
                autowareBeanName = field.getType().getName();
            }

            field.setAccessible(true);

            try {
                field.set(instance, this.beanWrapperMap.get(autowareBeanName).getInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private char lowerFirstCharacter(char c) {

        if (c < 'z' && c > 'a') {
            return c;
        }

        if (c < 'Z' && c > 'A') {
            return (char)(c + 32);
        }

        return c;
    }
}
