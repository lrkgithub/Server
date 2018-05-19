package Tomdog.webFrame.beans;

public class BeanDefinition {

    private String className;
    private String FactoryBeanName;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getFactoryBeanName() {
        return FactoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        FactoryBeanName = factoryBeanName;
    }

    public boolean isLasyInit() {
        return false;
    }
}
